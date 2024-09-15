package io.unlegit.utils.entity;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.modules.impl.player.InvMove;
import net.minecraft.client.KeyMapping;
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
        return keyHeld(mc.options.keyUp) || keyHeld(mc.options.keyDown) ||
               keyHeld(mc.options.keyLeft) || keyHeld(mc.options.keyRight);
    }
    
    private static boolean keyHeld(KeyMapping key)
    {
        return UnLegit.modules.get("Inv Move").isEnabled() ?
                InvMove.canMove(key) : key.isDown();
    }
    
    public static boolean isInMotion()
    {
        Vec3 deltaMovement = mc.player.getDeltaMovement();
        return deltaMovement.x != 0 || deltaMovement.z != 0;
    }
    
    public static Vec3 strafe(float speed)
    {
        Vec3 vec3 = mc.player.getDeltaMovement();
        float movementYaw = getDirection() * Mth.DEG_TO_RAD;
        float x = -Mth.sin(movementYaw) * speed, z = Mth.cos(movementYaw) * speed;
        return new Vec3(x, vec3.y, z);
    }
    
    public static float getSpeed()
    {
        Vec3 vec3 = mc.player.getDeltaMovement();
        return Mth.sqrt((float) (vec3.x * vec3.x + vec3.z * vec3.z));
    }
    
    public static float getDirection()
    {
        float movementYaw = mc.player.getYRot(), forward = 1;
        
        if (mc.player.input.forwardImpulse < 0)
        {
            movementYaw += 180;
            forward = -0.5F;
        }
        
        else if (mc.player.input.forwardImpulse > 0) forward = 0.5F;
        if (mc.player.input.leftImpulse > 0) movementYaw -= 90 * forward;
        else if (mc.player.input.leftImpulse < 0) movementYaw += 90 * forward;
        return movementYaw;
    }
    
    public static Vec3 strafe() { return strafe(getSpeed()); }
}
