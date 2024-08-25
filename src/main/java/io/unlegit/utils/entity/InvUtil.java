package io.unlegit.utils.entity;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InvUtil
{
    public static int getSlot(Inventory inventory, Item item)
    {
        for (int i = 0; i < inventory.items.size(); i++)
        {
            ItemStack stack = inventory.items.get(i);
            if (!stack.isEmpty() && stack.is(item)) return i;
        }
        
        return -1;
    }
}
