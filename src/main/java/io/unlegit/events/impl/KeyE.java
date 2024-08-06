package io.unlegit.events.impl;

import io.unlegit.events.Event;

public class KeyE implements Event
{
    private static KeyE e = new KeyE();
    public int key;
    
    public static KeyE get(int key)
    {
        e.key = key;
        return e;
    }
}
