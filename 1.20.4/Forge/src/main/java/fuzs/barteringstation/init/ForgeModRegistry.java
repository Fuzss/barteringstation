package fuzs.barteringstation.init;

import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.barteringstation.world.level.block.entity.ForgeBarteringStationBlockEntity;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForgeModRegistry {
    public static final RegistryReference<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = ModRegistry.REGISTRY.registerBlockEntityType("bartering_station", () -> BlockEntityType.Builder.of(ForgeBarteringStationBlockEntity::new, ModRegistry.BARTERING_STATION_BLOCK.get()));

    public static void touch() {

    }
}
