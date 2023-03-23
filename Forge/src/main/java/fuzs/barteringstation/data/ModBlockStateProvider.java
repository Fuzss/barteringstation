package fuzs.barteringstation.data;

import fuzs.barteringstation.client.handler.PiglinHeadModelHandler;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends AbstractModelProvider {

    public ModBlockStateProvider(PackOutput packOutput, String modId, ExistingFileHelper fileHelper) {
        super(packOutput, modId, fileHelper);
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
}
