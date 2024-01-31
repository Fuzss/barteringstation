package fuzs.barteringstation.neoforge;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.data.ModBlockLootProvider;
import fuzs.barteringstation.data.ModBlockTagProvider;
import fuzs.barteringstation.data.ModRecipeProvider;
import fuzs.barteringstation.data.client.ModLanguageProvider;
import fuzs.barteringstation.data.client.ModModelProvider;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import fuzs.puzzleslib.neoforge.api.init.v3.capability.NeoForgeCapabilityHelperV2;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(BarteringStation.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BarteringStationNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(BarteringStation.MOD_ID, BarteringStation::new);
        NeoForgeCapabilityHelperV2.registerWorldlyBlockEntityContainer(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE);
        DataProviderHelper.registerDataProviders(BarteringStation.MOD_ID,
                ModBlockLootProvider::new,
                ModBlockTagProvider::new,
                ModRecipeProvider::new,
                ModLanguageProvider::new,
                ModModelProvider::new
        );
    }
}
