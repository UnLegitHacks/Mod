package io.unlegit.modules.impl.player;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.network.Packets;
import net.minecraft.network.protocol.Packet;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * This class also behaves as a component,
 * as such its methods may be utilized.
 */
@IModule(name = "Blink", description = "Freezes you serverside.")
public class Blink extends ModuleU
{
    private final Deque<Packet<?>> packets = new ConcurrentLinkedDeque<>();
    private static boolean on = false;
    
    public void onEnable()
    {
        super.onEnable();
        on = true;
        
        if (mc.player == null || mc.hasSingleplayerServer())
            toggle();
    }
    
    public void onWorldChange() { toggle(); }
    
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
        on = false;
        
        while (!packets.isEmpty())
            Packets.sendNoEvent(packets.poll());
    }
    
    /** Switches on the Blink module silently. */
    public static void switchOn()
    {
        UnLegit.events.register(UnLegit.modules.get("Blink"));
        on = true;
    }
    
    /** Switches off the Blink module silently. */
    public static void switchOff()
    {
        UnLegit.modules.get("Blink").onDisable();
    }
    
    public static boolean isOn() { return on; }
    public Blink() { noStart = true; }
}
