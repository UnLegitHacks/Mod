package io.unlegit.gui.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class GlyphPage implements IMinecraft
{
    private HashMap<Character, Glyph> glyphCharacterMap = new HashMap<>();
    public int imageSize, maxFontHeight = -1;
    private AbstractTexture loadedTexture;
    private BufferedImage bufferedImage;
    private Font font;
    
    public GlyphPage(Font font)
    {
        this.font = font;
    }
    
    public void generateGlyphPage(char[] chars)
    {
        double maxWidth = -1, maxHeight = -1;
        AffineTransform affineTransform = new AffineTransform();
        FontRenderContext fontRenderContext = new FontRenderContext(affineTransform, true, true);
        
        for (char character : chars)
        {
            Rectangle2D bounds = font.getStringBounds(Character.toString(character), fontRenderContext);
            if (maxWidth < bounds.getWidth()) maxWidth = bounds.getWidth();
            if (maxHeight < bounds.getHeight()) maxHeight = bounds.getHeight();
        }
        
        maxWidth += 2; maxHeight += 2;
        // Magic code
        imageSize = (int) Math.ceil(Math.max(Math.ceil(Math.sqrt(maxWidth * maxWidth * chars.length) / maxWidth), Math.ceil(Math.sqrt(maxHeight * maxHeight * chars.length) / maxHeight)) * Math.max(maxWidth, maxHeight)) + 1;
        bufferedImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.setFont(font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, imageSize, imageSize); // Sets the image background to transparent
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int currentCharHeight = 0, posX = 0, posY = 0;
        
        for (char character : chars)
        {
            String strChar = Character.toString(character);
            Rectangle2D bounds = fontMetrics.getStringBounds(strChar, graphics);
            Glyph glyph = new Glyph(); glyph.width = bounds.getBounds().width + 8; glyph.height = bounds.getBounds().height;
            
            if (posX + glyph.width >= imageSize)
            {
                posX = 0;
                posY += currentCharHeight;
                currentCharHeight = 0;
            }
            
            glyph.x = posX;
            glyph.y = posY;
            if (glyph.height > maxFontHeight) maxFontHeight = glyph.height;
            if (glyph.height > currentCharHeight) currentCharHeight = glyph.height;
            graphics.drawString(strChar, posX + 2, posY + fontMetrics.getAscent());
            glyphCharacterMap.put(character, glyph);
            posX += glyph.width;
        }
    }
    
    public void setupTexture()
    {
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            byte[] bytes = outputStream.toByteArray();
            ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();
            loadedTexture = new DynamicTexture(NativeImage.read(data));
        } catch (Exception e) {}
    }
    
    public float drawChar(GuiGraphics guiGraphics, char character, float x, float y, float red, float blue, float green, float alpha)
    {
        Glyph glyph = glyphCharacterMap.get(character); if (glyph == null) return 0;
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        float pageX = glyph.x / (float) imageSize, pageY = glyph.y / (float) imageSize,
              pageWidth = glyph.width / (float) imageSize, pageHeight = glyph.height / (float) imageSize,
              width = glyph.width, height = glyph.height;
        
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        GlStateManager._enableBlend();
        bufferBuilder.addVertex(matrix4f, x, y + height, 0).setColor(red, green, blue, alpha).setUv(pageX, pageY + pageHeight);
        bufferBuilder.addVertex(matrix4f, x + width, y + height, 0).setColor(red, green, blue, alpha).setUv(pageX + pageWidth, pageY + pageHeight);
        bufferBuilder.addVertex(matrix4f, x + width, y, 0).setColor(red, green, blue, alpha).setUv(pageX + pageWidth, pageY);
        bufferBuilder.addVertex(matrix4f, x, y, 0).setColor(red, green, blue, alpha).setUv(pageX, pageY);
        BufferUploader.drawWithShader(bufferBuilder.build());
        return width - 8;
    }
    
    public float getWidth(char ch) { return glyphCharacterMap.get(ch).width; }
    public void bind() { RenderSystem.setShaderTexture(0, loadedTexture.getId()); }
    static class Glyph { public int x, y, width, height; }
}