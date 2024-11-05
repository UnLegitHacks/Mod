package io.unlegit.gui.buttons;

import com.mojang.blaze3d.platform.GlStateManager;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.SoundUtil;
import io.unlegit.utils.render.Animation;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.gui.GuiGraphics;

import java.awt.*;

public class UnPlainTextButton implements IGui
{
    private Animation hoverAnimation = null;
    public boolean special = false;
    private final Runnable action;
    private boolean flag = false;
    private final UnStyle style;
    private final String name;
    private final int x, y;
    
    public UnPlainTextButton(String name, int x, int y, UnStyle style, Runnable action)
    {
        this.name = name;
        this.style = style;
        this.action = action;
        this.x = x; this.y = y;
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        if (mouseOver(mouseX, mouseY, x, y, x + IFont.NORMAL.getStringWidth(name), y + 13))
        {
            if (!flag)
            {
                hoverAnimation = new Animation(160);
                flag = true;
            }
        }
        
        else if (hoverAnimation != null)
        {
            if (flag)
            {
                hoverAnimation = new Animation(160); hoverAnimation.reverse = true;
                flag = false;
            } if (hoverAnimation.finished()) hoverAnimation = null;
        }
        
        int width = IFont.NORMAL.getStringWidth(name);
        GlStateManager._enableBlend();
        
        if (style == UnStyle.UNDERLINE)
        {
            if (special)
                IFont.MEDIUM.drawStringWithShadow(graphics, name, x - 1, y, Color.YELLOW.getRGB());
            else
                IFont.NORMAL.drawStringWithShadow(graphics, name, x - 1, y, -1);
            
            if (hoverAnimation == null) return;
            
            graphics.fill(x + (int) (24 * (1 - hoverAnimation.get())), y + 11, (x + width) - (int) (24 * (1 - hoverAnimation.get())), y + 12, new Color(255, 255, 255, hoverAnimation.wrap(160)).getRGB());
        }
        
        else
        {
            int alpha = 200 + (hoverAnimation == null ? 0 : hoverAnimation.wrap(55));
            IFont.NORMAL.drawStringWithShadow(graphics, name, x - 1, y, Colorer.RGB(alpha, alpha, alpha, alpha));
        }
    }
    
    public void mouseClicked(double mouseX, double mouseY, int button)
    {
        if (mouseOver((int) mouseX, (int) mouseY, x, y, x + IFont.NORMAL.getStringWidth(name), y + 13))
        {
            action.run();
            SoundUtil.playActionSound();
        }
    }
    
    public enum UnStyle { UNDERLINE, FADE }
}
