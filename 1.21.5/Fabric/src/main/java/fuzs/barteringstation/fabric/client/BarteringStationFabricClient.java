package fuzs.barteringstation.fabric.client;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.client.BarteringStationClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class BarteringStationFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(BarteringStation.MOD_ID, BarteringStationClient::new);
    }
}
