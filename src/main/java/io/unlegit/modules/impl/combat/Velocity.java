package io.unlegit.modules.impl.combat;

import io.unlegit.events.impl.network.PacketReceiveE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;
import io.unlegit.utils.network.Packets;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.Vec3;

@IModule(name = "Velocity", description = "Reduces your knockback.")
public class Velocity extends ModuleU
{
    public SliderSetting horizontal = new SliderSetting("Horizontal (%)", "How much KB you should take horizontally.", 0, 100, 100),
                         vertical = new SliderSetting("Vertical (%)", "How much KB you should take vertically.", 0, 100, 100);
    
    public void onPacketReceive(PacketReceiveE e)
    {
        if (e.packet instanceof ClientboundSetEntityMotionPacket packet && mc.player != null)
        {
            if (packet.getId() != mc.player.getId()) return;
            e.cancelled = true;
            
            if (horizontal.value == 0 && vertical.value == 0) return;
            
            float horizontal = this.horizontal.value / 100, vertical = this.vertical.value / 100;
            Vec3 vec3 = new Vec3(packet.getXa() * horizontal, packet.getYa() * vertical, packet.getZa() * horizontal);
            
            Packets.receiveNoEvent(new ClientboundSetEntityMotionPacket(mc.player.getId(), vec3));
        }
    }
}
