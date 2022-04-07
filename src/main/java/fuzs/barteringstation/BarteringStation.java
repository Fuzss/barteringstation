package fuzs.barteringstation;

import fuzs.barteringstation.registry.ModRegistry;
import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import fuzs.barteringstation.config.ServerConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(BarteringStation.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BarteringStation {
    public static final String MOD_ID = "barteringstation";
    public static final String MOD_NAME = "Bartering Station";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<AbstractConfig, ServerConfig> CONFIG = ConfigHolder.server(() -> new ServerConfig());

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {

    }
}
