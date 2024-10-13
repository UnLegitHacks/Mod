package io.unlegit.modules.impl.misc;

import java.util.ArrayList;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.Setting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.network.Packets;
import net.minecraft.network.protocol.Packet;

@IModule(name = "Packet Blocker", description = "Allows you to block client -> server packets. May find disablers.")
public class PacketBlocker extends ModuleU
{
    public PacketBlocker()
    {
        ArrayList<Setting> packets = new ArrayList<>();
        
        for (Class<? extends Packet<?>> packet : Packets.client)
            packets.add(new ToggleSetting(packet.getSimpleName().replace("Serverbound", "").replace("Packet", "P"), "Blocks this packet from going.", false));
        
        UnLegit.settings.putSettings(this, packets);
    }
    
    public void onPacketSend(PacketSendE e)
    {
        for (Setting setting : UnLegit.settings.get(this))
        {
            ToggleSetting toggle = (ToggleSetting) setting;
            
            if (toggle.enabled && toggle.name.equals(
                    e.packet.getClass().getSimpleName().replace("Serverbound", "").replace("Packet", "P")))
                e.cancelled = true;
        }
    }
}
