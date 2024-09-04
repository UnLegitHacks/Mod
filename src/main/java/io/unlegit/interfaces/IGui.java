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
    default ResourceLocation withLinearScaling(ResourceLocation location) { return ScreenUtil.withLinearScaling(location); }
    default boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height) { return ScreenUtil.mouseOver(mouseX, mouseY, x, y, width, height); }
    default void blur(float factor, float partialTicks) { ScreenUtil.blur(factor, partialTicks); }
    default void drawShadow(GuiGraphics graphics, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n) { ScreenUtil.drawShadow(graphics, resourceLocation, i, j, f, g, k, l, m, n); }
    default float getBlurriness() { return ScreenUtil.getBlurriness(); }
}
