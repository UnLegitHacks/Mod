package io.unlegit.mixins.gui;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.ElapTime;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
import net.minecraft.resources.ResourceLocation;

@Mixin(Screen.class)
public class ScreenMixin implements IGui
{
    @Unique
    private static final boolean nightMode = ElapTime.getHour() > 17 // 6 P.M.
                                          || ElapTime.getHour() < 6; // 6 A.M.
    
    @Unique
    private static ResourceLocation background = nightMode ?
            ResourceLocation.fromNamespaceAndPath("unlegit", "night.png") :
            ResourceLocation.fromNamespaceAndPath("unlegit", "day.png");
    
    @Unique
    private boolean flag = true;
    
    @Shadow
    public int width, height;
    @Shadow protected Minecraft minecraft;
    
    @Inject(method = "renderPanorama", at = @At(value = "HEAD"), cancellable = true)
    public void renderPanorama(GuiGraphics graphics, float f, CallbackInfo info)
    {
        if ("Fancy".equals(UnLegit.THEME))
        {
            PoseStack poseStack = graphics.pose();
            float scale = (float) minecraft.getWindow().getGuiScale();
            
            poseStack.pushPose();
            poseStack.last().pose().scale(1 / scale);
            
            if (flag) { background = get(background); flag = false; }
            int bH, bW, mouseX = (int) minecraft.mouseHandler.xpos();
            
            if (nightMode)
            {
                bH = (int) (height * scale);
                bW = (int) Math.max(bH * 1.77F, width * scale);
            }
            
            else
            {
                bH = (int) (height * scale);
                bW = (int) Math.max(bH * 3.21F, width * scale);
            }
            
            GlStateManager._enableBlend();
            graphics.blit(RenderType::guiTextured, background, nightMode ? 0 : (-mouseX / 3), 0, bW, bH, bW, bH, bW, bH);
            poseStack.popPose();
            
            boolean loading = minecraft.screen instanceof ServerReconfigScreen || minecraft.screen instanceof ReceivingLevelScreen;
            graphics.fill(0, 0, width, height, Colorer.RGB(0, 0, 0, loading ? 150 : 50));
            info.cancel();
        }
    }
    
    @Inject(method = "init", at = @At(value = "HEAD"))
    public void resetTexture(CallbackInfo info) { flag = true; }
}
