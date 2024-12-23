package io.unlegit.events;

import io.unlegit.events.impl.block.BlockShapeE;
import io.unlegit.events.impl.client.*;
import io.unlegit.events.impl.entity.*;
import io.unlegit.events.impl.network.PacketReceiveE;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.events.impl.render.WorldRenderE;

public class EventHandler
{
    public static void handleEvent(EventListener listener, Event e)
    {
        // This syntax forces people to use Java 21 :D
        switch (e)
        {
            case GuiRenderE g: listener.onGuiRender(g); break;
            case UpdateE u: listener.onUpdate(); break;
            case KeyE k: listener.onKey(k); break;
            case MoveE m: listener.onMove(m); break;
            case AttackE a: listener.onAttack(a); break;
            case MotionE mo: listener.onMotion(mo); break;
            case PacketSendE ps: listener.onPacketSend(ps); break;
            case PacketReceiveE pr: listener.onPacketReceive(pr); break;
            case MessageE me: listener.onMessageReceive(me); break;
            case WorldRenderE w: listener.onWorldRender(w); break;
            case StrafeE s: listener.onStrafe(s); break;
            case WorldChangeE w: listener.onWorldChange(); break;
            case BlockShapeE b: listener.onBlockShape(b); break;
            case PlayerTurnE p: listener.onPlayerTurn(); break;
            default: break;
        }
    }
}
