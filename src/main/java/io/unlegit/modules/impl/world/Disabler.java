package io.unlegit.modules.impl.world;

import io.unlegit.events.impl.network.PacketReceiveE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ModeSetting;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.utils.ElapTime;
import io.unlegit.utils.network.Packets;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ClientboundPingPacket;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

@IModule(name = "Disabler", description = "Attempts to bypass certain parts of anticheats.")
public class Disabler extends ModuleU
{
    public ModeSetting mode = new ModeSetting("Mode", "The mode for the disabler.", new String[] {"Ping Spoof"});
    public SliderSetting pingDelay = new SliderSetting("Delay", "The delay of the ping in ms.", 500, 500, 25000);
    private final Deque<Packet<?>> packets = new ConcurrentLinkedDeque<>();
    private final ElapTime elapTime = new ElapTime();

    public void onUpdate()
    {
        if (elapTime.passed((long) pingDelay.value))
        {
            while (!packets.isEmpty())
                Packets.receive(packets.poll());
        }
    }

    public void onPacketReceive(PacketReceiveE e)
    {
        if (e.packet instanceof ClientboundKeepAlivePacket || e.packet instanceof ClientboundPingPacket)
        {
            packets.add(e.packet);
            e.cancelled = true;
        }
    }

    public void onDisable()
    {
        super.onDisable();

        while (!packets.isEmpty())
            Packets.receive(packets.poll());
    }
}
