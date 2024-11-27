package fuzs.barteringstation.services;

import fuzs.puzzleslib.api.core.v1.ServiceProviderHelper;
import net.minecraft.world.item.ItemStack;

public interface CommonAbstractions {
    CommonAbstractions INSTANCE = ServiceProviderHelper.load(CommonAbstractions.class);

    boolean isPiglinCurrency(ItemStack itemStack);
}
