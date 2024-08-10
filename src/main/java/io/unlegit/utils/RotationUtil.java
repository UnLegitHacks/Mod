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
        return new float[] {yaw, pitch};
    }
}
