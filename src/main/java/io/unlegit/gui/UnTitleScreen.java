package io.unlegit.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

// TODO: Implement this screen.
public class UnTitleScreen extends Screen
{
    public UnTitleScreen()
    {
        super(Component.translatable("narrator.screen.title"));
    }
    
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
    
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks)
    {
        ResourceLocation background = ResourceLocation.fromNamespaceAndPath("unlegit", "background.png");
        int bW = 853, bH = 398;
        
        // If they're too small, make them bigger while maintaining the aspect ratio.
        while (bW < width || bH < height) { bW++; bH++; } 
        
        GlStateManager._enableBlend();
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.blit(background, -mouseX / 6, 0, bW, bH, bW, bH, bW, bH);
    }
}
