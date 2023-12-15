package com.jsh;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.state.StateManager;
import net.minecraft.world.World;
import net.minecraft.state.property.BooleanProperty;
public class MinerBlock extends HorizontalFacingBlock {
    public static final BooleanProperty TRIGGERED = BooleanProperty.of("triggered");

    public MinerBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH)
                .with(TRIGGERED, false)
            );
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING);
        builder.add(TRIGGERED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!state.get(TRIGGERED) && world.isReceivingRedstonePower(pos)) {
            world.breakBlock(pos.add(state.get(FACING).getVector()), true);
            world.setBlockState(pos, state.with(TRIGGERED, true));
        }
        else if (state.get(TRIGGERED) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.with(TRIGGERED, false));
        }
    }
}
