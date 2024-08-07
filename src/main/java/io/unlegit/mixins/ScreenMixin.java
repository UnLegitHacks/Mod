package io.unlegit.mixins;

import java.awt.Color;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.interfaces.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

@Mixin(Screen.class)
public class ScreenMixin implements IGui
{
    private static ResourceLocation background = ResourceLocation.fromNamespaceAndPath("unlegit", "background.png");
    private boolean flag = true;
    
    @Shadow
    public int width, height;
    @Shadow public Minecraft minecraft;
    
    @Overwrite
    public void renderPanorama(GuiGraphics guiGraphics, float f)
    {
        PoseStack poseStack = guiGraphics.pose();
        float scale = (float) minecraft.getWindow().getGuiScale();
        
        poseStack.pushPose();
        poseStack.last().pose().scale(1 / scale);
        
        if (flag) { background = withLinearScaling(background); flag = false; }
        int bH = (int) (height * scale), bW = (int) Math.max(bH * 3.21F, width * scale), mouseX = (int) minecraft.mouseHandler.xpos();
        
        GlStateManager._enableBlend();
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.blit(background, -mouseX / 3, 0, bW, bH, bW, bH, bW, bH);
        poseStack.popPose();
        
        guiGraphics.fill(0, 0, width, height, new Color(0, 0, 0, 50).getRGB());
    }
    
    @Inject(method = "init", at = @At(value = "HEAD"))
    public void resetTexture(CallbackInfo info) { flag = true; }
}
