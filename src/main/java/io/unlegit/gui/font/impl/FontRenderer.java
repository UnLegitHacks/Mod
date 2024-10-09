package io.unlegit.gui.font.impl;

import java.awt.Color;
import java.awt.Font;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;

import io.unlegit.gui.font.GlyphPage;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IMinecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class FontRenderer implements IMinecraft, IGui
{
    private float prevScaleFactor, posX, posY;
    private ResourceLocation shadow;
    private GlyphPage page;
    private String path;
    protected int size;
    
    public static FontRenderer fancy(String path, int size)
    {
        GlyphPage page = init(path, size);
        FontRenderer result = new FontRenderer(page);
        result.path = path;
        result.size = size;
        return result;
    }
    
    public static VanillaFontRenderer vanilla(int size)
    {
        VanillaFontRenderer result = new VanillaFontRenderer(null);
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
        float red = color.getRed() / 255F, green = color.getGreen() / 255F, blue = color.getBlue() / 255F, alpha = color.getAlpha() / 255F,
              scaleFactor = checkGuiScaleUpdate();
        
        if (alpha == 0 || text.isEmpty()) return;
        
        PoseStack pose = graphics.pose();
        pose.pushPose(); pose.last().pose().scale(1 / scaleFactor);
        
        float posX = this.posX * scaleFactor, posY = this.posY * scaleFactor;
        GlStateManager._enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        page.bind();
        
        for (int i = 0; i < text.length(); ++i)
        {
            char character = text.charAt(i);
            posX += page.drawChar(builder, graphics, character, posX, posY, red, green, blue, alpha);
        }
        
        BufferUploader.drawWithShader(builder.build());
        pose.popPose();
    }
    
    public int getStringWidth(String text)
    {
        if (text == null) return 0;
        int width = 0, size = text.length();
        float scaleFactor = checkGuiScaleUpdate();
        
        for (int i = 0; i < size; i++)
        {
            char character = text.charAt(i);
            width += page.getWidth(character) - 8;
        }
        
        return (int) (width / scaleFactor);
    }
    
    public int drawString(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        return renderString(graphics, text, x, y, color);
    }
    
    public int drawStringWithShadow(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        if (shadow == null) shadow = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "shadow.png"));
        int width = getStringWidth(text) + 2, height = size + 2;
        x -= 5; width += 10; y -= 3; height += 6;
        
        GlStateManager._enableBlend();
        graphics.setColor(1, 1, 1, color.getAlpha() / 255F);
        drawShadow(graphics, shadow, x, y, 40, height, 10, height, 20, height);
        graphics.blit(shadow, x + 10, y, width - 20, height, 60, height, 1, height, 40, height);
        drawShadow(graphics, shadow, x + width - 10, y, 30, height, 10, height, 20, height);
        graphics.setColor(1, 1, 1, 1);
        x += 5; y += 3;
        
        return renderString(graphics, text, x, y, color);
    }
    
    public int drawCenteredString(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        return drawString(graphics, text, x - getStringWidth(text) / 2, y, color);
    }
    
    public int drawCenteredStringWithShadow(GuiGraphics graphics, String text, int x, int y, Color color)
    {
        return drawStringWithShadow(graphics, text, x - getStringWidth(text) / 2, y, color);
    }
    
    public float checkGuiScaleUpdate()
    {
        float scaleFactor = (float) mc.getWindow().getGuiScale();
        
        if (prevScaleFactor != scaleFactor)
        {
            page = init(path, (int) (size * scaleFactor));
            prevScaleFactor = scaleFactor;
        } return scaleFactor;
    }
    
    public FontRenderer(GlyphPage page) { this.page = page; }
}