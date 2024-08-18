package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.UnLegit;
import io.unlegit.modules.impl.render.ESP;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LevelRenderer.class)
public class LevelRenderMixin
{
    @Inject(method = "renderEntity", at = @At(value = "HEAD"))
    public void chamsBegin(Entity entity, double x, double y, double z, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info)
    {
        ESP esp = (ESP) UnLegit.modules.get("ESP");
        
        if (esp.isEnabled() && esp.mode.equals("Chams") && entity instanceof LivingEntity)
        {
            GlStateManager._enablePolygonOffset();
            GlStateManager._polygonOffset(1, -1000000);
        }
    }
    
    @Inject(method = "renderEntity", at = @At(value = "TAIL"))
    public void chamsEnd(Entity entity, double x, double y, double z, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, CallbackInfo info)
    {
        ESP esp = (ESP) UnLegit.modules.get("ESP");
        
        if (esp.isEnabled() && esp.mode.equals("Chams") && entity instanceof LivingEntity)
        {
            GlStateManager._polygonOffset(1, 1000000);
            GlStateManager._disablePolygonOffset();
        }
    }
}
