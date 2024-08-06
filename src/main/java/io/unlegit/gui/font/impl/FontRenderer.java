package io.unlegit.gui.font.impl;

import java.awt.Color;
import java.awt.Font;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import io.unlegit.gui.font.GlyphPage;
import io.unlegit.interfaces.IMinecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;

public class FontRenderer implements IMinecraft
{
    private float prevScaleFactor, posX, posY;
    private GlyphPage page;
    private String path;
    private int size;
    
    public static FontRenderer create(String path, int size)
    {
        GlyphPage page = init(path, size);
        FontRenderer result = new FontRenderer(page);
        result.path = path;
        result.size = size;
        return result;
    }
    
    private static GlyphPage init(String path, int size)
    {
        char[] characters = new char[256];
        for (int i = 0; i < characters.length; i++) characters[i] = (char) i;
        Font font = null;
        
        try
        {
            font = Font.createFont(Font.TRUETYPE_FONT, FontRenderer.class.getClassLoader().getResourceAsStream(path)).deriveFont(Font.PLAIN, size);
        } catch (Exception e) {}
        
        GlyphPage page = new GlyphPage(font);
        page.generateGlyphPage(characters);
        page.setupTexture();
        return page;
    }
    
    public int drawString(GuiGraphics guiGraphics, String text, int x, int y, Color color)
    {
        return renderString(guiGraphics, text, x, y, color);
    }
    
    public int drawCenteredString(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        return drawString(graphics, text, x - getStringWidth(text) / 2, y, color);
    }
    
    private int renderString(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        if (text == null) return 0;
        else
        {
            posX = x; posY = y;
            renderStringAtPos(graphics, text, color);
            return (int) posX / 2;
        }
    }
    
    private void renderStringAtPos(GuiGraphics graphics, String text, Color color)
    {
        float scaleFactor = (float) mc.getWindow().getGuiScale();
        
        if (prevScaleFactor != scaleFactor)
        {
            page = init(path, (int) (size * scaleFactor));
            prevScaleFactor = scaleFactor;
        }
        
        float red = color.getRed() / 255F, green = color.getGreen() / 255F, blue = color.getBlue() / 255F, alpha = color.getAlpha() / 255F;
        if (alpha == 0) return;
        PoseStack pose = graphics.pose();
        pose.pushPose(); pose.last().pose().scale(1 / scaleFactor);
        float posX = this.posX * scaleFactor, posY = this.posY * scaleFactor;
        GlStateManager._enableBlend();
        RenderSystem.setShader(() -> GameRenderer.getPositionTexColorShader());
        page.bind();
        
        for (int i = 0; i < text.length(); ++i)
        {
            char character = text.charAt(i);
            posX += page.drawChar(graphics, character, posX, posY, red, green, blue, alpha);
        }
        
        pose.popPose();
    }
    
    public int getStringWidth(String text)
    {
        if (text == null) return 0;
        int width = 0, size = text.length();
        float scaleFactor = (float) mc.getWindow().getGuiScale();
        
        for (int i = 0; i < size; i++)
        {
            char character = text.charAt(i);
            width += page.getWidth(character) - 8;
        }
        
        return (int) (width / scaleFactor);
    }
    
    public FontRenderer(GlyphPage page) { this.page = page; }
}