package fuzs.barteringstation.core;

import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean isStackPiglinCurrency(ItemStack stack) {
        return stack.getItem() == PiglinAi.BARTERING_ITEM;
    }
}
