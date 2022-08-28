package fuzs.barteringstation.client;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.client.handler.PiglinHeadModelHandler;
import fuzs.barteringstation.client.init.ModClientRegistry;
import fuzs.barteringstation.client.renderer.blockentity.BarteringStationRenderer;
import fuzs.barteringstation.init.ModRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BarteringStation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BarteringStationClient {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
        BlockEntityRenderers.register(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), BarteringStationRenderer::new);
        MenuScreens.register(ModRegistry.BARTERING_STATION_MENU_TYPE.get(), BarteringStationScreen::new);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions evt) {
        evt.registerLayerDefinition(ModClientRegistry.PIGLIN_HEAD_MODEL_LAYER_LOCATION, () -> PiglinHeadModelHandler.createPiglinHeadLayer(false));
    }

    @SubscribeEvent
    public static void onModelRegister(final ModelRegistryEvent evt) {
        // 1.19 has a dedicated event for this
        ForgeModelBakery.addSpecialModel(PiglinHeadModelHandler.PIGLIN_ITEM_MODEL_LOCATION);
    }

    @SubscribeEvent
    public static void onModelBake(final ModelBakeEvent evt) {
        PiglinHeadModelHandler.INSTANCE.invalidate();
    }
}
