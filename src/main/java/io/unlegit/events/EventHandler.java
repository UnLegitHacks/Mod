package io.unlegit.events;

import io.unlegit.events.impl.*;

public class EventHandler
{
    public static void handleEvent(EventListener listener, Event e)
    {
        // This syntax will force people to use Java 21. LOL
        switch (e)
        {
            case GuiRenderE g: listener.onGuiRender(g); break;
            case UpdateE u: listener.onUpdate(); break;
            case KeyE k: listener.onKey(k); break;
            case MoveE m: listener.onMove(m); break;
            case AttackE a: listener.onAttack(a); break;
            case MotionE m: listener.onMotion(m); break;
            case PacketSendE ps: listener.onPacketSend(ps); break;
            case PacketReceiveE pr: listener.onPacketReceive(pr); break;
            default: break;
        }
    }
}
