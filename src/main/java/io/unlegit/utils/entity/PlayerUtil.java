package io.unlegit.utils.entity;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class PlayerUtil implements IMinecraft
{
    public static boolean isCloseToEdge(Vec3 position)
    {
        return mc.level.isEmptyBlock(BlockPos.containing(position.subtract(0, 1, 0)))
                && mc.level.isEmptyBlock(BlockPos.containing(position.subtract(0, 2, 0)));
    }
    
    public static boolean isMoving()
    {
        return mc.options.keyUp.isDown() || mc.options.keyDown.isDown() || mc.options.keyLeft.isDown()
                || mc.options.keyRight.isDown();
    }
    
    public static boolean isInMotion()
    {
        Vec3 deltaMovement = mc.player.getDeltaMovement();
        return deltaMovement.x != 0 || deltaMovement.z != 0;
    }
    
    public static Vec3 strafe(Vec3 vec3, float speed)
    {
        float yaw = getDirection() * Mth.DEG_TO_RAD;
        float x = -Mth.sin(yaw) * speed, z = Mth.cos(yaw) * speed;
        return new Vec3(x, vec3.y, z);
    }
    
    public static float getSpeed(Vec3 vec3)
    {
        return Mth.sqrt((float) (vec3.x * vec3.x + vec3.z * vec3.z));
    }
    
    public static float getDirection()
    {
        float rotationYaw = mc.player.getYRot(), forward = 1;
        
        if (mc.player.input.forwardImpulse < 0)
        {
            rotationYaw += 180;
            forward = -0.5F;
        }
        
        else if (mc.player.input.forwardImpulse > 0) forward = 0.5F;
        if (mc.player.input.leftImpulse > 0) rotationYaw -= 90 * forward;
        else if (mc.player.input.leftImpulse < 0) rotationYaw += 90 * forward;
        return rotationYaw;
    }
    
    public static Vec3 strafe(Vec3 vec3) { return strafe(vec3, getSpeed(vec3)); }
}
