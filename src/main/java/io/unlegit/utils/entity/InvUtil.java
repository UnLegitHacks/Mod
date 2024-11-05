package io.unlegit.utils.entity;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class InvUtil
{
    public static int getSlot(Inventory inventory, ItemFilter filter)
    {
        for (int i = 0; i < 9; i++)
        {
            ItemStack stack = inventory.items.get(i);
            if (!stack.isEmpty() && filter.process(stack)) return i;
        }
        
        return -1;
    }
    
    public interface ItemFilter
    {
        boolean process(ItemStack item);
    }
}
