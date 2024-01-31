package fuzs.barteringstation.client.init;

import fuzs.barteringstation.BarteringStation;
import fuzs.puzzleslib.api.client.init.v1.ModelLayerFactory;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModClientRegistry {
    private static final ModelLayerFactory LAYER_REGISTRY = ModelLayerFactory.from(BarteringStation.MOD_ID);
    public static final ModelLayerLocation PIGLIN_HEAD_MODEL_LAYER_LOCATION = LAYER_REGISTRY.register("piglin_head");
}