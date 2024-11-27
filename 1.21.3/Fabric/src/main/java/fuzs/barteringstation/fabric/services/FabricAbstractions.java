package fuzs.barteringstation.fabric.services;

import fuzs.barteringstation.services.CommonAbstractions;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;

public class FabricAbstractions implements CommonAbstractions {

    @Override
    public boolean isPiglinCurrency(ItemStack itemStack) {
        return itemStack.getItem() == PiglinAi.BARTERING_ITEM;
    }
}
