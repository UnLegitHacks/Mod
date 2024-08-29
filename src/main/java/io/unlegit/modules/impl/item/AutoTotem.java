package io.unlegit.modules.impl.item;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.entity.InvUtil;
import io.unlegit.utils.network.Packets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.item.Items;

@IModule(name = "Auto Totem", description = "Automatically equips a totem in your offhand.")
public class AutoTotem extends ModuleU
{
    private Deque<Packet<?>> packetDeque = new ConcurrentLinkedDeque<>();
    
    public void onUpdate()
    {
        if (!mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING))
        {
            int totemSlot = InvUtil.getSlot(mc.player.getInventory(),
                    stack -> stack.is(Items.TOTEM_OF_UNDYING));
            
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
        if (!packetDeque.isEmpty()) Packets.send(packetDeque.poll());
    }
    
    public void queue(Packet<?>... packets)
    {
        for (Packet<?> packet : packets) packetDeque.add(packet);
    }
}
