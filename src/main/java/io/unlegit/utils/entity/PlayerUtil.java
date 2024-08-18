package io.unlegit.utils.entity;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class PlayerUtil implements IMinecraft
{
    public static boolean isCloseToEdge(Vec3 position)
    {
        return mc.level.isEmptyBlock(BlockPos.containing(position.subtract(0, 1, 0)));
    }
    
    public static boolean isMoving()
    {
        return mc.options.keyUp.isDown() || mc.options.keyDown.isDown() ||
               mc.options.keyLeft.isDown() || mc.options.keyUp.isDown();
    }
}
