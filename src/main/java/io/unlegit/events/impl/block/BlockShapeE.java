package io.unlegit.events.impl.block;

import io.unlegit.events.Event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockShapeE implements Event
{
    private static BlockShapeE e = new BlockShapeE();
    public boolean changed = false;
    public VoxelShape shape;
    public BlockState state;
    public BlockPos pos;
    
    public static BlockShapeE get(BlockState state, BlockPos pos, VoxelShape shape)
    {
        e.state = state;
        e.pos = pos;
        e.shape = shape;
        e.changed = false;
        return e;
    }
}
