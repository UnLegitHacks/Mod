package io.unlegit.events.impl.render;

import org.joml.Matrix4f;

import io.unlegit.events.Event;
import net.minecraft.client.Camera;

public class WorldRenderE implements Event
{
    private static final WorldRenderE e = new WorldRenderE();
    public float partialTicks;
    public Matrix4f matrix;
    public Camera camera;
    
    public static WorldRenderE get(Matrix4f matrix, Camera camera, float partialTicks)
    {
        e.matrix = matrix;
        e.camera = camera;
        e.partialTicks = partialTicks;
        return e;
    }
}
