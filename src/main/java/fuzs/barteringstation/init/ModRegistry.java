package fuzs.barteringstation.init;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.capability.BarteringStationCapabilityImpl;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.BarteringStationBlock;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.barteringstation.world.level.block.entity.ForgeBarteringStationBlockEntity;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.registry.RegistryManager;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    private static final RegistryManager REGISTRY = RegistryManager.of(BarteringStation.MOD_ID);
    public static final RegistryObject<Block> BARTERING_STATION_BLOCK = REGISTRY.registerBlockWithItem("bartering_station", () -> new BarteringStationBlock(BlockBehaviour.Properties.of(Material.NETHER_WOOD, MaterialColor.CRIMSON_STEM).strength(2.0F, 3.0F).sound(SoundType.WOOD)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = REGISTRY.registerRawBlockEntityType("bartering_station", () -> BlockEntityType.Builder.of(ForgeBarteringStationBlockEntity::new, BARTERING_STATION_BLOCK.get()));
    public static final RegistryObject<MenuType<BarteringStationMenu>> BARTERING_STATION_MENU_TYPE = REGISTRY.registerRawMenuType("bartering_station", () -> BarteringStationMenu::new);

    private static final CapabilityController CAPABILITIES = CapabilityController.of(BarteringStation.MOD_ID);
    public static final Capability<BarteringStationCapability> BARTERING_STATION_CAPABILITY = CAPABILITIES.registerEntityCapability("bartering_station", BarteringStationCapability.class, clazz -> new BarteringStationCapabilityImpl(), Piglin.class, new CapabilityToken<BarteringStationCapability>() {});

    public static void touch() {

    }
}
