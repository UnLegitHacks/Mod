package io.unlegit.utils.render;

import io.unlegit.interfaces.IMinecraft;
import io.unlegit.mixins.render.AccGameRender;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

public class ScreenUtil implements IMinecraft
{
    /**
     * Makes the texture use linear scaling when scaled up, instead of nearest scaling.
     */
    public static ResourceLocation withLinearScaling(ResourceLocation location)
    {
        TextureManager textureManager = mc.getTextureManager();
        textureManager.getTexture(location).setFilter(true, false);
        return location;
    }
    
    /**
     * Whenever you want to draw a shadow, use this method.
     * It makes it so that it doesn't render a shadow when
     * the theme is vanilla  automatically for you.
     */
    public static void drawShadow(GuiGraphics graphics, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n)
    {
        graphics.blit(resourceLocation, i, j, f, g, k, l, m, n);
    }
    
    public static void blur(float factor, float partialTicks)
    {
        PostChain blurEffect = ((AccGameRender) mc.gameRenderer).getBlurEffect();
        float blurriness = mc.options.getMenuBackgroundBlurriness();

        if (blurEffect != null && blurriness >= 1)
        {
            blurEffect.setUniform("Radius", blurriness * factor);
            blurEffect.process(partialTicks);
            mc.getMainRenderTarget().bindWrite(false);
        }
    }
    
    public static boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
}
