package io.unlegit.mixins.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;

@Mixin(Connection.class)
public interface AccConnection
{
    @Invoker("genericsFtw")
    public static <T extends PacketListener> void genericsFtw(Packet<T> packet, PacketListener packetListener) {}
}
