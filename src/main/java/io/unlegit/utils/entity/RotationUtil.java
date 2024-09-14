package io.unlegit.utils.entity;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RotationUtil implements IMinecraft
{
    public static float[] rotations(LivingEntity target, boolean predict)
    {
        double x, y, z;
        
        if (predict)
        {
            x = (target.getX() + (target.getX() - target.xo)) - mc.player.getX();
            z = (target.getZ() + (target.getZ() - target.zo)) - mc.player.getZ();
        }
        
        else
        {
            x = target.getX() - mc.player.getX();
            z = target.getZ() - mc.player.getZ();
        }
        
        y = target.getY() - mc.player.getY() - (mc.player.getEyeHeight() - target.getEyeHeight());
        double hypot = Math.sqrt(x * x + z * z);
        float yaw = (float) (Mth.atan2(z, x) * 180 / Math.PI) - 90;
        float pitch = (float) -(Mth.atan2(y, hypot) * 180 / Math.PI);
        return new float[] {yaw, pitch};
    }
    
    public static float[] rotations(BlockPos block)
    {
        double x = block.getX() + 0.5D - mc.player.getX();
        double y = block.getY() - 0.5D - mc.player.getY();
        double z = block.getZ() + 0.5D - mc.player.getZ();
        double hypot = Math.sqrt(x * x + z * z);
        float yaw = (float) (Mth.atan2(z, x) * 180 / Math.PI) - 90;
        float pitch = (float) -(Mth.atan2(y, hypot) * 180 / Math.PI);
        return new float[] {yaw, pitch};
    }
    
    public static float[] rotations(LivingEntity target) { return rotations(target, false); }
    
    public static boolean rayTrace(LivingEntity target, float yaw, float pitch, float distance)
    {
        Vec3 camera = mc.player.getEyePosition(),
             rotation = mc.player.calculateViewVector(pitch, yaw),
             position = camera.add(rotation.x * distance, rotation.y * distance, rotation.z * distance);
        AABB box = target.getBoundingBox().expandTowards(rotation.scale(distance));
        return ProjectileUtil.getEntityHitResult(mc.player, camera, position, box, entity -> !entity.isSpectator(), Mth.square(distance)) != null;
    }
    
    public static class GCDFix implements IMinecraft
    {
        public static float[] get(float[] rotations)
        {
            float e = (mc.options.sensitivity().get().floatValue() * 0.6F) + 0.2F,
                  GCD = e * e * e * 1.2F;
            
            float yaw = rotations[0], pitch = rotations[1];
            yaw -= yaw % GCD; pitch -= pitch % GCD;
            return new float[] {yaw, pitch};
        }
    }
}
