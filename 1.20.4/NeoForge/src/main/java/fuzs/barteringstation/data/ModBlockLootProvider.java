package fuzs.barteringstation.data;

import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLootProvider;
import net.minecraft.data.PackOutput;

public class ModBlockLootProvider extends AbstractLootProvider.Blocks {

    public ModBlockLootProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    public void generate() {
        this.dropSelf(ModRegistry.BARTERING_STATION_BLOCK.get());
    }
}
