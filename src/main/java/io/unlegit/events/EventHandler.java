package io.unlegit.events;

import io.unlegit.events.impl.*;

public class EventHandler
{
    public static void handleEvent(EventListener listener, Event e)
    {
        // This syntax will force people to use Java 21. LOL
        switch (e)
        {
            case GuiRenderE g: listener.onRender(); break;
            case UpdateE u: listener.onUpdate(); break;
            case KeyE k: listener.onKey(k); break;
            case MoveE m: listener.onMove(m); break;
            default: break;
        }
    }
}
