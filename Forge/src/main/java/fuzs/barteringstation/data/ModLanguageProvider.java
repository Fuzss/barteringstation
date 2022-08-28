package fuzs.barteringstation.data;

import fuzs.barteringstation.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(DataGenerator gen, String modId) {
        super(gen, modId, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.BARTERING_STATION_BLOCK.get(), "Bartering Station");
        this.add("container.barteringstation.bartering_station", "Bartering Station");
        this.add("gui.barteringstation.bartering_station.piglins", "Found %s piglins nearby for bartering");
    }
}
