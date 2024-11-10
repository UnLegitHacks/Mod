package io.unlegit.gui.buttons;

import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.SoundUtil;
import io.unlegit.utils.render.Animation;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class UnButton implements IGui
{
    private final ResourceLocation buttonTexture;
    private Animation animation = null;
    private boolean prevHover = false;
    private final String displayName;
    private final Runnable action;
    private final int x, y;
    
    public UnButton(String displayName, String textureName, int x, int y, Runnable action)
    {
        buttonTexture = get(ResourceLocation.fromNamespaceAndPath("unlegit", "mainmenu/buttons/" + textureName + ".png"));
        this.displayName = displayName;
        this.action = action;
        this.x = x;
        this.y = y;
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY)
    {
        int x = this.x, y = this.y, size = 64;
        boolean mouseOver = mouseOver(mouseX, mouseY, x, y, x + size, y + size);
        updateAnimation(mouseOver);
        
        if (animation != null)
        {
            size += animation.wrap(16); x -= animation.wrap(8); y -= animation.wrap(8);
            IFont.NORMAL.drawCenteredStringWithShadow(graphics, displayName, x + (size / 2), (y + size) - 8, Colorer.RGB(255, 255, 255, animation.wrap(255)));
        }
        
        graphics.blit(RenderType::guiTextured, buttonTexture, x, y, size, size, size, size, size, size);
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
