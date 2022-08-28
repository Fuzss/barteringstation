package fuzs.barteringstation;

import fuzs.barteringstation.init.FabricModRegistry;
import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;

public class BarteringStationFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(BarteringStation.MOD_ID).accept(new BarteringStation());
        FabricModRegistry.touch();
    }
}
