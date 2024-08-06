package io.unlegit.mixins;

import java.awt.Color;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;

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
        if (flag) { background = withLinearScaling(background); flag = false; }
        int bW = 1280, bH = 398, mouseX = (int) (minecraft.mouseHandler.xpos() / minecraft.getWindow().getGuiScale());
        
        // If they're too small, make them bigger while maintaining the aspect ratio.
        while (bW < width || bH < height) { bW *= 1.25F; bH *= 1.25F; } 
        
        GlStateManager._enableBlend();
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.blit(background, -mouseX / 6, 0, bW, bH, bW, bH, bW, bH);
        guiGraphics.fill(0, 0, width, height, new Color(0, 0, 0, 50).getRGB());
    }
    
    @Inject(method = "init", at = @At(value = "HEAD"))
    public void resetTexture(CallbackInfo info) { flag = true; }
}
