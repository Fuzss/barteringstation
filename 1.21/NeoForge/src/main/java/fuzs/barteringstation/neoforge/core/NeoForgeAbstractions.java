package fuzs.barteringstation.neoforge.core;

import fuzs.barteringstation.core.CommonAbstractions;
import net.minecraft.world.item.ItemStack;

public class NeoForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean isPiglinCurrency(ItemStack stack) {
        return stack.isPiglinCurrency();
    }
}
