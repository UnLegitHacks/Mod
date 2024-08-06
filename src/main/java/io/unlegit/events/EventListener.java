package io.unlegit.events;

import io.unlegit.events.impl.KeyE;

/**
 * If you want to listen to events,
 * you need to implement this.
 */
public interface EventListener
{
    default void onRender() {} default void onUpdate() {} default void onKey(KeyE e) {}
}
