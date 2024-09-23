package io.unlegit.gui;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IGui;
import io.unlegit.utils.SoundUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UnThemePicker extends Screen implements IGui
{
    private static boolean initialized = false;
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
    
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    public void init()
    {
        if (initialized) return;
        
        SoundUtil.playSound("modern/intro.wav");
        UnLegit.setFirstLaunch(false);
        minecraft.setScreen(new UnTitleScreen()); // Temporary
        initialized = true;
    }
    
    public void onClose()
    {
        minecraft.setScreen(new UnTitleScreen());
    }
    
    public UnThemePicker() { super(Component.literal("Theme Picker")); }
}
