package io.unlegit.modules.impl.misc;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.model.*;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.entity.RotationUtil;
import io.unlegit.utils.network.Packets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

@IModule(name = "Note Block Player", description = "Plays note blocks! Needs a 5x5 floor & NBS files.")
public class NoteBlockPlayer extends ModuleU
{
    public ModeSetting nbsFile = new ModeSetting("NBS File", "The reference for the player.", new String[] {"404 (none)"}),
                       mode = new ModeSetting("Mode", "The mode of the player.", new String[]
                       {
                           "Blatant", "Legit"
                       });
    
    public ToggleSetting tune = new ToggleSetting("Tune", "Tunes the note blocks. If this is off, you have to manually tune them.", true);
    
    public static final File nbsDirectory = new File("unlegit/nbs");
    private HashMap<Integer, RelativeCoord> notes = new HashMap<>();
    private volatile boolean running = true;
    private volatile BlockPos pos = null;
    public float yaw, pitch;
    
    public NoteBlockPlayer()
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
    
    public void onEnable()
    {
        super.onEnable();
        
        if (mc.player == null)
        {
            toggle();
            return;
        }
        
        running = true;
        yaw = mc.player.getYRot(); pitch = mc.player.getXRot();
        
        Thread.ofVirtual().name("Note Block Player").start(() ->
        {
            if (!tune()) return;
            
            Song song = NBSDecoder.parse(new File(nbsDirectory, nbsFile.selected));
            int timeSlept = 0, makeUpTime = 0;
            
            if (song == null)
            {
                mc.player.sendSystemMessage(Component.literal(UnLegit.PREFIX + ChatFormatting.RED +
                
                "The NBS file is not found. Disabling."));
                
                toggle();
                return;
            }
            
            for (int i = 0; i < song.getLength(); i++)
            {
                for (Entry<Integer, Layer> layer : song.getLayerHashMap().entrySet())
                {
                    Layer lays = layer.getValue();
                    Note note = lays.getNote(i);
                    
                    if (note != null)
                    {
                        int noteBlock = note.getKey() - 33;
                        RelativeCoord coord = notes.get(noteBlock);
                        int blockX = (int) Math.round(coord.x() + mc.player.getX()), blockY = (int) Math.round(mc.player.getY()) - 1, blockZ = (int) Math.round(coord.z() + mc.player.getZ());
                        BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                        BlockState block = mc.level.getBlockState(pos);
                        
                        if (block.is(Blocks.NOTE_BLOCK))
                        {
                            this.pos = pos;
                            float[] rotations = smoothenRotations(RotationUtil.rotations(pos));
                            mc.player.setYRot(rotations[0]);
                            mc.player.setXRot(rotations[1]);
                            
                            if (mode.equals("Legit"))
                            {
                                timeSlept += 50;
                                try { Thread.sleep(50); } catch (InterruptedException e) {}
                            }
                            
                            mc.gameMode.startDestroyBlock(pos, Direction.UP);
                            mc.player.swing(InteractionHand.MAIN_HAND);
                            
                            if (mode.equals("Legit"))
                            {
                                timeSlept += 50;
                                try { Thread.sleep(50); } catch (InterruptedException e) {}
                            }
                        }
                    }
                }
                
                if (!running) break;
                
                int shouldSleep = (int) (800 / (20 / song.getDelay())),
                    difference = shouldSleep - timeSlept;
                
                if (difference > 0)
                {
                    try { Thread.sleep(Math.max(difference - makeUpTime, 0)); } catch (InterruptedException e) {}
                    makeUpTime = 0;
                } else makeUpTime += -difference;
                
                timeSlept = 0;
            }
            
            setEnabled(false);
        });
    }
    
    public boolean tune()
    {
        int note = 0;
        if (!checkNoteBlocks()) return false;
        else if (tune.enabled) return true;
        
        for (double x = -2.5D; x < 2.5D; x++)
        {
            for (double z = -2.5D; z < 2.5D; z++)
            {
                int blockX = (int) Math.round(x + mc.player.getX()), blockY = (int) Math.round(mc.player.getY()) - 1, blockZ = (int) Math.round(z + mc.player.getZ());
                BlockPos pos = new BlockPos(blockX, blockY, blockZ);
                BlockState block = mc.level.getBlockState(pos);
                int notePitch = block.getValue(NoteBlock.NOTE).intValue();
                
                while (notePitch != note)
                {
                    this.pos = pos;
                    mc.player.swing(InteractionHand.MAIN_HAND);
                    Packets.send(new ServerboundUseItemOnPacket(InteractionHand.MAIN_HAND, new BlockHitResult(new Vec3(blockX, blockY + 0.5D, blockZ), Direction.UP, pos, false), 1));
                    notePitch++; if (notePitch > 24) notePitch = 0;
                    int delay = mode.equals("Legit") ? (100 + (int) (100 * Math.random())) : 100;
                    
                    if (!running) return false;
                    try { Thread.sleep(delay); } catch (InterruptedException e) {}
                }
                
                note++;
            }
        }
        
        return true;
    }
    
    public void onUpdate()
    {
        if (pos != null && mode.equals("Legit"))
        {
            float[] rotations = smoothenRotations(RotationUtil.rotations(pos));
            mc.player.setYRot(rotations[0]);
            mc.player.setXRot(rotations[1]);
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
                    mc.player.sendSystemMessage(Component.literal(UnLegit.PREFIX + ChatFormatting.YELLOW +
                    
                    "Couldn't find all note blocks! Please make sure there is a 5x5 area of " +
                    "note blocks below you, & that you're standing in the center of it."));
                    
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
            nbsFile.selected = names[0];
        }
    }
    
    // Smooth rotations
    private float[] smoothenRotations(float[] rotations)
    {
        if (mode.equals("Blatant")) return new float[] {mc.player.getYRot(), mc.player.getXRot()};
        
        float yawDifference = Math.abs(rotations[0] - yaw), pitchDifference = Math.abs(rotations[1] - pitch);
        
        if (yaw < rotations[0])
        {
            if (yawDifference < 5) yaw = rotations[0];
            else yaw += (rotations[0] - yaw) / 2;
        }
        
        else if (yaw > rotations[0])
        {
            if (yawDifference < 5) yaw = rotations[0];
            else yaw -= (yaw - rotations[0]) / 2;
        }
        
        if (pitch < rotations[1])
        {
            if (pitchDifference < 5) pitch = rotations[1];
            else pitch += (rotations[1] - pitch) / 2;
        }
        
        else if (pitch > rotations[1])
        {
            if (pitchDifference < 5) pitch = rotations[1];
            else pitch -= (pitch - rotations[1]) / 2;
        }
        
        return new float[] {yaw, pitch};
    }
    
    public void onDisable()
    {
        super.onDisable();
        running = false;
    }
    
    record RelativeCoord(double x, double z) {}
}
