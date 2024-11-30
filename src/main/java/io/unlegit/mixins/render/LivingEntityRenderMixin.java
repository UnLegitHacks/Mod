package io.unlegit.mixins.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.unlegit.UnLegit;
import io.unlegit.modules.impl.render.TrueSight;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRenderMixin
{
//    @Redirect(method = "render", at = @At(value = "INVOKE", target = "lerp", ordinal = 0))
//    private float rotationPitch(float f, float g, float h, @Local LocalRef<LivingEntity> livingEntity)
//    {
//        if (!(livingEntity.get() instanceof LocalPlayer)) return Mth.lerp(f, g, h);
//
//        Rotations rotations = (Rotations) UnLegit.modules.get("Rotations");
//
//        float pitch = rotations.isEnabled() && MotionE.get().changed ?
//                Mth.lerp(f, rotations.prevPitch, MotionE.get().pitch) : Mth.lerp(f, g, h);
//
//        return pitch;
//    }

    @ModifyExpressionValue(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;isBodyVisible(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Z"))
    private boolean isVisible(boolean visible)
    {
        TrueSight trueSight = (TrueSight) UnLegit.modules.get("True Sight");
        return (trueSight.isEnabled() && trueSight.invisibleEntities.enabled) || visible;
    }
}
