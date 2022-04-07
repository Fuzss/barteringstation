package fuzs.barteringstation.client;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.client.renderer.blockentity.BarteringStationRenderer;
import fuzs.barteringstation.registry.ModRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = BarteringStation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BarteringStationClient {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
//        MenuScreens.register(ModRegistry.TRADING_POST_MENU_TYPE.get(), TradingPostScreen::new);
        BlockEntityRenderers.register(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), BarteringStationRenderer::new);
    }
}
