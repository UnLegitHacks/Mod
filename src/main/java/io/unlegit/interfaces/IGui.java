package io.unlegit.interfaces;

import io.unlegit.utils.render.ScreenUtil;
import net.minecraft.resources.ResourceLocation;

/**
 * Basic methods which are useful in screens.
 * You can think of it as a header file (LOL) for ScreenUtil.
 */
public interface IGui
{
    default ResourceLocation withLinearScaling(ResourceLocation location) { return ScreenUtil.withLinearScaling(location); }
    default boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height) { return ScreenUtil.mouseOver(mouseX, mouseY, x, y, width, height); }
}
