package io.unlegit.modules.impl.world;

import io.unlegit.utils.nbs.NBSDecoder;
import io.unlegit.utils.nbs.model.Layer;
import io.unlegit.utils.nbs.model.Note;
import io.unlegit.utils.nbs.model.Song;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.entity.RotationUtil;
import io.unlegit.utils.network.Packets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

@IModule(name = "Note Bot", description = "Plays note blocks! Needs a 5x5 floor & NBS files.")
public class NoteBot extends ModuleU
{
    public ModeSetting nbsFile = new ModeSetting("NBS File", "The reference for the player.", new String[] {"404 (none)"}),
                       mode = new ModeSetting("Mode", "Legit is slower but it bypasses servers unlike blatant.", new String[]
                       {
                           "Blatant", "Legit"
                       });
    
    public ToggleSetting tune = new ToggleSetting("Tune", "Tunes the note blocks. If this is off, you have to manually tune them.", true),
                         highNotesOnly = new ToggleSetting("High Notes Only", "Reduces the notes being played by only playing medium-high ones.", false);
    
    public static final File nbsDirectory = new File("unlegit/nbs");
    private final HashMap<Integer, RelativeCoord> notes = new HashMap<>();
    private volatile boolean running = true;
    private volatile BlockPos pos = null;
    public float yaw, pitch;
    
    public void onEnable()
    {
        super.onEnable();
        
        if (mc.player == null)
        {
            setEnabled(false);
            return;
        }
        
        running = true;
        yaw = mc.player.getYRot(); pitch = mc.player.getXRot();
        
        Thread.ofVirtual().name("Note Block Player").start(() ->
        {
            if (!tune())
            {
                setEnabled(false);
                return;
            }
            
            File file = new File(nbsDirectory, nbsFile.selected);
            Song song = NBSDecoder.parse(file);
            int timeSlept = 0, makeUpTime = 0;
            
            if (song == null)
            {
                clientMessage(ChatFormatting.DARK_RED +
                        "The NBS file is not found. Disabling.");
                
                setEnabled(false);
                return;
            }
            
            clientMessage(ChatFormatting.GREEN +
                    "Now playing: " + file.getName().replace(".nbs", ""));
            
            for (int i = 0; i <= song.length(); i++)
            {
                for (Entry<Integer, Layer> layer : song.layerHashMap().entrySet())
                {
                    Layer lays = layer.getValue();
                    Note note = lays.getNote(i);
                    
                    // Instruments 3 and 4 are percussion
                    if (note != null && note.instrument() != 3 && note.instrument() != 4)
                    {
                        int noteBlock = note.key() - 33;
                        
                        if (!(highNotesOnly.enabled && noteBlock < 8))
                        {
                            RelativeCoord coord = notes.get(noteBlock);
                            
                            int blockX = (int) Math.round(coord.x() + mc.player.getX()), blockY = (int) Math.round(mc.player.getY()) - 1, blockZ = (int) Math.round(coord.z() + mc.player.getZ());
                            BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                            BlockState block = mc.level.getBlockState(pos);
                            
                            if (block.is(Blocks.NOTE_BLOCK))
                            {
                                this.pos = pos;
                                int delay = 30 + (int) (Math.random() * 20);
                                
                                if (mode.equals("Legit"))
                                {
                                    timeSlept += delay;
                                    try { Thread.sleep(delay); } catch (InterruptedException e) {}
                                }
                                
                                mc.gameMode.startDestroyBlock(pos, Direction.UP);
                                mc.player.swing(InteractionHand.MAIN_HAND);
                                
                                if (mode.equals("Legit"))
                                {
                                    timeSlept += delay;
                                    try { Thread.sleep(delay); } catch (InterruptedException e) {}
                                }
                            }
                        }
                    }
                }
                
                if (!running) break;
                
                int shouldSleep = (int) (1000 / (song.speed())),
                    difference = shouldSleep - timeSlept;
                
                if (difference > 0)
                {
                    try { Thread.sleep(Math.max(difference - makeUpTime, 0)); } catch (InterruptedException e) {}
                    makeUpTime = 0;
                } else makeUpTime -= difference;
                
                timeSlept = 0;
            }
            
            setEnabled(false);
        });
    }
    
    public boolean tune()
    {
        int note = 0;
        if (!checkNoteBlocks()) return false;
        else if (!tune.enabled) return true;
        
        for (double x = -2.5D; x < 2.5D; x++)
        {
            for (double z = -2.5D; z < 2.5D; z++)
            {
                int blockX = (int) Math.round(x + mc.player.getX()), blockY = (int) Math.round(mc.player.getY()) - 1, blockZ = (int) Math.round(z + mc.player.getZ());
                BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                BlockState block = mc.level.getBlockState(pos);
                int notePitch = block.getValue(NoteBlock.NOTE);
                
                while (notePitch != note)
                {
                    this.pos = pos;
                    mc.player.swing(InteractionHand.MAIN_HAND);
                    Packets.send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(blockX, blockY + 0.5D, blockZ), Direction.UP, pos, false), 1));
                    notePitch++; if (notePitch > 24) notePitch = 0;
                    int delay = mode.equals("Legit") ? (50 + (int) (100 * Math.random())) : 50;
                    
                    if (!running) return false;
                    try { Thread.sleep(delay); } catch (InterruptedException e) {}
                }
                
                note++;
            }
        }
        
        return true;
    }
    
    public void onMotion(MotionE e)
    {
        if (pos != null)
        {
            float[] rotations = RotationUtil.rotations(pos);
            e.yaw = rotations[0];
            e.pitch = rotations[1];
            e.changed = true;
        }
    }
    
    public boolean checkNoteBlocks()
    {
        for (double x = -2.5D; x < 2.5D; x++)
        {
            for (double z = -2.5D; z < 2.5D; z++)
            {
                int blockX = (int) Math.round(x + mc.player.getX()), blockY = (int) Math.round(mc.player.getY()) - 1, blockZ = (int) Math.round(z + mc.player.getZ());
                BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                BlockState block = mc.level.getBlockState(pos);
                
                if (!block.is(Blocks.NOTE_BLOCK))
                {
                    clientMessage(ChatFormatting.YELLOW +
                    
                    "Couldn't find all note blocks! Please make sure there is a 5x5 area of " +
                    "note blocks below you, & that you're standing in the center of it.");
                    
                    return false;
                }
            }
        }
        
        return true;
    }
    
    public void settingsReload()
    {
        if (!nbsDirectory.exists()) nbsDirectory.mkdirs();
        File[] nbsFiles = nbsDirectory.listFiles();
        
        if (nbsFiles != null && nbsFiles.length != 0)
        {
            String[] names = new String[nbsFiles.length];
            int i = 0;
            
            for (File nbs : nbsFiles)
            {
                names[i] = nbs.getName(); i++;
            }
            
            nbsFile.modes = names;
            
            if (nbsFile.selected.equals("404 (none)") || !new File(nbsDirectory, nbsFile.selected).exists())
                nbsFile.selected = names[0];
        }
    }
    
    public void onDisable()
    {
        super.onDisable();
        running = false;
    }
    

    public NoteBot()
    {
        int i = 0; for (double x = -2.5D; x < 2.5D; x++)
        {
            for (double z = -2.5D; z < 2.5D; z++)
            {
                notes.put(i, new RelativeCoord(x, z));
                i++;
            }
        }
    }
    
    record RelativeCoord(double x, double z) {}
}
