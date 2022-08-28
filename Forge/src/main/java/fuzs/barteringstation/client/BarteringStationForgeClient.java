package fuzs.barteringstation.client;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.client.handler.PiglinHeadModelHandler;
import fuzs.puzzleslib.client.core.ClientCoreServices;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = BarteringStation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BarteringStationForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientCoreServices.FACTORIES.clientModConstructor(BarteringStation.MOD_ID).accept(new BarteringStationClient());
    }

    @SubscribeEvent
    public static void onBakingCompleted(final ModelEvent.BakingCompleted evt) {
        PiglinHeadModelHandler.INSTANCE.invalidate();
    }
}
