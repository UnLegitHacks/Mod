package io.unlegit.utils.render;

import io.unlegit.interfaces.IMinecraft;
import io.unlegit.mixins.render.AccGameRender;
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
    
    public static void blur(float factor, float partialTicks)
    {
        PostChain blurEffect = ((AccGameRender) mc.gameRenderer).getBlurEffect();
        float blurriness = mc.options.getMenuBackgroundBlurriness();

        if (blurEffect != null && blurriness >= 1)
        {
            blurEffect.setUniform("Radius", blurriness * factor);
            blurEffect.process(partialTicks);
        }
        
        mc.getMainRenderTarget().bindWrite(false);
    }
    
    public static boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
}
