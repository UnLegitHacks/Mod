package io.unlegit.modules.impl.gui.keystrokes;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;

import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.render.Animation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class RenderKey implements IGui
{
    protected ResourceLocation circle = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "modules/circle.png"));
    protected boolean prevPressed = false;
    protected Animation animation = null;
    protected KeyMapping keyMapping;
    protected KeyStrokes parent;
    protected int x, y;
    
    // X & Y are relative
    public RenderKey(KeyStrokes parent, KeyMapping keyMapping, int x, int y)
    {
        this.keyMapping = keyMapping;
        this.x = x; this.y = y;
        this.parent = parent;
    }
    
    public void render(GuiGraphics graphics, float partialTicks)
    {
        boolean keyPressed = keyMapping.isDown();
        updateAnimation(keyPressed);
        int x = this.x + 7, y = this.y + 5;
        float alpha = animation != null ? animation.get() : 0;
        graphics.fill(x, y, x + 24, y + 24, new Color(0, 0, 0, 96).getRGB());
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        GlStateManager._enableBlend();
        graphics.setColor(1, 1, 1, 1);
        graphics.blit(parent.keyShadow, x - 8, y - 8, 40, 40, 40, 40, 40, 40);
        
        if (alpha != 0)
        {
            int size = animation.wrap(36);
            graphics.enableScissor(x, y, x + 24, y + 24);
            graphics.setColor(1, 1, 1, animation.get() / 4);
            graphics.blit(circle, x - (size - 24) / 2, y - (size - 24) / 2, size, size, size, size, size, size);
            graphics.disableScissor();
        }
        
        graphics.setColor(1, 1, 1, 1);
        IFont.NORMAL.drawCenteredString(graphics, keyMapping.getTranslatedKeyMessage().getString(), x + 11, y + 6, Color.WHITE);
        prevPressed = keyPressed;
        GlStateManager._disableBlend();
    }
    
    public void updateAnimation(boolean flag)
    {
        if (flag)
        {
            if (animation == null || !prevPressed) animation = new Animation(128);
        }
        
        else if (prevPressed)
        {
            animation = new Animation(128);
            animation.reverse = true;
        }
    }
}
