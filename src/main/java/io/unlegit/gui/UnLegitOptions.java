package io.unlegit.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UnLegitOptions extends Screen
{
    private static final UnLegitOptions INSTANCE = new UnLegitOptions();
    
    public static boolean WATER_MARK = false;
    
    protected UnLegitOptions()
    {
        super(Component.literal("UnLegit Options"));
    }
    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks)
    {
    }
    
    public static UnLegitOptions screen() { return INSTANCE; }
}
