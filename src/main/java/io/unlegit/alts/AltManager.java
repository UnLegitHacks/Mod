package io.unlegit.alts;

import java.awt.Color;
import java.util.ArrayList;

import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.render.Animation;
import io.unlegit.utils.render.EzColor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class AltManager extends Screen implements IGui
{
    private static AltManager INSTANCE = new AltManager();
    private ArrayList<Alt> alts = new ArrayList<>();
    public Animation animation = null;
    private Screen parent;
    
    protected AltManager()
    {
        super(Component.literal("Alt Manager"));
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.fill(0, 0, width, height, EzColor.RGB(0, 0, 0, 100));
        IFont.LARGE.drawString(graphics, "Alts", 4, 3, -1);
        IFont.NORMAL.drawString(graphics, "Add an account by clicking the top right button.", width - IFont.NORMAL.getStringWidth("Add an account by clicking the top right button.") - 2, height - 25, Color.LIGHT_GRAY.getRGB());
        IFont.NORMAL.drawString(graphics, "Left click on any \"alt\" to log into it and play on it.", width - IFont.NORMAL.getStringWidth("Double click on any \"alt\" to log into it and play on it.") - 2, height - 14, Color.LIGHT_GRAY.getRGB());
        
        IFont.LARGE.drawString(graphics, "+", width - 18, -1, mouseOver(mouseX, mouseY, width - 24, 0, width, 24) ?
                EzColor.RGB(0, 255, 255) : EzColor.RGB(0, 255, 128));
    }
    
    public void onClose()
    {
        minecraft.setScreen(parent);
    }
    
    public static AltManager get(Screen parent)
    {
        INSTANCE.parent = parent;
        return INSTANCE;
    }
    
    public static ArrayList<Alt> alts() { return INSTANCE.alts; }
}
