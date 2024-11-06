package io.unlegit.bots;

import io.unlegit.interfaces.IMinecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

// Automatically strafes.
public class StrafeEntity implements IMinecraft
{
    private static double random = Math.random();
    private static boolean direction = false;

    public static void tick()
    {
        if (mc.player.tickCount % (60 + (int) (random * 60)) == 0)
        {
            direction = !direction;
            random = Math.random();
        }

        if (slopeNear(mc.player))
            stop();

        else if (direction)
        {
            mc.options.keyLeft.setDown(true);
            mc.options.keyRight.setDown(false);
        }

        else
        {
            mc.options.keyLeft.setDown(false);
            mc.options.keyRight.setDown(true);
        }
    }

    public static void stop()
    {
        mc.options.keyLeft.setDown(false);
        mc.options.keyRight.setDown(false);
    }

    // Stops you from falling while you are strafing.
    public static boolean slopeNear(LocalPlayer player)
    {
        for (double x = player.getX() - 3; x < player.getX() + 3; x++)
        {
            for (double z = player.getZ() - 3; z < player.getZ() + 3; z++)
            {
                if (mc.level.getBlockState(new BlockPos((int) x, (int) player.getY() - 1, (int) z))
                        .is(Blocks.AIR))
                    return true;
            }
        }

        return false;
    }
}
