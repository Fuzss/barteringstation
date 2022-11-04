package fuzs.barteringstation;

import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.data.*;
import fuzs.barteringstation.init.ForgeModRegistry;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.capability.ForgeCapabilityController;
import fuzs.puzzleslib.core.CoreServices;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod(BarteringStation.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BarteringStationForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CoreServices.FACTORIES.modConstructor(BarteringStation.MOD_ID).accept(new BarteringStation());
        ForgeModRegistry.touch();
        registerCapabilities();
    }

    private static void registerCapabilities() {
        ForgeCapabilityController.setCapabilityToken(ModRegistry.BARTERING_STATION_CAPABILITY, new CapabilityToken<BarteringStationCapability>() {});
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(new ModBlockTagsProvider(generator, BarteringStation.MOD_ID, existingFileHelper));
        generator.addProvider(new ModLootTableProvider(generator, BarteringStation.MOD_ID));
        generator.addProvider(new ModRecipeProvider(generator));
        generator.addProvider(new ModLanguageProvider(generator, BarteringStation.MOD_ID));
        generator.addProvider(new ModBlockStateProvider(generator, BarteringStation.MOD_ID, existingFileHelper));
    }
}
