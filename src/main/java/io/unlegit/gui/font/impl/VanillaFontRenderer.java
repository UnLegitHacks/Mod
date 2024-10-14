package io.unlegit.gui.font.impl;

import java.awt.Color;

import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.gui.font.GlyphPage;
import io.unlegit.utils.render.Colorer;
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
        int red = color.getRed(), blue = color.getBlue(), green = color.getGreen();
        return renderString(graphics, text, x, y, color, red != 0 && green != 0 && blue != 0);
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
    
    public int renderString(GuiGraphics graphics, String text, int x, int y, Color color, boolean shadow)
    {
        float scale = size / 10F;
        PoseStack pose = graphics.pose();
        
        pose.pushPose();
        pose.scale(scale, scale, scale);
        x /= scale; y /= scale;
        y += 2; x += 1;
        
        int colorRGB = Colorer.RGB(color.getRed(), color.getGreen(), color.getBlue(), Math.max(color.getAlpha(), 4)),
            result = mc.font.drawInBatch(text, x, y, colorRGB, shadow, pose.last().pose(), graphics.bufferSource(), DisplayMode.SEE_THROUGH, 1, 1);
        
        pose.popPose();
        return result;
    }
    
    public int getStringWidth(String text)
    {
        return mc.font.width(text) * (int) (size / 10F);
    }
}
