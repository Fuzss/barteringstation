package fuzs.barteringstation.client;

import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.client.handler.PiglinHeadModelHandler;
import fuzs.barteringstation.client.init.ModClientRegistry;
import fuzs.barteringstation.client.renderer.blockentity.BarteringStationRenderer;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.client.core.ClientModConstructor;

public class BarteringStationClient implements ClientModConstructor {

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), BarteringStationRenderer::new);
    }

    @Override
    public void onRegisterMenuScreens(MenuScreensContext context) {
        context.registerMenuScreen(ModRegistry.BARTERING_STATION_MENU_TYPE.get(), BarteringStationScreen::new);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(ModClientRegistry.PIGLIN_HEAD_MODEL_LAYER_LOCATION, () -> PiglinHeadModelHandler.createPiglinHeadLayer(false));
    }

    @Override
    public void onRegisterModelBakingCompletedListeners(ModelBakingCompletedListenersContext context) {
        context.registerReloadListener(context1 -> PiglinHeadModelHandler.INSTANCE.bakeModel(context1::bakeModel));
    }

    @Override
    public void onRegisterAdditionalModels(AdditionalModelsContext context) {
        context.registerAdditionalModel(PiglinHeadModelHandler.PIGLIN_ITEM_MODEL_LOCATION);
    }
}
