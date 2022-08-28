package fuzs.barteringstation.core;

import net.minecraft.world.item.ItemStack;

public class ForgeAbstractions implements CommonAbstractions {

    @Override
    public boolean isStackPiglinCurrency(ItemStack stack) {
        return stack.isPiglinCurrency();
    }
}
