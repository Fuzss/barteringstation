package fuzs.barteringstation.client;

import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.client.handler.PiglinHeadModelRenderer;
import fuzs.barteringstation.client.renderer.blockentity.BarteringStationRenderer;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.AdditionalModelsContext;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import net.minecraft.client.gui.screens.MenuScreens;

public class BarteringStationClient implements ClientModConstructor {

    @Override
    public void onClientSetup() {
        MenuScreens.register(ModRegistry.BARTERING_STATION_MENU_TYPE.value(), BarteringStationScreen::new);
    }

    @Override
    public void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
        context.registerBlockEntityRenderer(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.value(), BarteringStationRenderer::new);
    }

    @Override
    public void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
        context.registerLayerDefinition(PiglinHeadModelRenderer.PIGLIN_HEAD_MODEL_LAYER_LOCATION, () -> PiglinHeadModelRenderer.createPiglinHeadLayer(false));
    }

    @Override
    public void onRegisterResourcePackReloadListeners(AddReloadListenersContext context) {
        context.registerReloadListener("piglin_head_model", PiglinHeadModelRenderer.INSTANCE);
    }

    @Override
    public void onRegisterAdditionalModels(AdditionalModelsContext context) {
        context.registerAdditionalModel(PiglinHeadModelRenderer.PIGLIN_ITEM_MODEL_LOCATION);
    }
}
