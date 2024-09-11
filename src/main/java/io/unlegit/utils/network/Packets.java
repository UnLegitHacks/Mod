package io.unlegit.utils.network;

import io.unlegit.interfaces.IMinecraft;
import io.unlegit.mixins.network.AccConnection;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.RunningOnDifferentThreadException;

public class Packets implements IMinecraft
{
    /** Sends a packet. */
    public static void send(Packet<?> packet)
    {
        connection().send(packet);
    }
    
    /** Sends a packet without the event triggering. */
    public static void sendNoEvent(Packet<?> packet)
    {
        connection().send(packet, null);
    }
    
    /** Simulates receiving a packet. */
    public static void receive(Packet<?> packet)
    {
        try { AccConnection.genericsFtw(packet, connection().getPacketListener()); }
        // Uncertain about the reason for this being thrown even if the code works
        catch (RunningOnDifferentThreadException ex) {}
    }
    
    private static Connection connection() { return mc.getConnection().getConnection(); }
}
