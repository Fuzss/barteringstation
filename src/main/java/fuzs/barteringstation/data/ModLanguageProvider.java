package fuzs.barteringstation.data;

import fuzs.barteringstation.registry.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_us");
    }

    @Override
    protected void addTranslations() {
        this.add(ModRegistry.BARTERING_STATION_BLOCK.get(), "Bartering Station");
        this.add(ModRegistry.PIGLIN_HEAD_BLOCK.get(), "Piglin Head");
        this.add(ModRegistry.PIGLIN_WALL_HEAD_BLOCK.get(), "Piglin Wall Head");
        this.add(ModRegistry.PIGLIN_HEAD_ITEM.get(), "Piglin Head");
        this.add(ModRegistry.BARTERING_STATION_BLOCK.get(), "Bartering Station");
        this.add("container.barteringstation.bartering_station", "Bartering Station");
        this.add("gui.barteringstation.bartering_station.piglins", "x%s");
    }
}
