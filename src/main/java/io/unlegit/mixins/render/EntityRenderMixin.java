package io.unlegit.mixins.render;

import com.mojang.blaze3d.vertex.PoseStack;
import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.impl.render.NameTags;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin implements IMinecraft
{
    @Inject(method = "renderNameTag", at = @At(value = "HEAD"), cancellable = true)
    public void customNameTag(EntityRenderState entityRenderState, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo info)
    {
        NameTags nameTags = (NameTags) UnLegit.modules.get("Name Tags");
        Entity entity = null;

        for (Entity e : mc.level.entitiesForRendering())
        {
            if (e instanceof LivingEntity && e.getName().getVisualOrderText().equals(entityRenderState.nameTag.getVisualOrderText()))
                entity = e;
        }
        
        if (entity != null && nameTags.isEnabled())
        {
            nameTags.renderNameTag((LivingEntity) entity, component, poseStack, multiBufferSource, i, mc.getDeltaTracker().getGameTimeDeltaTicks());
            info.cancel();
        }
    }
}
