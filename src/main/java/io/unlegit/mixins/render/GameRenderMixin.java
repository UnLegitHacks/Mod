package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.UnLegit;
import net.minecraft.client.renderer.GameRenderer;

@Mixin(GameRenderer.class)
public class GameRenderMixin
{
    @Inject(method = "bobHurt", at = @At(value = "HEAD"), cancellable = true)
    public void noHurtCamCheck(PoseStack pose, float partialTicks, CallbackInfo info)
    {
        if (UnLegit.modules.get("No Hurt Cam").isEnabled()) info.cancel();
    }
}
