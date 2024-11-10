package io.unlegit.modules.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import io.unlegit.UnLegit;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.gui.AccGraphics;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;
import io.unlegit.utils.render.Colorer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.awt.*;

@IModule(name = "Name Tags", description = "Allows you to customize name tags.")
public class NameTags extends ModuleU implements IGui
{
    public ToggleSetting scale = new ToggleSetting("Scale", "Automatically magnifies name tags.", true),
                         infiniteRange = new ToggleSetting("Infinite Range", "Disables the 64 block limit.", true);
    
    private ResourceLocation leftShadow, centerShadow, rightShadow;
    private GuiGraphics graphics = null;
    
    public void renderNameTag(LivingEntity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float partialTicks)
    {
        if ("Vanilla".equals(UnLegit.THEME))
        {
            renderNameTagVanilla(entity, component, poseStack, multiBufferSource, i, partialTicks);
            return;
        }
        
        double d = mc.getEntityRenderDispatcher().distanceToSqr(entity);
        if (d > 4096 && !infiniteRange.enabled) return;
        
        if (graphics == null) graphics = new GuiGraphics(mc, (BufferSource) multiBufferSource);
        ((AccGraphics) graphics).setPose(poseStack);
        Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTicks));
        
        if (vec3 != null)
        {
            float scale = this.scale.enabled && entity instanceof Player ?
                    Math.max(1, smoothDistanceTo(entity, partialTicks) / 5) : 1,
                  
                    health = entity.getHealth();
            
            String healthText = "Health: " + (int) health;
            
            int stringWidth = Math.max(IFont.LARGE.getStringWidth(component.getString()), IFont.NORMAL.getStringWidth(healthText)) + 10,
                colorRGB = component.getStyle().getColor() == null ? -1 : component.getStyle().getColor().getValue();

            if (stringWidth < 30) return;
            
            poseStack.pushPose();
            poseStack.translate(vec3.x, vec3.y + (0.75 * (scale / 1.15)), vec3.z);
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            poseStack.scale(0.0125F * scale, -0.0125F * scale, 0.0125F * scale);
            int healthColor = Colorer.RGB(0, 255, 128);
            
            if (health > 10 && health < 20)
                healthColor = Colorer.blend((health - 10) / 10, Color.ORANGE.getRGB(), Colorer.RGB(0, 255, 128));
            else if (health <= 10)
                healthColor = Colorer.blend(health / 10, Colorer.RGB(255, 50, 50), Color.ORANGE.getRGB());
            
            drawShadows(stringWidth);
            GlStateManager._enableDepthTest();
            
            // Background
            poseStack.translate(0, 0, -0.0001F);
            graphics.fill(RenderType.guiOverlay(), -stringWidth / 2, 0, stringWidth / 2, 44, Colorer.RGB(20, 20, 30, 150));
            poseStack.translate(0, 0, 0.0001F);
            
            if (entity instanceof Player)
            {
                // Health
                poseStack.translate(-stringWidth / 2F, 0, 0);
                graphics.fill(RenderType.guiOverlay(), 0, 42, (int) (stringWidth * Math.min(health / entity.getMaxHealth(), 1)), 44, healthColor);
                poseStack.translate(stringWidth / 2F, 0, 0);
                
                GlStateManager._disableDepthTest();
                IFont.NORMAL.drawString(graphics, healthText, -stringWidth / 2 + 5, 27, -1);
            } else GlStateManager._disableDepthTest();

            if (component != null && component.getString() != null)
                IFont.LARGE.drawCenteredString(graphics, ChatFormatting.stripFormatting(component.getString()), -1, entity instanceof Player ? 4 : 10, new Color(colorRGB).getRGB());

            GlStateManager._disableBlend();
            poseStack.popPose();
        }
    }
    
    public void renderNameTagVanilla(LivingEntity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float partialTicks)
    {
        double d = mc.getEntityRenderDispatcher().distanceToSqr(entity);
        
        if (!(d > 4096) || infiniteRange.enabled)
        {
            Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTicks));
            
            if (vec3 != null)
            {
                int j = "deadmau5".equals(component.getString()) ? -10 : 0;
                float scale = this.scale.enabled && entity instanceof Player ? Math.max(1, smoothDistanceTo(entity, partialTicks) / 5) : 1;
                
                poseStack.pushPose();
                poseStack.translate(vec3.x, vec3.y + Math.max(0.5, (0.25 * scale)), vec3.z);
                poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
                poseStack.scale(0.025F * scale, 0.025F * -scale, 0.025F * scale);
                Matrix4f matrix4f = poseStack.last().pose();
                
                float g = mc.options.getBackgroundOpacity(0.25F);
                int k = (int) (g * 255) << 24;
                float h = -mc.font.width(component) / 2F;
                mc.font.drawInBatch(component, h, j, -1, false, matrix4f, multiBufferSource, Font.DisplayMode.SEE_THROUGH, k, i);
                poseStack.popPose();
            }
        }
    }
    
    public void drawShadows(int stringWidth)
    {
        if (leftShadow == null) leftShadow = get(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/nametags/left.png"));
        if (centerShadow == null) centerShadow = get(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/nametags/center.png"));
        if (rightShadow == null) rightShadow = get(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/nametags/right.png"));
        
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        drawShadow(graphics, leftShadow, -stringWidth / 2 - 8, -9, 27, 62, 27, 62, 27, 62);
        drawShadow(graphics, centerShadow, -stringWidth / 2 + 19, -9, 1, 62, Math.abs((-stringWidth / 2 - 8) - (stringWidth / 2 - 46)), 62, 1, 62);
        drawShadow(graphics, rightShadow, stringWidth / 2 - 19, -9, 27, 62, 27, 62, 27, 62);
    }
    
    public float smoothDistanceTo(Entity entity, float partialTicks)
    {
        float x = (float) (Mth.lerp(partialTicks, mc.player.xOld, mc.player.getX())
                - Mth.lerp(partialTicks, entity.xOld, entity.getX()));
        
        float y = (float) (Mth.lerp(partialTicks, mc.player.yOld, mc.player.getY())
                - Mth.lerp(partialTicks, entity.yOld, entity.getY()));
        
        float z = (float) (Mth.lerp(partialTicks, mc.player.zOld, mc.player.getZ())
                - Mth.lerp(partialTicks, entity.zOld, entity.getZ()));
        
        return Mth.sqrt(x * x + y * y + z * z);
    }
}
