package io.unlegit.modules.impl.gui;

import java.util.ArrayList;

import com.mojang.blaze3d.platform.GlStateManager;

import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.gui.font.IFont;
import io.unlegit.gui.font.impl.FontRenderer;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

@IModule(name = "Compass", description = "A simplistic compass that shows directions.")
public class Compass extends ModuleU implements IGui
{
    public static final int CARDINALS = 0, ORDINALS = 1, NUMBERS = 2;
    private ArrayList<String> directions = new ArrayList<>();
    private ResourceLocation shadow;
    private FontRenderer font;
    
    public Compass()
    {
        add("S", "E", "N", "W", "_",
            
            "SE", "NE", "NW", "SW", "_",
            
            "165", "150", "120", "105",
            "75", "60", "30", "15",
            "345", "330", "300", "285",
            "255", "240", "210", "195");
    }
    
    public void onGuiRender(GuiRenderE e)
    {
        float rotation = Mth.wrapDegrees(mc.player.getYRot()) / 360F;
        int offset = 0, count = 0, type = 0, prevType = 0;
        int width = mc.getWindow().getGuiScaledWidth();
        GuiGraphics graphics = e.graphics;
        renderShadow(graphics, width);
        switchFont(IFont.LARGE);
        
        for (String direction : directions)
        {
            if (direction.equals("_")) { type++; continue; } // Separator
            
            if (type == CARDINALS)
            {
                drawString(graphics, width, direction, offset + (int) ((width / 2) + (rotation * 640)), 10, 1);
                offset += 160;
            }
            
            else if (type == ORDINALS)
            {
                if (prevType != type)
                {
                    switchFont(IFont.MEDIUM); offset = 80;
                }
                
                drawString(graphics, width, direction, offset + (int) ((width / 2) + (rotation * 640)), 13, 0.9F);
                offset += 160;
            }
            
            else if (type == NUMBERS)
            {
                if (prevType != type)
                {
                    switchFont(IFont.NORMAL); offset = 26;
                }
                
                drawString(graphics, width, direction, offset + (int) ((width / 2) + (rotation * 640)), 17, 0.8F);
                if (++count % 4 == 0) offset += 53;
                else if (count % 2 == 0) offset += 57;
                else offset += 25;
            }
            
            prevType = type;
        }
        
        GlStateManager._disableBlend();
        e.graphics.fill(0, 0, 0, 0, 0);
    }
    
    public int getColor(int width, float x, float alphaMultiplier)
    {
        float alpha = 255 - Math.abs((width / 2) - x) * 1.75F;
        if (alpha < 0) alpha = 0; if (alpha > 255) alpha = 255;
        return Colorer.RGB(255, 255, 255, (int) (alpha * alphaMultiplier));
    }
    
    public void drawString(GuiGraphics graphics, int width, String direction, int x, int y, float alphaMultiplier)
    {
        font.drawCenteredString(graphics, direction, x - 640, y, getColor(width, x - 640, alphaMultiplier));
        font.drawCenteredString(graphics, direction, x, y, getColor(width, x, alphaMultiplier));
        font.drawCenteredString(graphics, direction, x + 640, y, getColor(width, x + 640, alphaMultiplier));
    }
    
    public void add(String... degrees)
    {
        for (String degree : degrees) directions.add(degree);
    }
    
    public void renderShadow(GuiGraphics graphics, int width)
    {
        if (shadow == null) shadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "shadow.png"));
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        graphics.setColor(1, 1, 1, 1);
        drawShadow(graphics, shadow, (width / 2) - 131, 5, 261, 37, 261, 37, 261, 37);
        GlStateManager._disableBlend();
    }
    
    public void switchFont(FontRenderer font) { this.font = font; }
}
