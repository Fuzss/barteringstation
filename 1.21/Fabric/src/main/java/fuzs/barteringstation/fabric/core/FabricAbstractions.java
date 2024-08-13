package fuzs.barteringstation.fabric.core;

import fuzs.barteringstation.core.CommonAbstractions;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean isPiglinCurrency(ItemStack stack) {
        return stack.getItem() == PiglinAi.BARTERING_ITEM;
    }
}
