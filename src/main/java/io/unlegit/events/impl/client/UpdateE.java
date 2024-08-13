package io.unlegit.events.impl.client;

import io.unlegit.events.Event;

public class UpdateE implements Event
{
    private static UpdateE e = new UpdateE();
    public static UpdateE get() { return e; }
}
