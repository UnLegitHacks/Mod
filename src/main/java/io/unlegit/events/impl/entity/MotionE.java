package io.unlegit.events.impl.entity;

import io.unlegit.events.Event;

public class MotionE implements Event
{
    public boolean onGround, sprinting, changed = false;
    private static MotionE e = new MotionE();
    public float yaw, pitch;
    public double x, y, z;
    
    public static MotionE get(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting)
    {
        e.x = x; e.y = y; e.z = z; e.yaw = yaw; e.pitch = pitch; e.onGround = onGround; e.sprinting = sprinting; e.changed = false;
        return e;
    } public static MotionE get() { return e; }
}
