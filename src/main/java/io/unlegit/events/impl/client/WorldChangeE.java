package io.unlegit.events.impl.client;

import io.unlegit.events.Event;

public class WorldChangeE implements Event
{
    private static final WorldChangeE e = new WorldChangeE();
    public static WorldChangeE get() { return e; }
}
