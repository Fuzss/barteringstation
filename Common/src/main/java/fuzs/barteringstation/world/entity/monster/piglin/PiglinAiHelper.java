package fuzs.barteringstation.world.entity.monster.piglin;

import fuzs.barteringstation.core.ModServices;
import fuzs.barteringstation.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;

public class PiglinAiHelper extends PiglinAi {

    public static boolean mobInteract(Piglin piglin, ItemStack stack, BlockPos source) {
        if (canAdmire(piglin, stack)) {
            ItemStack currencyStack = stack.split(1);
            holdInOffhand(piglin, currencyStack);
            admireGoldItem(piglin);
            stopWalking(piglin);
            ModRegistry.BARTERING_STATION_CAPABILITY.maybeGet(piglin)
                    .ifPresent(capability -> capability.setBarteringStationPos(source));
            return true;
        }
        return false;
    }

    private static void holdInOffhand(Piglin p_34933_, ItemStack p_34934_) {
        if (isHoldingItemInOffHand(p_34933_)) {
            p_34933_.spawnAtLocation(p_34933_.getItemInHand(InteractionHand.OFF_HAND));
        }
        holdInOffHand(p_34933_, p_34934_);
    }

    private static boolean isHoldingItemInOffHand(Piglin p_35027_) {
        return !p_35027_.getOffhandItem().isEmpty();
    }

    private static void holdInOffHand(Piglin piglin, ItemStack stack) {
        if (ModServices.ABSTRACTIONS.isStackPiglinCurrency(stack)) {
            piglin.setItemSlot(EquipmentSlot.OFFHAND, stack);
            piglin.setGuaranteedDrop(EquipmentSlot.OFFHAND);
        } else {
            setItemSlotAndDropWhenKilled(piglin, EquipmentSlot.OFFHAND, stack);
        }
    }

    private static void setItemSlotAndDropWhenKilled(Piglin piglin, EquipmentSlot p_21469_, ItemStack p_21470_) {
        piglin.setItemSlot(p_21469_, p_21470_);
        piglin.setGuaranteedDrop(p_21469_);
        piglin.setPersistenceRequired();
    }

    private static void admireGoldItem(LivingEntity p_34939_) {
        p_34939_.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, true, 120L);
    }

    private static void stopWalking(Piglin p_35007_) {
        p_35007_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_35007_.getNavigation().stop();
    }
}
