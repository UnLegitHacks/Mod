package io.unlegit.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UnLegitOptions extends Screen
{
    public static boolean WATER_MARK = false, INTRO_SOUND = false;
    private static UnLegitOptions INSTANCE = new UnLegitOptions();
    
    protected UnLegitOptions()
    {
        super(Component.literal("UnLegit Options"));
    }
    
    public static UnLegitOptions screen() { return INSTANCE; }
}
