package io.unlegit.modules.impl.movement.scaffold;

import com.mojang.blaze3d.platform.InputConstants;

import io.unlegit.UnLegit;
import io.unlegit.interfaces.IMinecraft;
import io.unlegit.mixins.client.KeyMapAccessor;
import io.unlegit.utils.entity.PlayerUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;

public class HelperBlock implements IMinecraft
{
    public static Direction getDirection()
    {
        return PlayerUtil.isInMotion() ? Direction.fromYRot(PlayerUtil.getDirection(mc.player.getYRot())).getOpposite()
                : jumpKeyDown() ? Direction.UP : mc.player.getDirection().getOpposite();
    }
    
    public static double getBlockX()
    {
        double playerX = mc.player.getX();
        
        switch (getDirection())
        {
            case WEST:
                return (int) Math.floor(playerX);
            case EAST:
                return (int) Math.floor(playerX + 1);
            default:
                return playerX;
        }
    }
    
    public static double getBlockZ()
    {
        double playerZ = mc.player.getZ();
        
        switch (getDirection())
        {
            case NORTH:
                return (int) Math.floor(playerZ);
            case SOUTH:
                return (int) Math.floor(playerZ + 1);
            default:
                return playerZ;
        }
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
        return InputConstants.isKeyDown(mc.getWindow().getWindow(), ((KeyMapAccessor) jump).getKey().getValue());
    }
    
    private static Scaffold getScaffold() { return (Scaffold) UnLegit.modules.get("Scaffold"); }
}
