package io.unlegit.events.impl.render;

import io.unlegit.events.Event;
import net.minecraft.client.gui.GuiGraphics;

public class GuiRenderE implements Event
{
    private static final GuiRenderE e = new GuiRenderE();
    public GuiGraphics graphics;
    public float partialTicks;
    
    public static GuiRenderE get(GuiGraphics graphics, float partialTicks)
    {
        e.graphics = graphics;
        e.partialTicks = partialTicks;
        return e;
    }
}
