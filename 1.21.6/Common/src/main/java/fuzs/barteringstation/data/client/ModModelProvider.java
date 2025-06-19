package fuzs.barteringstation.data.client;

import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.client.data.v2.AbstractModelProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.world.level.block.Blocks;

public class ModModelProvider extends AbstractModelProvider {

    public ModModelProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addBlockModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createCraftingTableLike(ModRegistry.BARTERING_STATION_BLOCK.value(),
                Blocks.CRIMSON_PLANKS,
                TextureMapping::craftingTable);
    }
}
