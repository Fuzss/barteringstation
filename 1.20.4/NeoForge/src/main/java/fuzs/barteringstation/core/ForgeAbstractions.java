package fuzs.barteringstation.core;

import net.minecraft.world.item.ItemStack;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean isPiglinCurrency(ItemStack stack) {
        return stack.isPiglinCurrency();
    }
}
