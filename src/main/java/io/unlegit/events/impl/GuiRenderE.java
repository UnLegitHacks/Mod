package io.unlegit.events.impl;

import io.unlegit.events.Event;
import net.minecraft.client.gui.GuiGraphics;

public class GuiRenderE implements Event
{
    private static GuiRenderE e = new GuiRenderE();
    public GuiGraphics graphics;
    
    public static GuiRenderE get(GuiGraphics graphics)
    {
        e.graphics = graphics;
        return e;
    }
}
