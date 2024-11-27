package io.unlegit.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.unlegit.UnLegit;
import io.unlegit.events.impl.entity.StrafeE;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin
{
    @ModifyExpressionValue(method = "moveRelative", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getYRot()F"))
    public float getYaw(float yaw)
    {
        if (!(((Entity) (Object) this) instanceof LocalPlayer)) return yaw;
        StrafeE e = StrafeE.get(yaw);
        UnLegit.events.post(e);
        return e.changed ? e.yaw : yaw;
    }
}
