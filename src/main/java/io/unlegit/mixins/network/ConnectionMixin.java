package io.unlegit.mixins.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.network.PacketReceiveE;
import io.unlegit.events.impl.network.PacketSendE;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;

@Mixin(Connection.class)
public class ConnectionMixin
{
    @Inject(method = "send", at = @At(value = "HEAD"), cancellable = true)
    public void packetSendEvent(CallbackInfo info, @Local LocalRef<Packet<?>> packet)
    {
        PacketSendE e = PacketSendE.get(packet.get());
        UnLegit.events.post(e);
        if (e.cancelled) info.cancel();
    }
    
    @Inject(method = "channelRead0", at = @At(value = "HEAD"), cancellable = true)
    public void packetReceiveEvent(CallbackInfo info, @Local LocalRef<Packet<?>> packet)
    {
        PacketReceiveE e = PacketReceiveE.get(packet.get());
        UnLegit.events.post(e);
        if (e.cancelled) info.cancel();
    }
}
