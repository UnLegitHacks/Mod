package io.unlegit.modules.impl.item;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundPickItemPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@IModule(name = "Auto Totem", description = "Automatically equips a totem in your offhand.")
public class AutoTotem extends ModuleU
{
    private Deque<Packet<?>> packetDeque = new ConcurrentLinkedDeque<>();
    
    public void onUpdate()
    {
        if (!mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING))
        {
            int totemSlot = getTotemSlot(mc.player.getInventory());
            
            if (totemSlot != -1 && packetDeque.isEmpty())
            {
                if (totemSlot < 9)
                {
                    /**
                     * Queues the slot of the item, the swap packet, and then back to
                     * the slot of the player's currently held item.
                     */
                    queue(new ServerboundSetCarriedItemPacket(totemSlot),
                          new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN),
                          new ServerboundSetCarriedItemPacket(mc.player.getInventory().selected));
                }
                
                else
                {
                    /**
                     * Queues the open inventory packet, the swap packet
                     * the pick item packet, and then the inventory close packet.
                     */
                    queue(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY),
                          new ServerboundPlayerActionPacket(ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN),
                          new ServerboundPickItemPacket(totemSlot),
                          new ServerboundContainerClosePacket(mc.player.inventoryMenu.containerId));
                }
            }
        }
        
        // Sends packets tick-by-tick which also avoids flagging Timer
        if (!packetDeque.isEmpty()) mc.getConnection().send(packetDeque.poll());
    }
    
    public int getTotemSlot(Inventory inventory)
    {
        for (int i = 0; i < inventory.items.size(); i++)
        {
            ItemStack stack = inventory.items.get(i);
            if (!stack.isEmpty() && stack.is(Items.TOTEM_OF_UNDYING)) return i;
        }
        
        return -1;
    }
    
    public void queue(Packet<?>... packets)
    {
        for (Packet<?> packet : packets) packetDeque.add(packet);
    }
}
