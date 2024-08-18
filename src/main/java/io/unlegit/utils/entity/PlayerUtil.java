package io.unlegit.utils.entity;

import io.unlegit.interfaces.IMinecraft;

public class PlayerUtil implements IMinecraft
{
    public static boolean isMoving()
    {
        return mc.options.keyUp.isDown() || mc.options.keyDown.isDown() ||
               mc.options.keyLeft.isDown() || mc.options.keyUp.isDown();
    }
}
