package io.unlegit.events.impl.entity;

import io.unlegit.events.Event;

public class PlayerTurnE implements Event
{
    private static final PlayerTurnE e = new PlayerTurnE();
    public static PlayerTurnE get() { return e; }
}
