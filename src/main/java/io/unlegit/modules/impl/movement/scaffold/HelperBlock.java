package io.unlegit.modules.impl.movement.scaffold;

import com.mojang.blaze3d.platform.InputConstants;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.mixins.client.AccKeyMap;
import io.unlegit.utils.entity.PlayerUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;

public class HelperBlock implements IMinecraft
{
    public static Direction getDirection()
    {
        return jumpKeyDown() && !PlayerUtil.isMoving() &&
                !PlayerUtil.isCloseToEdge(mc.player.position()) ? Direction.UP :
                    PlayerUtil.isInMotion() ? Direction.fromYRot(PlayerUtil.getDirection()).getOpposite() :
                        mc.player.getDirection().getOpposite();
    }
    
    public static double getBlockX()
    {
        double playerX = mc.player.getX();

        return switch (getDirection())
        {
            case WEST -> (int) Math.floor(playerX);
            case EAST -> (int) Math.floor(playerX + 1);
            default -> playerX;
        };
    }
    
    public static double getBlockZ()
    {
        double playerZ = mc.player.getZ();

        return switch (getDirection())
        {
            case NORTH -> (int) Math.floor(playerZ);
            case SOUTH -> (int) Math.floor(playerZ + 1);
            default -> playerZ;
        };
    }
    
    public static void preSwitchItem()
    {
        Scaffold scaffold = getScaffold();
        if (scaffold.switchItem.equals("Normal") || scaffold.blockSlot == -1) return;
        scaffold.prevItem = mc.player.getMainHandItem();
        mc.player.setItemInHand(InteractionHand.MAIN_HAND, mc.player.getInventory().getItem(scaffold.blockSlot));
    }
    
    public static void postSwitchItem()
    {
        Scaffold scaffold = getScaffold();
        if (scaffold.switchItem.equals("Normal") || scaffold.blockSlot == -1) return;
        mc.player.setItemInHand(InteractionHand.MAIN_HAND, scaffold.prevItem);
    }
    
    public static boolean jumpKeyDown()
    {
        KeyMapping jump = mc.options.keyJump;
        return InputConstants.isKeyDown(mc.getWindow().getWindow(), ((AccKeyMap) jump).getKey().getValue());
    }
    
    private static Scaffold getScaffold() { return (Scaffold) UnLegit.modules.get("Scaffold"); }
}
