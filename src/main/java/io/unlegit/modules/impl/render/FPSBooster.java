package io.unlegit.modules.impl.render;

import io.unlegit.events.impl.PacketReceiveE;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.world.entity.EntityType;

@IModule(name = "FPS Booster", description = "Disables armor stand and particle rendering.")
public class FPSBooster extends ModuleU
{
    public void onPacketReceive(PacketReceiveE e)
    {
        if (e.packet instanceof ClientboundLevelParticlesPacket) e.cancelled = true;
        else if (e.packet instanceof ClientboundAddEntityPacket)
        {
            ClientboundAddEntityPacket p = (ClientboundAddEntityPacket) e.packet;
            if (p.getType() == EntityType.ARMOR_STAND) e.cancelled = true;
        }
    }
}
