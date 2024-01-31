package fuzs.barteringstation.client;

import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.client.handler.PiglinHeadModelHandler;
import fuzs.barteringstation.client.init.ModClientRegistry;
import fuzs.barteringstation.client.renderer.blockentity.BarteringStationRenderer;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.AdditionalModelsContext;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.DynamicBakingCompletedContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.core.v1.context.ModLifecycleContext;
import net.minecraft.client.gui.screens.MenuScreens;

public class BarteringStationClient implements ClientModConstructor {

    @Override
    public void onClientSetup(ModLifecycleContext context) {
        MenuScreens.register(ModRegistry.BARTERING_STATION_MENU_TYPE.get(), BarteringStationScreen::new);
    }

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), BarteringStationRenderer::new);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ModClientRegistry.PIGLIN_HEAD_MODEL_LAYER_LOCATION, () -> PiglinHeadModelHandler.createPiglinHeadLayer(false));
    }

    @Override
    public void onBakingCompleted(DynamicBakingCompletedContext context) {
        PiglinHeadModelHandler.INSTANCE.bakeModel(context::getModel);
    }

    @Override
    public void onRegisterAdditionalModels(AdditionalModelsContext context) {
        context.registerAdditionalModel(PiglinHeadModelHandler.PIGLIN_ITEM_MODEL_LOCATION);
    }
}
