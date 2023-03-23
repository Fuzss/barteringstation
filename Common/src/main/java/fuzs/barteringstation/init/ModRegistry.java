package fuzs.barteringstation.init;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.capability.BarteringStationCapabilityImpl;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.BarteringStationBlock;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.api.capability.v2.CapabilityController;
import fuzs.puzzleslib.api.capability.v2.data.CapabilityKey;
import fuzs.puzzleslib.api.core.v1.ModLoader;
import fuzs.puzzleslib.api.init.v2.RegistryManager;
import fuzs.puzzleslib.api.init.v2.RegistryReference;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class ModRegistry {
    static final RegistryManager REGISTRY = RegistryManager.instant(BarteringStation.MOD_ID);
    public static final RegistryReference<Block> BARTERING_STATION_BLOCK = REGISTRY.registerBlock("bartering_station", () -> new BarteringStationBlock(BlockBehaviour.Properties.of(Material.NETHER_WOOD, MaterialColor.CRIMSON_STEM).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final RegistryReference<Item> BARTERING_STATION_ITEM = REGISTRY.registerBlockItem(BARTERING_STATION_BLOCK);
    public static final RegistryReference<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = REGISTRY.whenNotOn(ModLoader.FORGE).registerBlockEntityType("bartering_station", () -> BlockEntityType.Builder.of(BarteringStationBlockEntity::new, BARTERING_STATION_BLOCK.get()));
    public static final RegistryReference<MenuType<BarteringStationMenu>> BARTERING_STATION_MENU_TYPE = REGISTRY.registerMenuType("bartering_station", () -> BarteringStationMenu::new);

    private static final CapabilityController CAPABILITIES = CapabilityController.from(BarteringStation.MOD_ID);
    public static final CapabilityKey<BarteringStationCapability> BARTERING_STATION_CAPABILITY = CAPABILITIES.registerEntityCapability("bartering_station", BarteringStationCapability.class, clazz -> new BarteringStationCapabilityImpl(), Piglin.class);

    public static void touch() {

    }
}
