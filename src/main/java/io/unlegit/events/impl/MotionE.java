package io.unlegit.events.impl;

import io.unlegit.events.Event;

public class MotionE implements Event
{
    private static MotionE e = new MotionE();
    public float yaw, pitch;
    public boolean onGround;
    public double x, y, z;
    
    public static MotionE get(double x, double y, double z, float yaw, float pitch, boolean onGround)
    {
        e.x = x; e.y = y; e.z = z; e.yaw = yaw; e.pitch = pitch; e.onGround = onGround;
        return e;
    }
}
