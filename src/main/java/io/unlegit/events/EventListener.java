package io.unlegit.events;

import io.unlegit.events.impl.AttackE;
import io.unlegit.events.impl.GuiRenderE;
import io.unlegit.events.impl.KeyE;
import io.unlegit.events.impl.MotionE;
import io.unlegit.events.impl.MoveE;
import io.unlegit.events.impl.PacketReceiveE;
import io.unlegit.events.impl.PacketSendE;

/**
 * If you want to listen to events,
 * you need to implement this.
 */
public interface EventListener
{
    default void onGuiRender(GuiRenderE e) {} default void onUpdate() {} default void onKey(KeyE e) {}
    default void onMove(MoveE e) {} default void onAttack(AttackE e) {} default void onMotion(MotionE e) {}
    default void onPacketSend(PacketSendE e) {} default void onPacketReceive(PacketReceiveE e) {}
}
