package io.unlegit.events.impl.entity;

import io.unlegit.events.Event;

public class AttackE implements Event
{
    private static AttackE e = new AttackE();
    public boolean cancelled = false;
    
    public static AttackE get()
    {
        e.cancelled = false;
        return e;
    }
}
