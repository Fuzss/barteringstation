package fuzs.barteringstation.data;

import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractLanguageProvider;
import net.minecraft.data.PackOutput;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.BARTERING_STATION_BLOCK.get(), "Bartering Station");
        this.add("container.barteringstation.bartering_station", "Bartering Station");
        this.add("gui.barteringstation.bartering_station.piglins", "Found %s piglin(s) nearby for bartering");
    }
}
