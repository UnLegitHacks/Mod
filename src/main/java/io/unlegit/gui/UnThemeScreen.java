package io.unlegit.gui;

import io.unlegit.utils.SoundUtil;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UnThemeScreen extends Screen
{
    public void init()
    {
        SoundUtil.playSound("modern/intro.wav");
    }
    
    public UnThemeScreen() { super(Component.literal("Theme Picker")); }
}
