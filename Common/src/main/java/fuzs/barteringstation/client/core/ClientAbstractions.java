package fuzs.barteringstation.client.core;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public interface ClientAbstractions {

    BakedModel bakeModel(ResourceLocation modelLocation);
}
