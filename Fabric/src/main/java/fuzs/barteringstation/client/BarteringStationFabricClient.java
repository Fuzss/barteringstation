package fuzs.barteringstation.client;

import fuzs.barteringstation.BarteringStation;
import fuzs.puzzleslib.client.core.ClientCoreServices;
import net.fabricmc.api.ClientModInitializer;

public class BarteringStationFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientCoreServices.FACTORIES.clientModConstructor(BarteringStation.MOD_ID).accept(new BarteringStationClient());
    }
}
