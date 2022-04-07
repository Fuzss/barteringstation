package fuzs.barteringstation.world.level.block.entity;

import fuzs.barteringstation.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSkullBlockEntity extends SkullBlockEntity {
    public ModSkullBlockEntity(BlockPos p_155731_, BlockState p_155732_) {
        super(p_155731_, p_155732_);
    }

    @Override
    public BlockEntityType<?> getType() {
        return ModRegistry.SKULL_BLOCK_ENTITY_TYPE.get();
    }
}
