package io.unlegit.modules.impl.misc;

import static net.minecraft.world.item.Items.*;

import java.util.ArrayList;
import java.util.Map.Entry;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.tracker.PlayerTracker;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

@IModule(name = "Murder Mystery", description = "Provides features for the game Murder Mystery.")
public class MurderMystery extends ModuleU
{
    public ArrayList<Item> swordItems = new ArrayList<>();
    public Player murderer = null;
    
    public void onUpdate()
    {
        if (murderer == null)
        {
            for (Entry<Player, ArrayList<ItemStack>> entry : PlayerTracker.get().items.entrySet())
            {
                for (ItemStack stack : entry.getValue())
                {
                    if (illegal(stack))
                    {
                        murderer = entry.getKey();
                        clientMessage(UnLegit.PREFIX + ChatFormatting.GOLD +
                        
                                "Murderer: " + murderer.getName().getString());
                        
                        break;
                    }
                }
            }
        }
    }
    
    public boolean illegal(ItemStack stack)
    {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof ShovelItem
                || stack.getItem() instanceof AxeItem || stack.getItem() instanceof PickaxeItem
                || stack.getItem() instanceof HoeItem || stack.getItem() instanceof BoatItem
                || swordItems.stream().filter(item -> stack.is(item)).count() != 0;
    }
    
    public void onWorldChange()
    {
        if (murderer != null) murderer = null;
    }
    
    public void add(Item... items)
    {
        for (Item item : items) swordItems.add(item);
    }
    
    public MurderMystery()
    {
        add(GOLDEN_CARROT, CARROT, CARROT_ON_A_STICK, BONE,
            TROPICAL_FISH, PUFFERFISH, SALMON, BLAZE_ROD,
            PUMPKIN_PIE, NAME_TAG, APPLE, FEATHER,
            COOKIE, SHEARS, COOKED_SALMON, STICK,
            QUARTZ, ROSE_BUSH, ICE, COOKED_BEEF,
            NETHER_BRICK, COOKED_CHICKEN, MUSIC_DISC_BLOCKS,
            RED_DYE, OAK_BOAT, BOOK, GLISTERING_MELON_SLICE,
            JUNGLE_SAPLING, PRISMARINE_SHARD, SPONGE, DEAD_BUSH,
            REDSTONE_TORCH, CHORUS_PLANT);
    }
}
