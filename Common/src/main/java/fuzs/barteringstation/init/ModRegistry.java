package fuzs.barteringstation.init;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.capability.BarteringStationCapabilityImpl;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.BarteringStationBlock;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.capability.data.CapabilityKey;
import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.init.RegistryManager;
import fuzs.puzzleslib.init.RegistryReference;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class ModRegistry {
    private static final RegistryManager REGISTRY = CoreServices.FACTORIES.registration(BarteringStation.MOD_ID);
    public static final RegistryReference<Block> BARTERING_STATION_BLOCK = REGISTRY.registerBlockWithItem("bartering_station", () -> new BarteringStationBlock(BlockBehaviour.Properties.of(Material.NETHER_WOOD, MaterialColor.CRIMSON_STEM).strength(2.0F, 3.0F).sound(SoundType.WOOD)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryReference<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = REGISTRY.placeholder(Registry.BLOCK_ENTITY_TYPE_REGISTRY, "bartering_station");
    public static final RegistryReference<MenuType<BarteringStationMenu>> BARTERING_STATION_MENU_TYPE = REGISTRY.registerMenuTypeSupplier("bartering_station", () -> BarteringStationMenu::new);

    private static final CapabilityController CAPABILITIES = CoreServices.FACTORIES.capabilities(BarteringStation.MOD_ID);
    public static final CapabilityKey<BarteringStationCapability> BARTERING_STATION_CAPABILITY = CAPABILITIES.registerEntityCapability("bartering_station", BarteringStationCapability.class, clazz -> new BarteringStationCapabilityImpl(), Piglin.class);

    public static void touch() {

    }
}
