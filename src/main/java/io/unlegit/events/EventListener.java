package io.unlegit.events;

import io.unlegit.events.impl.client.KeyE;
import io.unlegit.events.impl.client.MessageE;
import io.unlegit.events.impl.entity.*;
import io.unlegit.events.impl.network.PacketReceiveE;
import io.unlegit.events.impl.network.PacketSendE;
import io.unlegit.events.impl.render.GuiRenderE;
import io.unlegit.events.impl.render.WorldRenderE;

/**
 * If you want to listen to events,
 * you need to implement this.
 */
public interface EventListener
{
    default void onGuiRender(GuiRenderE e) {} default void onUpdate() {} default void onKey(KeyE e) {}
    default void onMove(MoveE e) {} default void onAttack(AttackE e) {} default void onMotion(MotionE e) {}
    default void onPacketSend(PacketSendE e) {} default void onPacketReceive(PacketReceiveE e) {}
    default void onMessageReceive(MessageE e) {} default void onWorldRender(WorldRenderE e) {}
    default void onStrafe(StrafeE e) {}
}
