package io.unlegit.events.impl.network;

import io.unlegit.events.Event;
import net.minecraft.network.protocol.Packet;

public class PacketSendE implements Event
{
    private static final PacketSendE e = new PacketSendE();
    public boolean cancelled = false;
    public Packet<?> packet;
    
    public static PacketSendE get(Packet<?> packet)
    {
        e.cancelled = false;
        e.packet = packet;
        return e;
    }
}
