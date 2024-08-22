package io.unlegit.modules.impl.gui.keystrokes;

import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;

import io.unlegit.gui.font.IFont;
import io.unlegit.utils.render.Animation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;

public class RenderClick extends RenderKey
{
    public RenderClick(KeyMapping keyMapping, int x, int y)
    {
        super(keyMapping, x, y);
    }
    
    public void render(GuiGraphics graphics, float partialTicks)
    {
        boolean keyPressed = keyMapping.isDown();
        updateAnimation(keyPressed);
        int x = this.x + 7, y = this.y + 5;
        Color color;
        
        if (animation != null)
            color = new Color(animation.wrap(96), animation.wrap(96), animation.wrap(96), 96 + animation.wrap(32));
        else color = new Color(0, 0, 0, 96);
        
        graphics.fill(x, y, x + 37, y + 24, color.getRGB());
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        IFont.NORMAL.drawCenteredString(graphics, keyMapping.getTranslatedKeyMessage().getString().substring(0, 1), x + 17, y + 6, Color.WHITE);
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
