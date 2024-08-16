package io.unlegit.utils.entity;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RotationUtil implements IMinecraft
{
    public static float[] rotations(LivingEntity target)
    {
        double x = target.getX() - mc.player.getX();
        double y = target.getY() - mc.player.getY() - (mc.player.getEyeHeight() - target.getEyeHeight());
        double z = target.getZ() - mc.player.getZ();
        double hypot = Math.sqrt(x * x + z * z);
        float yaw = (float) (Mth.atan2(z, x) * 180 / Math.PI) - 90;
        float pitch = (float) -(Mth.atan2(y, hypot) * 180 / Math.PI);
        return GCDFix.get(new float[] {yaw, pitch});
    }
    
    public static boolean rayTrace(LivingEntity target, float yaw, float pitch, float distance)
    {
        Vec3 camera = mc.player.getEyePosition(),
             rotation = mc.player.calculateViewVector(pitch, yaw),
             position = camera.add(rotation.x * distance, rotation.y * distance, rotation.z * distance);
        AABB box = target.getBoundingBox().expandTowards(rotation.scale(distance)).expandTowards(1, 1, 1);
        return ProjectileUtil.getEntityHitResult(mc.player, camera, position, box, entity -> !entity.isSpectator(), Mth.square(distance)) != null;
    }
}

class GCDFix implements IMinecraft
{
    private static float[] prevRotations = null;
    
    public static float[] get(float[] rotations)
    {
        if (prevRotations == null) prevRotations = rotations;
        
        float e = (mc.options.sensitivity().get().floatValue() * 0.6F) + 0.2F,
              GCD = e * e * e * 1.2F;
        
        float deltaYaw = rotations[0] - prevRotations[0], deltaPitch = rotations[1] - prevRotations[1];
        float betaYaw = deltaYaw - (deltaYaw % GCD), betaPitch = deltaPitch - (deltaPitch % GCD);
        float yaw = prevRotations[0] + betaYaw, pitch = prevRotations[1] + betaPitch;
        
        prevRotations = rotations;
        return new float[] {yaw, pitch};
    }
}
