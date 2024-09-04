package io.unlegit.gui.font.impl;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.gui.font.GlyphPage;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.GuiGraphics;

public class VanillaFontRenderer extends FontRenderer
{
    public VanillaFontRenderer(GlyphPage page)
    {
        super(page);
    }
    
    public int drawString(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        return renderString(graphics, text, x, y, color, false);
    }
    
    public int drawStringWithShadow(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        return renderString(graphics, text, x, y, color, true);
    }
    
    public int drawCenteredString(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        return drawString(graphics, text, x - getStringWidth(text) / 2, y, color);
    }
    
    public int drawCenteredStringWithShadow(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        return drawStringWithShadow(graphics, text, x - getStringWidth(text) / 2, y, color);
    }
    
    private int renderString(GuiGraphics graphics, String text, int x, int y, Color color, boolean shadow)
    {
        float scale = size / 10F;
        PoseStack pose = graphics.pose();
        
        pose.pushPose();
        pose.scale(scale, scale, scale);
        x /= scale; y /= scale;
        y += 2; x += 1;
        
        int colorRGB = new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(color.getAlpha(), 4)).getRGB(),
            result = mc.font.drawInBatch(text, x, y, colorRGB, shadow, pose.last().pose(), graphics.bufferSource(), DisplayMode.SEE_THROUGH, 1, 1);
        
        pose.popPose();
        return result;
    }
    
    public int getStringWidth(String text)
    {
        return mc.font.width(text) * (int) (size / 10F);
    }
}
