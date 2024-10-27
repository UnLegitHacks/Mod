package io.unlegit.mixins.client.level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import io.unlegit.UnLegit;
import io.unlegit.events.impl.block.BlockShapeE;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin
{
    @ModifyReturnValue(method = "getCollisionShape", at = @At(value = "RETURN"))
    private VoxelShape getCollisionShape(VoxelShape shape, BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext)
    {
        if (blockPos == null)
            return shape;
        
        if (Minecraft.getInstance() != null)
        {
            BlockShapeE e = BlockShapeE.get(blockState, blockPos, shape);
            UnLegit.events.post(e);
            return e.changed ? e.shape : shape;
        }
        
        else return shape;
    }
}
