package fuzs.barteringstation;

import fuzs.barteringstation.config.ClientConfig;
import fuzs.barteringstation.config.ServerConfig;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.core.ModConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarteringStation implements ModConstructor {
    public static final String MOD_ID = "barteringstation";
    public static final String MOD_NAME = "Bartering Station";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder CONFIG = CoreServices.FACTORIES
            .clientConfig(ClientConfig.class, () -> new ClientConfig())
            .serverConfig(ServerConfig.class, () -> new ServerConfig());

    @Override
    public void onConstructMod() {
        CONFIG.bakeConfigs(MOD_ID);
        ModRegistry.touch();
    }
}
