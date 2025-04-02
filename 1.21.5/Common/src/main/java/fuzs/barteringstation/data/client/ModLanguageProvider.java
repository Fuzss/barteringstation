package fuzs.barteringstation.data.client;

import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.api.client.data.v2.AbstractLanguageProvider;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;

public class ModLanguageProvider extends AbstractLanguageProvider {

    public ModLanguageProvider(DataProviderContext context) {
        super(context);
    }

    @Override
    public void addTranslations(TranslationBuilder builder) {
        builder.add(ModRegistry.BARTERING_STATION_BLOCK.value(), "Bartering Station");
        builder.add(BarteringStationBlockEntity.CONTAINER_BARTERING_STATION, "Bartering Station");
        builder.add(BarteringStationScreen.KEY_NEARBY_PIGLINS, "%sx %s");
    }
}
