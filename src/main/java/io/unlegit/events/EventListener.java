package io.unlegit.events;

/**
 * If you want to listen to events,
 * you need to implement this.
 */
public interface EventListener
{
    default void onRender() {} default void onUpdate() {}
}
