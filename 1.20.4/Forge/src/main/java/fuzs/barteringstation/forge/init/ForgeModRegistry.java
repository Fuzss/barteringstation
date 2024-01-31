package fuzs.barteringstation.forge.init;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.forge.world.level.block.entity.ForgeBarteringStationBlockEntity;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForgeModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(BarteringStation.MOD_ID);
    public static final Holder.Reference<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = REGISTRY.registerBlockEntityType(
            "bartering_station",
            () -> BlockEntityType.Builder.of(ForgeBarteringStationBlockEntity::new,
                    ModRegistry.BARTERING_STATION_BLOCK.get()
            )
    );

    public static void touch() {

    }
}
