package io.unlegit.events.impl.entity;

import io.unlegit.events.Event;

public class StrafeE implements Event
{
    private static StrafeE e = new StrafeE();
    public float yaw;
    
    public static StrafeE get(float yaw)
    {
        e.yaw = yaw;
        return e;
    } public static StrafeE get() { return e; }
}
