package fuzs.barteringstation;

import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.data.*;
import fuzs.barteringstation.init.ForgeModRegistry;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.capability.v2.ForgeCapabilityHelper;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

import java.util.concurrent.CompletableFuture;

@Mod(BarteringStation.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BarteringStationForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(BarteringStation.MOD_ID, BarteringStation::new);
        ForgeModRegistry.touch();
        registerCapabilities();
    }

    private static void registerCapabilities() {
        ForgeCapabilityHelper.setCapabilityToken(ModRegistry.BARTERING_STATION_CAPABILITY, new CapabilityToken<BarteringStationCapability>() {});
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        final DataGenerator dataGenerator = evt.getGenerator();
        final PackOutput packOutput = dataGenerator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = evt.getLookupProvider();
        final ExistingFileHelper fileHelper = evt.getExistingFileHelper();
        dataGenerator.addProvider(true, new ModBlockLootProvider(packOutput, BarteringStation.MOD_ID));
        dataGenerator.addProvider(true, new ModBlockStateProvider(packOutput, BarteringStation.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModBlockTagsProvider(packOutput, lookupProvider, BarteringStation.MOD_ID, fileHelper));
        dataGenerator.addProvider(true, new ModLanguageProvider(packOutput, BarteringStation.MOD_ID));
        dataGenerator.addProvider(true, new ModRecipeProvider(packOutput));
    }
}
