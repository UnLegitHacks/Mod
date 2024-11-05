package io.unlegit.gui.font.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.*;
import io.unlegit.gui.font.GlyphPage;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class FontRenderer implements IMinecraft, IGui
{
    private final ResourceLocation shadowLeft, shadowCenter, shadowRight;
    private float prevScaleFactor, posX, posY;
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
    
    private int renderString(GuiGraphics graphics, String text, int x, int y, int color)
    {
        if (text == null) return 0;
        else
        {
            posX = x; posY = y;
            renderStringAtPos(graphics, text, color);
            return (int) posX / 2;
        }
    }
    
    private void renderStringAtPos(GuiGraphics graphics, String text, int color)
    {
        int[] values = Colorer.extract(color);
        float red = values[0] / 255F, green = values[1] / 255F, blue = values[2] / 255F, alpha = values[3] / 255F,
              scaleFactor = checkGuiScaleUpdate();
        
        if (alpha == 0 || text.isEmpty()) return;
        
        PoseStack pose = graphics.pose();
        pose.pushPose(); pose.last().pose().scale(1 / scaleFactor);
        
        float posX = this.posX * scaleFactor, posY = this.posY * scaleFactor;
        // RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        /* Workarounds */
        graphics.blit(RenderType::guiTextured, shadowCenter, 0, 0, 0, 0, 0, 0, 0, 0);
        graphics.fill(0, 0, 0, 0, 0);
        GlStateManager._enableBlend();

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
            width += (int) (page.getWidth(character) - 8);
        }
        
        return (int) (width / scaleFactor);
    }
    
    public int drawString(GuiGraphics graphics, String text, int x, int y, int color)
    {
        return renderString(graphics, text, x, y, color);
    }
    
    public int drawStringWithShadow(GuiGraphics graphics, String text, int x, int y, int color)
    {
        int width = getStringWidth(text) + 2, height = size + 2;
        x -= 5; width += 10; y -= 3; height += 6;
        
        GlStateManager._enableBlend();
        drawShadow(graphics, shadowLeft, x, y, 10, height, 10, height, 10, height, Colorer.RGB(1, 1, 1, Colorer.extract(color)[3]));
        drawShadow(graphics, shadowCenter, x + 10, y, width - 20, height, width - 20, height, width - 20, height, Colorer.RGB(1, 1, 1, Colorer.extract(color)[3]));
        drawShadow(graphics, shadowRight, x + (width - 10), y, 10, height, 10, height, 10, height, Colorer.RGB(1, 1, 1, Colorer.extract(color)[3]));
        x += 5; y += 3;

        return renderString(graphics, text, x, y, color);
    }
    
    public int drawCenteredString(GuiGraphics graphics, String text, int x, int y, int color)
    {
        return drawString(graphics, text, x - getStringWidth(text) / 2, y, color);
    }
    
    public int drawCenteredStringWithShadow(GuiGraphics graphics, String text, int x, int y, int color)
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
    
    public FontRenderer(GlyphPage page)
    {
        this.page = page;
        shadowLeft = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "shadow/left.png"));
        shadowCenter = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "shadow/center.png"));
        shadowRight = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "shadow/right.png"));
    }
}