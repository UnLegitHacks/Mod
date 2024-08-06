package io.unlegit.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.UpdateE;
import net.minecraft.client.player.LocalPlayer;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin
{
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "tick"))
    public void updateEvent(CallbackInfo info)
    {
        UnLegit.events.post(UpdateE.get());
    }
}
