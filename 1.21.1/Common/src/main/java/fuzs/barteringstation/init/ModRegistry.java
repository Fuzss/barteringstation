package fuzs.barteringstation.init;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.BarteringStationBlock;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.api.capability.v3.CapabilityController;
import fuzs.puzzleslib.api.capability.v3.data.EntityCapabilityKey;
import fuzs.puzzleslib.api.core.v1.ModLoader;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.from(BarteringStation.MOD_ID);
    public static final Holder.Reference<Block> BARTERING_STATION_BLOCK = REGISTRY.registerBlock("bartering_station",
            () -> new BarteringStationBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CRIMSON_STEM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD))
    );
    public static final Holder.Reference<Item> BARTERING_STATION_ITEM = REGISTRY.registerBlockItem(
            BARTERING_STATION_BLOCK);
    public static final Holder.Reference<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = REGISTRY.whenNotOn(
                    ModLoader.FORGE)
            .registerBlockEntityType("bartering_station",
                    () -> BlockEntityType.Builder.of(BarteringStationBlockEntity::new, BARTERING_STATION_BLOCK.value())
            );
    public static final Holder.Reference<MenuType<BarteringStationMenu>> BARTERING_STATION_MENU_TYPE = REGISTRY.registerMenuType(
            "bartering_station",
            () -> BarteringStationMenu::new
    );

    static final CapabilityController CAPABILITIES = CapabilityController.from(BarteringStation.MOD_ID);
    public static final EntityCapabilityKey<Piglin, BarteringStationCapability> BARTERING_STATION_CAPABILITY = CAPABILITIES.registerEntityCapability(
            "bartering_station",
            BarteringStationCapability.class,
            BarteringStationCapability::new,
            Piglin.class
    );

    public static void touch() {

    }
}
