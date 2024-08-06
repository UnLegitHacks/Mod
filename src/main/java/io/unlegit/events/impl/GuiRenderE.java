package io.unlegit.events.impl;

import io.unlegit.events.Event;

public class GuiRenderE implements Event
{
    private static GuiRenderE e = new GuiRenderE();
    public static GuiRenderE get() { return e; }
}
