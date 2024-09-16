package io.unlegit.tracker;

import java.util.ArrayList;
import java.util.HashMap;

import io.unlegit.UnLegit;
import io.unlegit.events.EventListener;
import io.unlegit.interfaces.IMinecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * This class keeps track of the items
 * of players, useful for InvSee
 */
public class PlayerTracker implements EventListener, IMinecraft
{
    public HashMap<Player, ArrayList<ItemStack>> items = new HashMap<>();
    private static PlayerTracker instance = new PlayerTracker();
    
    public void onUpdate()
    {
        for (Entity entity : mc.level.entitiesForRendering())
        {
            if (entity instanceof Player entityPlayer)
            {
                ItemStack heldItem = entityPlayer.getMainHandItem();
                
                if (!heldItem.is(Items.AIR) && !heldItem.getDisplayName().getString().contains("Air"))
                {
                    if (!items.containsKey(entityPlayer))
                        items.put(entityPlayer, new ArrayList<>());
                    
                    if (items.get(entityPlayer).stream().filter(
                        
                        stack -> stack.getItem().equals(heldItem.getItem()))
                        
                        .count() == 0)
                    {
                        items.get(entityPlayer).add(heldItem.copy());
                    }
                }
            }
        }
    }
    
    public void onWorldChange()
    {
        items.clear();
    }
    
    public void start()
    {
        UnLegit.events.register(instance);
    }
    
    public static PlayerTracker get() { return instance; }
}
