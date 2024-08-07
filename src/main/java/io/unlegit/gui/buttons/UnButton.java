package io.unlegit.gui.buttons;

import java.awt.Color;

import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.utils.Animation;
import io.unlegit.utils.SoundUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class UnButton implements IMinecraft, IGui
{
    private ResourceLocation buttonTexture;
    private Animation animation = null;
    private boolean prevHover = false;
    private String displayName;
    private Runnable action;
    private int x, y;
    
    public UnButton(String displayName, String textureName, int x, int y, Runnable action)
    {
        buttonTexture = withLinearScaling(ResourceLocation.fromNamespaceAndPath("unlegit", "main_menu/buttons/" + textureName + ".png"));
        this.displayName = displayName;
        this.action = action;
        this.x = x;
        this.y = y;
    }
    
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY)
    {
        int x = this.x, y = this.y, size = 64;
        boolean mouseOver = mouseOver(mouseX, mouseY, x, y, x + size, y + size);
        updateAnimation(mouseOver);
        
        if (animation != null)
        {
            size += animation.wrap(16); x -= animation.wrap(8); y -= animation.wrap(8);
            IFont.NORMAL.drawCenteredStringWithShadow(guiGraphics, displayName, x + (size / 2), (y + size) - 8, new Color(255, 255, 255, animation.wrap(255)));
        }
        
        guiGraphics.blit(buttonTexture, x, y, size, size, size, size, size, size);
        prevHover = mouseOver;
    }
    
    public void mouseClicked(double mouseX, double mouseY, int button)
    {
        if (button == 0 && prevHover)
        {
            this.action.run();
            SoundUtil.playActionSound();
        }
    }
    
    public void updateAnimation(boolean flag)
    {
        if (flag)
        {
            if (animation == null || !prevHover) animation = new Animation(64);
        }
        
        else if (prevHover)
        {
            animation = new Animation(64);
            animation.reverse = true;
        }
    }
}
