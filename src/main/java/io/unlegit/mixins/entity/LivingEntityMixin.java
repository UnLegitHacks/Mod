package io.unlegit.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import io.unlegit.events.impl.entity.StrafeE;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    @ModifyExpressionValue(method = "jumpFromGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"))
    public float getYaw(float yaw)
    {
        if (!(((Entity) (Object) this) instanceof LocalPlayer)) return yaw;
        return StrafeE.get().changed ? StrafeE.get().yaw : yaw;
    }
}
