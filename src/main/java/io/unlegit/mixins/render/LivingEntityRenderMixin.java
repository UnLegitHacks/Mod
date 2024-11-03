package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.MotionE;
import io.unlegit.modules.impl.misc.Rotations;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

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
}
