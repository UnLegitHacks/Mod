package io.unlegit.modules.impl.combat;

import io.unlegit.events.impl.PacketReceiveE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.SliderSetting;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.RunningOnDifferentThreadException;
import net.minecraft.world.phys.Vec3;

@IModule(name = "Velocity", description = "Reduces your knockback.")
public class Velocity extends ModuleU
{
    public SliderSetting horizontal = new SliderSetting("Horizontal (%)", "How much KB you should take horizontally.", 0, 100, 100),
                         vertical = new SliderSetting("Vertical (%)", "How much KB you should take vertically.", 0, 100, 100);
    
    public void onPacketReceive(PacketReceiveE e)
    {
        if (e.packet instanceof ClientboundSetEntityMotionPacket && mc.player != null)
        {
            ClientboundSetEntityMotionPacket packet = (ClientboundSetEntityMotionPacket) e.packet;
            
            if (packet.getId() != mc.player.getId()) return;
            e.cancelled = true;
            if (horizontal.currentValue == 0 && vertical.currentValue == 0) return;
            
            float horizontal = this.horizontal.currentValue / 100, vertical = this.vertical.currentValue / 100;
            Vec3 vec3 = new Vec3(packet.getXa() * horizontal, packet.getYa() * vertical, packet.getZa() * horizontal);
            packet = new ClientboundSetEntityMotionPacket(mc.player.getId(), vec3);
            try { packet.handle(mc.getConnection()); }
            // Not sure why they throw this EVEN if the code has been successfully executed lol
            catch (RunningOnDifferentThreadException ex) {}
        }
    }
}
