package fuzs.barteringstation.world.level.block;

import fuzs.barteringstation.registry.ModRegistry;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

@SuppressWarnings("deprecation")
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
        return new BarteringStationBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, ModRegistry. BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), BarteringStationBlockEntity::clientTick) : null;
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
    public boolean isPathfindable(BlockState state, BlockGetter blockReader, BlockPos pos, PathComputationType pathType) {
        return false;
    }
}
