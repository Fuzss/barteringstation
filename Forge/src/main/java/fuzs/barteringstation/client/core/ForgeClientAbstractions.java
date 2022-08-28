package fuzs.barteringstation.client.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public class ForgeClientAbstractions implements ClientAbstractions {

    @Override
    public BakedModel bakeModel(ResourceLocation modelLocation) {
        ModelBakery modelBakery = Minecraft.getInstance().getModelManager().getModelBakery();
        UnbakedModel unbakedModel = modelBakery.getModel(modelLocation);
        return unbakedModel.bake(modelBakery, modelBakery.getAtlasSet()::getSprite, BlockModelRotation.X0_Y0, modelLocation);
    }
}
