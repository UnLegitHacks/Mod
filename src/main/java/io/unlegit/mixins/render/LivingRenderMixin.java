package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.render.NameTags;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;

@Mixin(LivingEntityRenderer.class)
public class LivingRenderMixin
{
    @WrapOperation(method = "shouldShowName", at = @At(value = "INVOKE", target = "distanceToSqr"))
    public double nameTagInfiniteRange(EntityRenderDispatcher dispatcher, Entity entity, Operation<Double> distance)
    {
        NameTags nameTags = (NameTags) UnLegit.modules.get("Name Tags"); 
        if (nameTags.isEnabled() && nameTags.infiniteRange.enabled) return 1;
        return distance.call(dispatcher, entity);
    }
}
