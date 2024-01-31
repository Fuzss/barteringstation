package fuzs.barteringstation.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class BarteringStationCapabilityImpl implements BarteringStationCapability {
    private static final String TAG_POSITION = "Pos";

    private BlockPos pos;

    @Override
    public BlockPos getBarteringStationPos() {
        return this.pos;
    }

    @Override
    public void setBarteringStationPos(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void write(CompoundTag tag) {
        if (this.hasBarteringStationPos()) {
            ListTag listTag = new ListTag();
            listTag.add(IntTag.valueOf(this.pos.getX()));
            listTag.add(IntTag.valueOf(this.pos.getY()));
            listTag.add(IntTag.valueOf(this.pos.getZ()));
            tag.put(TAG_POSITION, listTag);
        }
    }

    @Override
    public void read(CompoundTag tag) {
        if (tag.contains(TAG_POSITION, Tag.TAG_LIST)) {
            ListTag listTag = tag.getList(TAG_POSITION, Tag.TAG_INT);
            this.pos = new BlockPos(listTag.getInt(0), listTag.getInt(1), listTag.getInt(2));
        }
    }
}
