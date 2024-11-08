package io.unlegit.events.impl.entity;

import io.unlegit.events.Event;
import net.minecraft.world.phys.Vec3;

public class MoveE implements Event
{
    private static final MoveE e = new MoveE();
    public Vec3 vec3;
    
    public static MoveE get(Vec3 vec3)
    {
        e.vec3 = vec3;
        return e;
    }
}
