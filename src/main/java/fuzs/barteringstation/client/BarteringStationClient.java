package fuzs.barteringstation.client;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.client.registry.ModClientRegistry;
import fuzs.barteringstation.client.renderer.blockentity.BarteringStationRenderer;
import fuzs.barteringstation.registry.ModRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BarteringStation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BarteringStationClient {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
        MenuScreens.register(ModRegistry.BARTERING_STATION_MENU_TYPE.get(), BarteringStationScreen::new);
        BlockEntityRenderers.register(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), BarteringStationRenderer::new);
        BlockEntityRenderers.register(ModRegistry.SKULL_BLOCK_ENTITY_TYPE.get(), SkullBlockRenderer::new);
        SkullBlockRenderer.SKIN_BY_TYPE.put(ModRegistry.PIGLIN_SKULL_BLOCK_TYPE, new ResourceLocation("textures/entity/piglin/piglin.png"));
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions evt) {
        evt.registerLayerDefinition(ModClientRegistry.PIGLIN_HEAD_MODEL_LAYER_LOCATION, () -> LayerDefinition.create(PiglinModel.createMesh(CubeDeformation.NONE), 64, 64));
    }

    @SubscribeEvent
    public static void onCreateSkullModels(final EntityRenderersEvent.CreateSkullModels evt) {
        evt.registerSkullModel(ModRegistry.PIGLIN_SKULL_BLOCK_TYPE, new SkullModel(evt.getEntityModelSet().bakeLayer(ModClientRegistry.PIGLIN_HEAD_MODEL_LAYER_LOCATION)));
    }
}
