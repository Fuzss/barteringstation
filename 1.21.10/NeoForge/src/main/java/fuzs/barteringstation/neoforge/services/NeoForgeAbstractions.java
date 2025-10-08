package fuzs.barteringstation.neoforge.services;

import fuzs.barteringstation.services.CommonAbstractions;
import net.minecraft.world.item.ItemStack;

public class NeoForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean isPiglinCurrency(ItemStack itemStack) {
        return itemStack.isPiglinCurrency();
    }
}
