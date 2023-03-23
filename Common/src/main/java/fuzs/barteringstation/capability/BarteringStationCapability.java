package fuzs.barteringstation.capability;

import fuzs.puzzleslib.api.capability.v2.data.CapabilityComponent;
import net.minecraft.core.BlockPos;

public interface BarteringStationCapability extends CapabilityComponent {

    BlockPos getBarteringStationPos();

    void setBarteringStationPos(BlockPos pos);

    default boolean hasBarteringStationPos() {
        return this.getBarteringStationPos() != null;
    }

    default void clearBarteringStationPos() {
        this.setBarteringStationPos(null);
    }
}
