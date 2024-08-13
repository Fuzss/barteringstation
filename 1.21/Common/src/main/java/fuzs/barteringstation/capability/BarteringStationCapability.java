package fuzs.barteringstation.capability;

import fuzs.barteringstation.BarteringStation;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.monster.piglin.Piglin;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BarteringStationCapability extends CapabilityComponent<Piglin> {
    public static final String TAG_POSITION = BarteringStation.id("position").toString();

    @Nullable
    private BlockPos pos;

    @Nullable
    public BlockPos getBarteringStationPos() {
        return this.pos;
    }

    public void setBarteringStationPos(@Nullable BlockPos pos) {
        if (!Objects.equals(this.pos, pos)) {
            this.pos = pos;
            this.setChanged();
        }
    }

    @Override
    public void write(CompoundTag tag) {
        if (this.pos != null) {
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
