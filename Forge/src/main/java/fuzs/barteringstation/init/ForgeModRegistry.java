package fuzs.barteringstation.init;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.barteringstation.world.level.block.entity.ForgeBarteringStationBlockEntity;
import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import fuzs.puzzleslib.init.builder.ModBlockEntityTypeBuilder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForgeModRegistry {
    private static final RegistryManager REGISTRY = CoreServices.FACTORIES.registration(BarteringStation.MOD_ID);
    public static final RegistryReference<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityTypeBuilder("bartering_station", () -> ModBlockEntityTypeBuilder.of(ForgeBarteringStationBlockEntity::new, ModRegistry.BARTERING_STATION_BLOCK.get()));

    public static void touch() {

    }
}
