package io.unlegit.events.impl.network;

import io.unlegit.events.Event;
import net.minecraft.network.protocol.Packet;

public class PacketReceiveE implements Event
{
    private static PacketReceiveE e = new PacketReceiveE();
    public boolean cancelled = false;
    public Packet<?> packet;
    
    public static PacketReceiveE get(Packet<?> packet)
    {
        e.cancelled = false;
        e.packet = packet;
        return e;
    }
}
