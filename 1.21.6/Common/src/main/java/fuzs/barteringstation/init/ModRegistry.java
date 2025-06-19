package fuzs.barteringstation.init;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.BarteringStationBlock;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import fuzs.puzzleslib.api.core.v1.ModLoader;
import fuzs.puzzleslib.api.init.v3.registry.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class ModRegistry {
    static final RegistryManager REGISTRIES = RegistryManager.from(BarteringStation.MOD_ID);
    public static final Holder.Reference<Block> BARTERING_STATION_BLOCK = REGISTRIES.registerBlock("bartering_station",
            BarteringStationBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CRIMSON_STEM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.NETHER_WOOD));
    public static final Holder.Reference<Item> BARTERING_STATION_ITEM = REGISTRIES.registerBlockItem(
            BARTERING_STATION_BLOCK);
    public static final Holder.Reference<BlockEntityType<BarteringStationBlockEntity>> BARTERING_STATION_BLOCK_ENTITY_TYPE = REGISTRIES.whenNotOn(
                    ModLoader.FORGE)
            .registerBlockEntityType("bartering_station", BarteringStationBlockEntity::new, BARTERING_STATION_BLOCK);
    public static final Holder.Reference<MenuType<BarteringStationMenu>> BARTERING_STATION_MENU_TYPE = REGISTRIES.registerMenuType(
            "bartering_station",
            BarteringStationMenu::new);

    public static final DataAttachmentType<Entity, BlockPos> BARTERING_STATION_ATTACHMENT_TYPE = DataAttachmentRegistry.<BlockPos>entityBuilder()
            .persistent(BlockPos.CODEC)
            .build(BarteringStation.id("bartering_station"));

    public static void bootstrap() {
        // NO-OP
    }
}
