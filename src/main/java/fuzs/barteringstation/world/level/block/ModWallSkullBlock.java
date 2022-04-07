package fuzs.barteringstation.world.level.block;

import fuzs.barteringstation.world.level.block.entity.ModSkullBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModWallSkullBlock extends WallSkullBlock {
    public ModWallSkullBlock(SkullBlock.Type p_58101_, Properties p_58102_) {
        super(p_58101_, p_58102_);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos p_151996_, BlockState p_151997_) {
        return new ModSkullBlockEntity(p_151996_, p_151997_);
    }
}
