package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.impl.render.NameTags;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin implements IMinecraft
{
    @Redirect(method = "renderNameTag", at = @At(value = "INVOKE", target = "scale"))
    public void scaleCheck(PoseStack poseStack1, float x, float y, float z, Entity entity, Component component, PoseStack poseStack2, MultiBufferSource multiBufferSource, int i, float f)
    {
        NameTags nameTags = (NameTags) UnLegit.modules.get("Name Tags");
        float scale = x;
        
        if (nameTags.isEnabled() && nameTags.scale.enabled)
        {
            float distance = mc.player.distanceTo(entity);
            if (distance > 6) scale *= distance / 6;
        }
        
        poseStack1.scale(scale, -scale, scale);
    }
    
    @ModifyVariable(method = "renderNameTag", at = @At(value = "STORE"))
    public double distanceCheck(double distance, Entity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f)
    {
        NameTags nameTags = (NameTags) UnLegit.modules.get("Name Tags");
        return nameTags.isEnabled() && nameTags.infiniteRange.enabled ? 0 : distance;
    }
}
