package fuzs.barteringstation.mixin;

import com.google.common.collect.ImmutableMap;
import fuzs.barteringstation.capability.BarteringStationCapability;
import fuzs.barteringstation.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mixin(Behavior.class)
abstract class BehaviorMixin<E extends LivingEntity> {
    private static final Map<MemoryModuleType<?>, MemoryStatus> ADMIRING_ITEM_ENTRY_CONDITION = ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT);
    @Shadow
    @Final
    protected Map<MemoryModuleType<?>, MemoryStatus> entryCondition;

    @Inject(method = "tryStart", at = @At("RETURN"))
    public final void barteringstation$tryStart(ServerLevel level, E owner, long gameTime, CallbackInfoReturnable<Boolean> callback) {
        // hook used to be in PiglinAi, but the vanilla brain system with all the static methods is really not cool for supporting other mods
        // so place this hook here, and just hope all piglin related mods with their custom bartering properly set same entry conditions as vanilla
        // works with e.g. Piglin Proliferation mod
        if (callback.getReturnValue() && owner instanceof Piglin && ADMIRING_ITEM_ENTRY_CONDITION.equals(this.entryCondition)) {
            Optional<BarteringStationCapability> optional = ModRegistry.BARTERING_STATION_CAPABILITY.maybeGet(owner);
            if (optional.isPresent() && optional.map(BarteringStationCapability::hasBarteringStationPos).orElseThrow()) {
                BarteringStationCapability capability = optional.orElseThrow(IllegalStateException::new);
                BlockPos pos = capability.getBarteringStationPos();
                capability.clearBarteringStationPos();
                level.getBlockEntity(pos, ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get()).ifPresent(blockEntity -> {
                    List<ItemEntity> thrownItems = level.getEntitiesOfClass(ItemEntity.class, owner.getBoundingBox(), item -> {
                        UUID thrower = item.getThrower();
                        return item.hasPickUpDelay() && thrower != null && thrower.equals(owner.getUUID());
                    });
                    for (ItemEntity item : thrownItems) {
                        if (blockEntity.placeBarterResponseItem(item.getItem())) {
                            item.discard();
                        }
                    }
                });
            }
        }
    }
}
