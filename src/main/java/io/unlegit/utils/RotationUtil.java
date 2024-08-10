package io.unlegit.utils;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

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
}

class GCDFix implements IMinecraft
{
    public static float[] get(float[] rotations)
    {
        float prevYaw = mc.player.yRotO, prevPitch = mc.player.xRotO;
        float yaw = rotations[0], pitch = rotations[1];
        float differenceYaw = Math.abs(yaw - prevYaw), differencePitch = Math.abs(pitch - prevPitch);
        
        if (differenceYaw < (minimumMouseFactor() / 2)) yaw = prevYaw;
        else yaw = fix(yaw, prevYaw);
        
        if (differencePitch < (minimumMouseFactor() / 2)) pitch = prevPitch;
        else pitch = fix(pitch, prevPitch);
        
        return new float[] {yaw, pitch};
    }
    
    private static float fix(float rotation, float prevRotation)
    {
        float sens = sensitivity();
        return (Math.round((rotation - prevRotation) / 0.15F / sens) * 0.15F * sens) + prevRotation;
    }
    
    private static float sensitivity()
    {
        float e = (mc.options.sensitivity().get().floatValue() * 0.6F) + 0.2F;
        return e * e * e * 8;
    }
    
    private static float minimumMouseFactor() { return 0.15F * sensitivity(); }
}
