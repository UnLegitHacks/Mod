package io.unlegit.alts;

import io.unlegit.alts.popups.AddAltPopup;
import io.unlegit.alts.popups.AltPopup;
import io.unlegit.gui.font.IFont;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.render.Animation;
import io.unlegit.utils.render.Colorer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;

public class AltManager extends Screen implements IGui
{
    private static final AltManager INSTANCE = new AltManager();
    private final ArrayList<Alt> alts = new ArrayList<>();
    public Animation animation = null;
    private AltPopup popup;
    private Screen parent;
    
    protected AltManager()
    {
        super(Component.literal("Alt Manager"));
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(graphics, mouseX, mouseY, partialTicks);
        IFont.LARGE.drawStringWithShadow(graphics, "Alts", 4, 3, -1);
        IFont.NORMAL.drawStringWithShadow(graphics, "Add an account by clicking the top right button.", width - IFont.NORMAL.getStringWidth("Add an account by clicking the top right button.") - 2, height - 25, Color.LIGHT_GRAY.getRGB());
        IFont.NORMAL.drawStringWithShadow(graphics, "Left click on any \"alt\" to log into it and play on it.", width - IFont.NORMAL.getStringWidth("Left click on any \"alt\" to log into it and play on it.") - 2, height - 14, Color.LIGHT_GRAY.getRGB());
        
        IFont.LARGE.drawString(graphics, "+", width - 18, -1, mouseOver(mouseX, mouseY, width - 24, 0, width, 24) ?
                Colorer.RGB(0, 255, 255) : Colorer.RGB(0, 255, 128));

        if (popup != null)
            popup.render(graphics, mouseX, mouseY, partialTicks);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (mouseOver((int) mouseX, (int) mouseY, width - 24, 0, width, 24) && button == 0)
        {
            if (popup == null)
                popup = new AddAltPopup(width, height);
        }

        return popup != null ? popup.mouseClicked(mouseX, mouseY, button) : super.mouseClicked(mouseX, mouseY, button);
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
