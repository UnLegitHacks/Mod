package io.unlegit.modules.impl.gui.keystrokes;

import com.mojang.blaze3d.platform.GlStateManager;
import io.unlegit.gui.font.IFont;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;

import java.awt.*;

public class RenderClick extends RenderKey
{
    public RenderClick(KeyStrokes parent, KeyMapping keyMapping, int x, int y)
    {
        super(parent, keyMapping, x, y);
    }
    
    public void render(GuiGraphics graphics, float partialTicks)
    {
        boolean keyPressed = keyMapping.isDown();
        updateAnimation(keyPressed);
        int x = this.x + 7, y = this.y + 5;
        float alpha = animation != null ? animation.get() : 0;
        renderBlur(graphics, 0, partialTicks);
        graphics.fill(x, y, x + 37, y + 24, new Color(0, 0, 0, getAlpha()).getRGB());
        GlStateManager._enableBlend();
        GlStateManager._blendFuncSeparate(770, 771, 1, 1);
        drawShadow(graphics, parent.clickShadow, x - 8, y - 8, 53, 40, 53, 40, 53, 40);
        
        if (alpha != 0)
        {
            int size = animation.wrap(45);
            graphics.enableScissor(x, y, x + 37, y + 24);
            graphics.blit(RenderType::guiTextured, circle, x - (size - 37) / 2, y - (size - 24) / 2, size, size, size, size, size, size, size, size, Colorer.RGB(1, 1, 1, animation.get() / 4));
            graphics.disableScissor();
        }

        IFont.NORMAL.drawCenteredString(graphics, keyMapping.getTranslatedKeyMessage().getString().substring(0, 1), x + 17, y + 6, -1);
        prevPressed = keyPressed;
        GlStateManager._disableBlend();
    }

    public void renderBlur(GuiGraphics graphics, int sizeOffset, float partialTicks)
    {
        int x = this.x + 7, y = this.y + 5;
        graphics.enableScissor(x - sizeOffset, y - sizeOffset, x + 37 + sizeOffset, y + 24 + sizeOffset);
        blur(2, partialTicks);
        graphics.disableScissor();
    }
}
