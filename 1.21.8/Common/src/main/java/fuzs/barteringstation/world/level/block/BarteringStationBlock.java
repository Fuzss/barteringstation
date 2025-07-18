package fuzs.barteringstation.world.level.block;

import com.mojang.serialization.MapCodec;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.api.block.v1.entity.TickingEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;

public class BarteringStationBlock extends BaseEntityBlock implements TickingEntityBlock<BarteringStationBlockEntity> {
    public static final MapCodec<BarteringStationBlock> CODEC = simpleCodec(BarteringStationBlock::new);

    public BarteringStationBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntityType<? extends BarteringStationBlockEntity> getBlockEntityType() {
        return ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.value();
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
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
    protected void affectNeighborsAfterRemoval(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, boolean bl) {
        Containers.updateNeighboursAfterDestroy(blockState, serverLevel, blockPos);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Override
    public boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }
}
