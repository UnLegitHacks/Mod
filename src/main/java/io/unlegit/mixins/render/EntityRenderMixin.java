package io.unlegit.mixins.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.impl.render.NameTags;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin implements IMinecraft
{
    @Inject(method = "renderNameTag", at = @At(value = "HEAD"), cancellable = true)
    public void customNameTag(Entity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f, CallbackInfo info)
    {
        NameTags nameTags = (NameTags) UnLegit.modules.get("Name Tags");
        
        if (nameTags.isEnabled() && entity instanceof LivingEntity)
        {
            nameTags.renderNameTag((LivingEntity) entity, component, poseStack, multiBufferSource, i, f);
            info.cancel();
        }
    }
}
