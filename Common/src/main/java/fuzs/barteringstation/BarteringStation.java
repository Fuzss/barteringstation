package fuzs.barteringstation;

import fuzs.barteringstation.config.ClientConfig;
import fuzs.barteringstation.config.ServerConfig;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarteringStation implements ModConstructor {
    public static final String MOD_ID = "barteringstation";
    public static final String MOD_NAME = "Bartering Station";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).client(ClientConfig.class).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.touch();
    }
}
