package fuzs.barteringstation.client.core;

import net.fabricmc.fabric.api.client.model.BakedModelManagerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public class FabricClientAbstractions implements ClientAbstractions {

    @Override
    public BakedModel bakeModel(ResourceLocation modelLocation) {
        return BakedModelManagerHelper.getModel(Minecraft.getInstance().getModelManager(), modelLocation);
    }
}
