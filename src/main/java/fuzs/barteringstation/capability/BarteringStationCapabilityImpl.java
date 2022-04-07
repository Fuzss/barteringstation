package fuzs.barteringstation.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class BarteringStationCapabilityImpl implements BarteringStationCapability {
    private BlockPos barteringStation;

    @Override
    public BlockPos getBarteringStationPos() {
        return this.barteringStation;
    }

    @Override
    public void setBarteringStationPos(BlockPos pos) {
        this.barteringStation = pos;
    }

    @Override
    public void write(CompoundTag tag) {
        if (this.hasBarteringStationPos()) {
            tag.putInt("BarteringStationX", this.barteringStation.getX());
            tag.putInt("BarteringStationY", this.barteringStation.getY());
            tag.putInt("BarteringStationZ", this.barteringStation.getZ());
        }
    }

    @Override
    public void read(CompoundTag tag) {
        if (tag.contains("BarteringStationX", 99) && tag.contains("BarteringStationY", 99) && tag.contains("BarteringStationZ", 99)) {
            this.barteringStation = new BlockPos(tag.getInt("BarteringStationX"), tag.getInt("BarteringStationY"), tag.getInt("BarteringStationZ"));
        }
    }
}
