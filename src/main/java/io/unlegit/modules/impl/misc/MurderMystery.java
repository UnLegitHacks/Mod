package io.unlegit.modules.impl.misc;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.tracker.PlayerTracker;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

import java.util.ArrayList;
import java.util.Arrays;

import static net.minecraft.world.item.Items.*;

@IModule(name = "Murder Mystery", description = "Reveals the murderer in the game.")
public class MurderMystery extends ModuleU
{
    public ArrayList<Item> swordItems = new ArrayList<>();
    public Player murderer = null;
    
    public void onUpdate()
    {
        if (murderer == null)
        {
            Player player = PlayerTracker.get().findPlayerWithItem(this::illegal);
            
            if (player != null)
            {
                clientMessage(ChatFormatting.GOLD +
                        "Murderer: " + player.getName().getString());
                
                murderer = player;
            }
        }
    }
    
    public boolean illegal(ItemStack stack)
    {
        return stack.getItem() instanceof SwordItem || stack.getItem() instanceof ShovelItem
                || stack.getItem() instanceof AxeItem || stack.getItem() instanceof PickaxeItem
                || stack.getItem() instanceof HoeItem || stack.getItem() instanceof BoatItem
                || swordItems.stream().anyMatch(stack::is);
    }
    
    public void onWorldChange()
    {
        if (murderer != null) murderer = null;
    }
    
    public void add(Item... items)
    {
        swordItems.addAll(Arrays.asList(items));
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
    
    public void onEnable() { super.onEnable(); murderer = null; }
}
