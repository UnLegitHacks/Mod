package io.unlegit.modules.impl.misc;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.Setting;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.network.Packets;

import java.util.ArrayList;

@IModule(name = "Packet Blocker", description = "Allows you to block client -> server packets. May find disablers.")
public class PacketBlocker extends ModuleU
{
    public PacketBlocker()
    {
        ArrayList<Setting> packets = new ArrayList<>();

        for (String packet : Packets.c2s.keySet())
            packets.add(new ToggleSetting(packet, "Blocks this packet from going.", false));
        
        UnLegit.settings.putSettings(this, packets);
    }
    
    public void onPacketSend(PacketSendE e)
    {
        for (Setting setting : UnLegit.settings.get(this))
        {
            ToggleSetting toggle = (ToggleSetting) setting;

            if (toggle.enabled && Packets.c2s.get(toggle.name).getSimpleName().equals(e.packet.getClass().getSimpleName()))
            {
                e.cancelled = true;
                break;
            }
        }
    }
}
