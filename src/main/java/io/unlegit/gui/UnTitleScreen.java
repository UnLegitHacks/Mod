package io.unlegit.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UnTitleScreen extends Screen
{
    public UnTitleScreen()
    {
        super(Component.translatable("narrator.screen.title"));
    }
    
    public void render(GuiGraphics guiGraphics, int width, int height, float partialTicks)
    {
        this.renderPanorama(guiGraphics, partialTicks);
    }
    
    public void renderBackground(GuiGraphics guiGraphics, int width, int height, float partialTicks) {}
}
