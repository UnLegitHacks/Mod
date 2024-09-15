package io.unlegit.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class KeyBindManager extends Screen
{
    private static KeyBindManager INSTANCE = new KeyBindManager();
    
    protected KeyBindManager()
    {
        super(Component.literal("Key Bind Manager"));
    }
    
    public static KeyBindManager get() { return INSTANCE; }
}
