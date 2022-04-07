package fuzs.barteringstation.registry;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.capability.BarteringStationCapabilityImpl;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.BarteringStationBlock;
import fuzs.barteringstation.world.level.block.ModSkullBlock;
import fuzs.barteringstation.world.level.block.ModWallSkullBlock;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.barteringstation.world.level.block.entity.ModSkullBlockEntity;
import fuzs.puzzleslib.capability.CapabilityController;
import fuzs.puzzleslib.registry.RegistryManager;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    public static final SkullBlock.Type PIGLIN_SKULL_BLOCK_TYPE = new SkullBlock.Type() {};

    private static final RegistryManager REGISTRY = RegistryManager.of(BarteringStation.MOD_ID);
    public static final RegistryObject<Block> PIGLIN_HEAD_BLOCK = REGISTRY.registerBlock("piglin_head", () -> new ModSkullBlock(PIGLIN_SKULL_BLOCK_TYPE, BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F)));
    public static final RegistryObject<Block> PIGLIN_WALL_HEAD_BLOCK = REGISTRY.registerBlock("piglin_wall_head", () -> new ModWallSkullBlock(PIGLIN_SKULL_BLOCK_TYPE, BlockBehaviour.Properties.of(Material.DECORATION).strength(1.0F).lootFrom(() -> PIGLIN_HEAD_BLOCK.get())));
    public static final RegistryObject<Item> PIGLIN_HEAD_ITEM = REGISTRY.registerItem("piglin_head", () -> new StandingAndWallBlockItem(PIGLIN_HEAD_BLOCK.get(), PIGLIN_WALL_HEAD_BLOCK.get(), (new Item.Properties()).tab(CreativeModeTab.TAB_DECORATIONS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockEntityType<ModSkullBlockEntity>> SKULL_BLOCK_ENTITY_TYPE = REGISTRY.registerRawBlockEntityType("skull", () -> BlockEntityType.Builder.of(ModSkullBlockEntity::new, PIGLIN_HEAD_BLOCK.get(), PIGLIN_WALL_HEAD_BLOCK.get()));

    public static final RegistryObject<Block> BARTERING_STATION_BLOCK = REGISTRY.registerBlockWithItem("bartering_station", () -> new BarteringStationBlock(BlockBehaviour.Properties.of(Material.NETHER_WOOD, MaterialColor.CRIMSON_STEM).strength(2.0F, 3.0F).sound(SoundType.WOOD)), CreativeModeTab.TAB_DECORATIONS);
    public static final RegistryObject<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = REGISTRY.registerRawBlockEntityType("bartering_station", () -> BlockEntityType.Builder.of(BarteringStationBlockEntity::new, BARTERING_STATION_BLOCK.get()));
    public static final RegistryObject<MenuType<BarteringStationMenu>> BARTERING_STATION_MENU_TYPE = REGISTRY.registerRawMenuType("bartering_station", () -> BarteringStationMenu::new);

    private static final CapabilityController CAPABILITIES = CapabilityController.of(BarteringStation.MOD_ID);
    public static final Capability<BarteringStationCapability> BARTERING_STATION_CAPABILITY = CAPABILITIES.registerEntityCapability("bartering_station", BarteringStationCapability.class, clazz -> new BarteringStationCapabilityImpl(), Piglin.class, new CapabilityToken<BarteringStationCapability>() {});

    public static void touch() {

    }
}
