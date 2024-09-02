package io.unlegit.mixins.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.StrafeE;
import net.minecraft.world.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin
{
    @ModifyExpressionValue(method = "moveRelative", at = @At(value = "INVOKE", target = "getYRot"))
    public float getYaw(float yaw)
    {
        StrafeE e = StrafeE.get(yaw);
        UnLegit.events.post(e);
        return e.yaw;
    }
}
