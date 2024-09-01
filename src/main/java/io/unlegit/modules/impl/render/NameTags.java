package io.unlegit.modules.impl.render;

import java.awt.Color;
import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.render.AccGraphics;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

@IModule(name = "Name Tags", description = "Allows you to customize name tags.")
public class NameTags extends ModuleU
{
    public ToggleSetting scale = new ToggleSetting("Scale", "Automatically magnifies name tags.", true),
                         infiniteRange = new ToggleSetting("Infinite Range", "Disables the 64 block limit.", true);

    private HashMap<Entity, Float> prevDistances = new HashMap<>();
    private GuiGraphics graphics = null;
    
    public void onUpdate()
    {
        if (mc.player.tickCount == 0) prevDistances.clear();
    }
    
    public void renderNameTag(Entity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float f)
    {
        double d = mc.getEntityRenderDispatcher().distanceToSqr(entity);
        if (d > 4096 && !infiniteRange.enabled) return;
        
        if (graphics == null) graphics = new GuiGraphics(mc, (BufferSource) multiBufferSource);
        ((AccGraphics) graphics).setPose(poseStack);
        Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(f));
        
        if (vec3 != null)
        {
            float scale = this.scale.enabled ? (float) Math.max(1, smoothDistanceTo(entity) / 5) : 1;
            String health = "Health: " + ((LivingEntity) entity).getHealth();
            int stringWidth = IFont.LARGE.getStringWidth(component.getString()) + 10;
            
            poseStack.pushPose();
            poseStack.translate(vec3.x, vec3.y + (0.75F * (scale / 1.15F)), vec3.z);
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            poseStack.scale(0.0125F * scale, -0.0125F * scale, 0.0125F * scale);
            
            poseStack.translate(0, 0, -0.0125F);
            graphics.fill(RenderType.gui(), -stringWidth / 2, 0, stringWidth / 2, 44, new Color(20, 20, 30, 150).getRGB());
            poseStack.translate(0, 0, 1.0125F);
            graphics.fill(RenderType.gui(), -stringWidth / 2, 40, stringWidth / 2, 44, new Color(255, 100, 100).getRGB());
            
            IFont.LARGE.drawCenteredString(graphics, component.getString(), -1, 5, Color.WHITE);
            IFont.NORMAL.drawString(graphics, health, -stringWidth / 2 + 5, 27, Color.WHITE.darker());
            
            poseStack.popPose();
        }
    }
    
    public float smoothDistanceTo(Entity entity)
    {
        float distance = mc.player.distanceTo(entity);
        if (!prevDistances.containsKey(entity)) prevDistances.put(entity, distance);
        
        float prevDistance = prevDistances.get(entity);
        
        if (prevDistance < distance) prevDistance += (distance - prevDistance) / 2;
        else if (distance < prevDistance) prevDistance -= (prevDistance - distance) / 2;
        
        if (prevDistance != distance) prevDistances.replace(entity, prevDistance);
        return prevDistance;
    }
}
