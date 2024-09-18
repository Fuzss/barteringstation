package fuzs.barteringstation.mixin;

import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.init.ModRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PiglinAi.class)
abstract class PiglinAiMixin {

    @Inject(method = "stopHoldingOffHandItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;getBarterResponseItems(Lnet/minecraft/world/entity/monster/piglin/Piglin;)Ljava/util/List;"), cancellable = true)
    private static void stopHoldingOffHandItem(Piglin piglin, boolean shouldBarter, CallbackInfo callback) {
        if (!piglin.level().isClientSide) {
            BarteringStationCapability capability = ModRegistry.BARTERING_STATION_CAPABILITY.get(piglin);
            if (capability.getBarteringStationPos() != null) {
                piglin.level().getBlockEntity(capability.getBarteringStationPos(), ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.value()).ifPresent(blockEntity -> {
                    List<ItemStack> items = getBarterResponseItems(piglin);
                    items.removeIf(blockEntity::placeBarterResponseItem);
                    if (!items.isEmpty()) {
                        throwItems(piglin, items);
                    } else {
                        piglin.swing(InteractionHand.OFF_HAND);
                    }
                    callback.cancel();
                });
                capability.setBarteringStationPos(null);
            }
        }
    }

    @Shadow
    private static List<ItemStack> getBarterResponseItems(Piglin piglin) {
        throw new RuntimeException();
    }

    @Shadow
    private static void throwItems(Piglin piglin, List<ItemStack> stacks) {
        throw new RuntimeException();
    }
}
