package io.unlegit.gui.buttons;

import java.awt.Color;

import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.Animation;
import io.unlegit.utils.SoundUtil;
import net.minecraft.client.gui.GuiGraphics;

public class UnPlainTextButton implements IGui
{
    private Animation hoverAnimation = null;
    private boolean flag = false;
    private int width, height;
    private Runnable action;
    private String name;
    
    public UnPlainTextButton(String name, int width, int height, Runnable action)
    {
        this.name = name;
        this.action = action;
        this.width = width; this.height = height;
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        if (mouseOver((int) mouseX, (int) mouseY, width - IFont.NORMAL.getStringWidth(name) - 1, height - 13, width, height))
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
        
        if (hoverAnimation != null)
            graphics.fill(width + (25 - hoverAnimation.wrap(25)) - IFont.NORMAL.getStringWidth(name) - 1, height - 2, width - (25 - hoverAnimation.wrap(25)), height - 1, new Color(255, 255, 255, hoverAnimation.wrap(255)).getRGB());
    }
    
    public void mouseClicked(double mouseX, double mouseY, int button)
    {
        if (mouseOver((int) mouseX, (int) mouseY, width - IFont.NORMAL.getStringWidth(name) - 1, height - 13, width, height))
        {
            action.run();
            SoundUtil.playActionSound();
        }
    }
}
