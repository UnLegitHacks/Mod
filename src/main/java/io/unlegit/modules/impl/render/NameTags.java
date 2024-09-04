package io.unlegit.modules.impl.render;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.UnLegit;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IModule;
import io.unlegit.mixins.render.AccGraphics;
import io.unlegit.modules.ModuleU;
import io.unlegit.modules.settings.impl.ToggleSetting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.Vec3;

@IModule(name = "Name Tags", description = "Allows you to customize name tags.")
public class NameTags extends ModuleU implements IGui
{
    public ToggleSetting scale = new ToggleSetting("Scale", "Automatically magnifies name tags.", true),
                         infiniteRange = new ToggleSetting("Infinite Range", "Disables the 64 block limit.", true);
    
    private ResourceLocation leftShadow, centerShadow, rightShadow;
    private GuiGraphics graphics = null;
    
    public void renderNameTag(LivingEntity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, float partialTicks)
    {
        double d = mc.getEntityRenderDispatcher().distanceToSqr(entity);
        if (d > 4096 && !infiniteRange.enabled) return;
        
        if (graphics == null) graphics = new GuiGraphics(mc, (BufferSource) multiBufferSource);
        ((AccGraphics) graphics).setPose(poseStack);
        Vec3 vec3 = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getViewYRot(partialTicks));
        
        if (vec3 != null)
        {
            float scale = this.scale.enabled ? (float) Math.max(1, smoothDistanceTo(entity, partialTicks) / 5) : 1,
                  health = entity.getHealth();
            
            String healthText = "Health: " + health;
            int stringWidth = Math.max(IFont.LARGE.getStringWidth(component.getString()), IFont.NORMAL.getStringWidth(healthText)) + 10;
            
            if (stringWidth < 30) return;
            
            poseStack.pushPose();
            poseStack.translate(vec3.x, vec3.y + (0.75F * (scale / 1.15F)), vec3.z);
            poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            poseStack.scale(0.0125F * scale, -0.0125F * scale, 0.0125F * scale);
            Color healthColor = new Color(0, 255, 75);
            
            if (health > 10 && health < 20)
                healthColor = blendColors((health - 10) / 10, Color.ORANGE, new Color(0, 255, 75));
            else if (health <= 10)
                healthColor = blendColors(health / 10, new Color(255, 50, 50), Color.ORANGE);
            
            drawShadows(stringWidth);
            GlStateManager._enableDepthTest();
            
            // Background
            poseStack.translate(0, 0, -0.0001F);
            graphics.fill(RenderType.guiOverlay(), -stringWidth / 2, 0, stringWidth / 2, 44, new Color(20, 20, 30, 150).getRGB());
            poseStack.translate(0, 0, 0.0001F);
            
            // Health
            poseStack.translate(-stringWidth / 2, 0, 0);
            graphics.fill(RenderType.guiOverlay(), 0, 40, (int) ((stringWidth - 1) * Math.min(health / entity.getMaxHealth(), 1)), 44, healthColor.getRGB());
            poseStack.translate(stringWidth / 2, 0, 0);
            GlStateManager._disableDepthTest();
            
            if ("Fancy".equals(UnLegit.THEME))
            {
                IFont.LARGE.drawCenteredString(graphics, ChatFormatting.stripFormatting(component.getString()), -1, 5, Color.WHITE);
                IFont.NORMAL.drawString(graphics, healthText, -stringWidth / 2 + 5, 27, Color.WHITE.darker());
            }
            
            else
            {
                poseStack.pushPose();
                poseStack.scale(2, 2, 2);
                mc.font.drawInBatch(ChatFormatting.stripFormatting(component.getString()), -stringWidth / 4 + 3, 4, Color.WHITE.getRGB(), false, poseStack.last().pose(), graphics.bufferSource(), DisplayMode.SEE_THROUGH, 1, 1);
                poseStack.popPose();
                
                mc.font.drawInBatch(healthText, -stringWidth / 2 + 5, 27, Color.WHITE.darker().getRGB(), false, poseStack.last().pose(), graphics.bufferSource(), DisplayMode.SEE_THROUGH, 1, 1);
            }
            
            GlStateManager._disableBlend();
            poseStack.popPose();
        }
    }
    
    public void drawShadows(int stringWidth)
    {
        if (leftShadow == null) leftShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/nametags/left.png"));
        if (centerShadow == null) centerShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/nametags/center.png"));
        if (rightShadow == null) rightShadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/nametags/right.png"));
        
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        drawShadow(graphics, leftShadow, -stringWidth / 2 - 8, -9, 27, 62, 27, 62, 27, 62);
        drawShadow(graphics, centerShadow, -stringWidth / 2 + 19, -9, 1, 62, Math.abs((-stringWidth / 2 - 8) - (stringWidth / 2 - 46)), 62, 1, 62);
        drawShadow(graphics, rightShadow, stringWidth / 2 - 19, -9, 27, 62, 27, 62, 27, 62);
    }
    
    private Color blendColors(float mixture, Color color1, Color color2)
    {
        int red = color1.getRed(), green = color1.getGreen(), blue = color1.getBlue();
        
        if (red < color2.getRed()) red += (color2.getRed() - color1.getRed()) * mixture;
        if (green < color2.getGreen()) green += (color2.getGreen() - color1.getGreen()) * mixture;
        if (blue < color2.getBlue()) blue += (color2.getBlue() - color1.getBlue()) * mixture;
        
        if (red > color2.getRed()) red -= (color1.getRed() - color2.getRed()) * mixture;
        if (green > color2.getGreen()) green -= (color1.getGreen() - color2.getGreen()) * mixture;
        if (blue > color2.getBlue()) blue -= (color1.getBlue() - color2.getBlue()) * mixture;
        
        return new Color(red, green, blue, color1.getAlpha());
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
