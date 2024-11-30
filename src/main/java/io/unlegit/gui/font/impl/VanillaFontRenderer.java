package io.unlegit.gui.font.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import io.unlegit.gui.font.GlyphPage;
import io.unlegit.mixins.gui.AccGraphics;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.GuiGraphics;

public class VanillaFontRenderer extends FontRenderer
{
    public VanillaFontRenderer(GlyphPage page)
    {
        super(page);
    }
    
    public int drawString(GuiGraphics graphics, String text, int x, int y, int color)
    {
        int[] values = Colorer.extract(color);
        int red = values[0], green = values[1], blue = values[2];
        return renderString(graphics, text, x, y, color, red != 0 && green != 0 && blue != 0);
    }
    
    public int drawStringWithShadow(GuiGraphics graphics, String text, int x, int y, int color)
    {
        return renderString(graphics, text, x, y, color, true);
    }
    
    public int drawCenteredString(GuiGraphics graphics, String text, int x, int y, int color)
    {
        return drawString(graphics, text, x - getStringWidth(text) / 2, y, color);
    }
    
    public int drawCenteredStringWithShadow(GuiGraphics graphics, String text, int x, int y, int color)
    {
        return drawStringWithShadow(graphics, text, x - getStringWidth(text) / 2, y, color);
    }
    
    public int renderString(GuiGraphics graphics, String text, int x, int y, int color, boolean shadow)
    {
        if (Colorer.extract(color)[3] /* alpha */ < 12 || text.isEmpty()) return 0;

        float scale = size / 10F;
        PoseStack pose = graphics.pose();

        pose.pushPose();
        pose.scale(scale, scale, scale);
        x = (int) (x / scale); y = (int) (y / scale);
        y += 2; x += 1;

        int result = mc.font.drawInBatch(text, x, y, color, shadow, pose.last().pose(), ((AccGraphics) graphics).getBufferSource(), DisplayMode.SEE_THROUGH, 1, 1);
        pose.popPose();

        return result;
    }

    public float checkGuiScaleUpdate()
    {
        return (float) mc.getWindow().getGuiScale();
    }
    
    public int getStringWidth(String text)
    {
        return mc.font.width(text) * (int) (size / 10F);
    }
}
