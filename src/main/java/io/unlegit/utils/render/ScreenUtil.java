package io.unlegit.utils.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.mixins.gui.AccGameRender;
import io.unlegit.mixins.gui.AccGraphics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.glfwGetKeyName;

public class ScreenUtil implements IMinecraft
{
    private static final ResourceLocation BLUR_POST_CHAIN_ID = ResourceLocation.withDefaultNamespace("blur");

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
    public static void drawShadow(GuiGraphics graphics, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n, int tint)
    {
        if ("Vanilla".equals(UnLegit.THEME)) return;
        graphics.blit(RenderType::guiTextured, resourceLocation, i, j, f, g, k, l, k, l, m, n, tint);
    }

    public static void drawShadow(GuiGraphics graphics, ResourceLocation resourceLocation, int i, int j, float f, float g, int k, int l, int m, int n)
    {
        drawShadow(graphics, resourceLocation, i, j, f, g, k, l, m, n, -1);
    }

    public static void blur(float factor, float partialTicks)
    {
        PostChain blurEffect = mc.getShaderManager().getPostChain(BLUR_POST_CHAIN_ID, LevelTargetBundle.MAIN_TARGETS);
        float blurriness = getBlurriness();
        
        if (blurEffect != null && blurriness >= 1)
        {
            blurEffect.setUniform("Radius", blurriness * factor);
            blurEffect.process(mc.getMainRenderTarget(), ((AccGameRender) mc.gameRenderer).getResourcePool());
            mc.getMainRenderTarget().bindWrite(false);
        }
    }
    
    public static int getBlurriness()
    {
        if ("Fancy".equals(UnLegit.THEME))
            return mc.options.getMenuBackgroundBlurriness();
        else return 0;
    }
    
    public static boolean mouseOver(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX > x && mouseX < width && mouseY > y && mouseY < height;
    }
    
    public static int getKey(String keyName)
    {
        for (int i = 39; i < 97; i++)
        {
            if (keyName.equalsIgnoreCase(glfwGetKeyName(i, 0))) return i;
        } return -1;
    }
    
    public static void horzGradient(GuiGraphics graphics, int i, int j, int k, int l, int n, int o)
    {
        VertexConsumer vertexConsumer = ((AccGraphics) graphics).getBufferSource().getBuffer(RenderType.gui());
        Matrix4f matrix4f = graphics.pose().last().pose();
        vertexConsumer.addVertex(matrix4f, i, j, 0).setColor(n);
        vertexConsumer.addVertex(matrix4f, i, l, 0).setColor(n);
        vertexConsumer.addVertex(matrix4f, k, l, 0).setColor(o);
        vertexConsumer.addVertex(matrix4f, k, j, 0).setColor(o);
        graphics.flush();
    }
}
