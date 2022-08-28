package fuzs.barteringstation.world.level.block;

import fuzs.barteringstation.init.ModRegistry;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BarteringStationBlock extends BaseEntityBlock {

    public BarteringStationBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get().create(pPos, pState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), pLevel.isClientSide ? BarteringStationBlockEntity::clientTick : BarteringStationBlockEntity::serverTick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (level.getBlockEntity(pos) instanceof BarteringStationBlockEntity blockEntity) {
                player.openMenu(blockEntity);
            }

            return InteractionResult.CONSUME;
        }
    }
    
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (stack.hasCustomHoverName()) {
            if (level.getBlockEntity(pos) instanceof BarteringStationBlockEntity blockEntity) {
                blockEntity.setCustomName(stack.getHoverName());
            }
        }
    }

    @Override
    public void onRemove(BlockState p_48713_, Level p_48714_, BlockPos p_48715_, BlockState p_48716_, boolean p_48717_) {
        if (!p_48713_.is(p_48716_.getBlock())) {
            BlockEntity blockentity = p_48714_.getBlockEntity(p_48715_);
            if (blockentity instanceof BarteringStationBlockEntity) {
                if (p_48714_ instanceof ServerLevel) {
                    Containers.dropContents(p_48714_, p_48715_, (BarteringStationBlockEntity)blockentity);
                }

                p_48714_.updateNeighbourForOutputSignal(p_48715_, this);
            }

            super.onRemove(p_48713_, p_48714_, p_48715_, p_48716_, p_48717_);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_48700_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState p_48702_, Level p_48703_, BlockPos p_48704_) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(p_48703_.getBlockEntity(p_48704_));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter blockReader, BlockPos pos, PathComputationType pathType) {
        return false;
    }
}
