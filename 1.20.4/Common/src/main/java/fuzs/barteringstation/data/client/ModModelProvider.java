package fuzs.barteringstation.data.client;

import fuzs.barteringstation.client.handler.PiglinHeadModelRenderer;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.world.level.block.Blocks;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators builder) {
        builder.createCraftingTableLike(ModRegistry.BARTERING_STATION_BLOCK.value(), Blocks.CRIMSON_PLANKS, TextureMapping::craftingTable);
    }

    @Override
    public void addItemModels(ItemModelGenerators builder) {
        ModelTemplates.SKULL_INVENTORY.create(PiglinHeadModelRenderer.PIGLIN_ITEM_MODEL_LOCATION, new TextureMapping(), builder.output);
    }
}
