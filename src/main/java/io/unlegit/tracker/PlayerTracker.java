package io.unlegit.tracker;

import java.util.ArrayList;
import java.util.HashMap;

import io.unlegit.UnLegit;
import io.unlegit.events.EventListener;
import io.unlegit.interfaces.IMinecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * This class keeps track of the items of players (spies on them),
 * originally built for InvSee. May be powerful at times.
 */
public class PlayerTracker implements EventListener, IMinecraft
{
    public HashMap<Player, ArrayList<ItemStack>> items = new HashMap<>();
    private static PlayerTracker instance = new PlayerTracker();
    
    public void onUpdate()
    {
        for (Entity entity : mc.level.entitiesForRendering())
        {
            if (entity instanceof Player entityPlayer && !(entityPlayer instanceof LocalPlayer))
            {
                // For example, respawns
                if (teleported(entityPlayer) && items.containsKey(entityPlayer))
                    items.get(entityPlayer).clear();
                
                ItemStack heldItem = entityPlayer.getMainHandItem();
                
                if (heldItem != null && heldItem.getItem() != null && !heldItem.is(Items.AIR) && !heldItem.getDisplayName().getString().contains("Air"))
                {
                    if (!items.containsKey(entityPlayer))
                        items.put(entityPlayer, new ArrayList<>());
                    
                    if (!items.get(entityPlayer).contains(heldItem))
                    {
                        for (int i = 0; i < items.get(entityPlayer).size(); i++)
                        {
                            ItemStack item = items.get(entityPlayer).get(i);
                            
                            if (item.is(heldItem.getItem()))
                                items.get(entityPlayer).remove(item);
                        }
                        
                        items.get(entityPlayer).add(heldItem);
                    }
                }
            }
        }
    }
    
    public boolean teleported(Player player)
    {
        double x = Math.abs(player.xo - player.getX()),
               z = Math.abs(player.zo - player.getZ());
        return x > 32 || z > 32;
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
