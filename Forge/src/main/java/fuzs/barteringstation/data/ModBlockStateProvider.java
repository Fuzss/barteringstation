package fuzs.barteringstation.data;

import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.client.handler.PiglinHeadModelHandler;
import fuzs.barteringstation.init.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.commons.lang3.ArrayUtils;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(DataGenerator gen, String modId, ExistingFileHelper exFileHelper) {
        super(gen, modId, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(ModRegistry.BARTERING_STATION_BLOCK.get(), this.models().cube(this.name(ModRegistry.BARTERING_STATION_BLOCK.get()),
                this.mcLoc("block/crimson_planks"),
                this.modLoc("block/bartering_station_top"),
                this.modLoc("block/bartering_station_front"),
                this.modLoc("block/bartering_station_side"),
                this.modLoc("block/bartering_station_side"),
                this.modLoc("block/bartering_station_front")
        ).texture("particle", this.modLoc("block/bartering_station_front")));
        this.itemModels().withExistingParent(this.name(ModRegistry.BARTERING_STATION_BLOCK.get()), this.extendKey(ModRegistry.BARTERING_STATION_BLOCK.get(), ModelProvider.BLOCK_FOLDER));
        this.itemModels().withExistingParent(PiglinHeadModelHandler.PIGLIN_ITEM_MODEL_LOCATION.toString(), this.mcLoc("template_skull"));
    }

    private ResourceLocation key(Block block) {
        return Registry.BLOCK.getKey(block);
    }

    private ResourceLocation extendKey(Block block, String... extensions) {
        ResourceLocation loc = this.key(block);
        extensions = ArrayUtils.add(extensions, loc.getPath());
        return new ResourceLocation(loc.getNamespace(), String.join("/", extensions));
    }

    private String name(Block block) {
        return this.key(block).getPath();
    }
}
