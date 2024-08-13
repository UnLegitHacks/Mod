package io.unlegit.utils.render;

import io.unlegit.interfaces.IMinecraft;
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
    
    public static boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
}
