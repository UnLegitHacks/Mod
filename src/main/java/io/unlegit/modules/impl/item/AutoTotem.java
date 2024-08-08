package io.unlegit.modules.impl.item;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@IModule(name = "Auto Totem", description = "Automatically equips a totem in your offhand.")
public class AutoTotem extends ModuleU
{
    public void onUpdate()
    {
        if (!mc.player.getOffhandItem().getItem().equals(Items.TOTEM_OF_UNDYING))
        {
            // int totemSlot = getTotemSlot(mc.player.getInventory());
        }
    }
    
    public int getTotemSlot(Inventory inventory)
    {
        for (int i = 0; i < inventory.items.size(); i++)
        {
            ItemStack stack = inventory.items.get(i);
            if (!stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING) return i;
        }
        
        return -1;
    }
}
