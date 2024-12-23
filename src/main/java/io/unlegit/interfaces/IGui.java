package io.unlegit.interfaces;

import io.unlegit.utils.render.ScreenUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Basic methods which are useful in screens.
 * You can think of it as a header file for ScreenUtil. (lol)
 */
public interface IGui
{
    default ResourceLocation get(ResourceLocation location) { return ScreenUtil.get(location); }
    default boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height) { return ScreenUtil.mouseOver(mouseX, mouseY, x, y, width, height); }
    default void blur(float factor, float partialTicks) { ScreenUtil.blur(factor, partialTicks); }
    default void drawShadow(GuiGraphics graphics, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) { ScreenUtil.drawShadow(graphics, resourceLocation, i, j, f, g, k, l, m, n); }
    default void drawShadow(GuiGraphics graphics, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, int tint) { ScreenUtil.drawShadow(graphics, resourceLocation, i, j, f, g, k, l, m, n, tint); }
    default int getBlurriness() { return ScreenUtil.getBlurriness(); }
    default void horzGradient(GuiGraphics graphics, int i, int j, int k, int l, int n, int o) { ScreenUtil.horzGradient(graphics, i, j, k, l, n, o); }
}
