package com.jsh;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.state.StateManager;
import net.minecraft.world.World;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.world.event.GameEvent;

public class PlacerBlock extends DispenserBlock {
    public PlacerBlock(AbstractBlock.Settings settings) {
        super(settings);
    }
    private static final DispenserBehavior BEHAVIOR = new ItemDispenserBehavior();

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(Properties.FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    protected void dispense(ServerWorld world, BlockState state, BlockPos pos) {
        DispenserBlockEntity dispenserBlockEntity = (DispenserBlockEntity)world.getBlockEntity(pos, BlockEntityType.DISPENSER).orElse((DispenserBlockEntity) null);
        if (dispenserBlockEntity == null) {
            // LOGGER.warn("Ignoring dispensing attempt for Dropper without matching block entity at {}", pos);
        } else {
            int i = dispenserBlockEntity.chooseNonEmptySlot(world.random);
            if (i < 0) {
                world.syncWorldEvent(1001, pos, 0);
            } else {
                ItemStack itemStack = dispenserBlockEntity.getStack(i);
                if (!itemStack.isEmpty()) {

                    Item item = itemStack.getItem();
                    Direction direction = world.getBlockState(pos).get(FACING);

                    if (item instanceof BlockItem blockItem) {

                        BlockState blockState2 = blockItem.getBlock().getDefaultState();
                        world.setBlockState(pos.offset(direction), blockState2);
                        world.emitGameEvent(GameEvent.BLOCK_PLACE, pos.offset(direction), null);
                        itemStack.decrement(1);
                        dispenserBlockEntity.setStack(i, itemStack);
                    }
                }
            }
        }
    }
}
