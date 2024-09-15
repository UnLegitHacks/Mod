package io.unlegit.modules.impl.player;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.network.Packets;
import net.minecraft.network.protocol.Packet;

@IModule(name = "Blink", description = "Freezes you serverside.")
public class Blink extends ModuleU
{
    private Deque<Packet<?>> packets = new ConcurrentLinkedDeque<>();
    
    public void onEnable()
    {
        super.onEnable();
        
        if (mc.player == null || mc.hasSingleplayerServer())
            toggle();
    }
    
    public void onPacketSend(PacketSendE e)
    {
        if (mc.player.tickCount > 20)
        {
            packets.add(e.packet);
            e.cancelled = true;
        }
    }
    
    public void onDisable()
    {
        super.onDisable();
        
        while (!packets.isEmpty())
            Packets.send(packets.poll());
    }
}
