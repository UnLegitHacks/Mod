package io.unlegit.modules.impl.movement;

import io.unlegit.interfaces.IModule;
import io.unlegit.modules.ModuleU;
import io.unlegit.utils.entity.PlayerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

// You thought this was going to be named Block Fly? lol
@IModule(name = "Scaffold", description = "Automatically bridges for you.")
public class Scaffold extends ModuleU
{
    public void onUpdate()
    {
        Vec3 block = new Vec3(getBlockX(), mc.player.getY(), getBlockZ());
        MutableBlockPos pos = new BlockPos((int) mc.player.getX(), (int) block.y - 1, (int) mc.player.getZ()).relative(getDirection()).mutable();
        
        if (mc.level.isEmptyBlock(pos.relative(getDirection().getOpposite())))
        {
            ItemStack itemStack = mc.player.getMainHandItem();
            int i = itemStack.getCount();
            BlockHitResult hitResult = new BlockHitResult(block, getDirection().getOpposite(), pos, false);
            InteractionResult actResult =  mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, hitResult);
            if (actResult.consumesAction() && actResult.shouldSwing()) mc.player.swing(InteractionHand.MAIN_HAND);
            
            if (!itemStack.isEmpty() && (itemStack.getCount() != i || mc.gameMode.hasInfiniteItems()))
                mc.gameRenderer.itemInHandRenderer.itemUsed(InteractionHand.MAIN_HAND);
        }
    }
    
    public double getBlockX()
    {
        double playerX = mc.player.getX();
        
        switch (getDirection())
        {
            case WEST:
                return (int) playerX;
            case EAST:
                return (int) playerX + 1;
            default:
                return playerX;
        }
    }
    
    public double getBlockZ()
    {
        double playerZ = mc.player.getZ();
        
        switch (getDirection())
        {
            case NORTH:
                return (int) playerZ;
            case SOUTH:
                return (int) playerZ + 1;
            default:
                return playerZ;
        }
    }
    
    private Direction getDirection()
    {
        return Direction.fromYRot(PlayerUtil.getDirection()).getOpposite();
    }
}
