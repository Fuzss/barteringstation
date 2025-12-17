package fuzs.barteringstation.world.level.block.entity;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.config.ServerConfig;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.barteringstation.services.CommonAbstractions;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.puzzleslib.api.block.v1.entity.TickingBlockEntity;
import fuzs.puzzleslib.api.container.v1.ContainerMenuHelper;
import fuzs.puzzleslib.api.container.v1.ContainerSerializationHelper;
import fuzs.puzzleslib.api.container.v1.ListBackedContainer;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.IntUnaryOperator;

public class BarteringStationBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, ListBackedContainer, TickingBlockEntity {
    public static final MutableComponent CONTAINER_BARTERING_STATION = Component.translatable(
            "container.bartering_station");
    public static final String TAG_DELAY = BarteringStation.id("delay").toString();
    public static final int ALL_SLOTS = 21;
    public static final int CURRENCY_SLOTS = 6;
    public static final int DATA_SLOTS = 2;
    private static final int[] SLOTS_FOR_INPUT = Util.make(new int[CURRENCY_SLOTS], arr -> {
        Arrays.setAll(arr, IntUnaryOperator.identity());
    });
    private static final int[] SLOTS_FOR_OUTPUT = Util.make(new int[ALL_SLOTS - CURRENCY_SLOTS], arr -> {
        Arrays.setAll(arr, i -> i + CURRENCY_SLOTS);
    });

    private final ItemStationAnimationController animationController;
    private final ContainerData dataAccess;
    private final NonNullList<ItemStack> items = NonNullList.withSize(ALL_SLOTS, ItemStack.EMPTY);
    private int barterDelay;
    private int nearbyPiglins;

    public BarteringStationBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.value(), blockPos, blockState);
        this.animationController = new ItemStationAnimationController(blockPos);
        this.dataAccess = new ContainerData() {

            @Override
            public int get(int id) {
                return switch (id) {
                    case 0 -> BarteringStationBlockEntity.this.getBarterDelay();
                    case 1 -> BarteringStationBlockEntity.this.nearbyPiglins;
                    default -> throw new IndexOutOfBoundsException(id);
                };
            }

            @Override
            public void set(int id, int data) {
                // NO-OP
            }

            @Override
            public int getCount() {
                return DATA_SLOTS;
            }
        };
    }

    public int getBarterDelay() {
        return Math.min(BarteringStation.CONFIG.get(ServerConfig.class).barterDelay, this.barterDelay);
    }

    @Override
    protected void loadAdditional(ValueInput valueInput) {
        super.loadAdditional(valueInput);
        ContainerSerializationHelper.loadAllItems(valueInput, this.items);
        this.barterDelay = valueInput.getShortOr(TAG_DELAY, (short) 0);

    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        ContainerSerializationHelper.saveAllItems(valueOutput, this.items);
        valueOutput.putShort(TAG_DELAY, (short) this.barterDelay);
    }

    @Override
    protected Component getDefaultName() {
        return CONTAINER_BARTERING_STATION;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.getContainerItems();
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        ContainerMenuHelper.copyItemsIntoList(items, this.getContainerItems());
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new BarteringStationMenu(id, inventory, this, this.dataAccess);
    }

    @Override
    public void clientTick() {
        this.animationController.tick(this.getLevel());
    }

    @Override
    public void serverTick() {
        if (this.barterDelay > 0) this.barterDelay--;
        final int totalBarterDelay = BarteringStation.CONFIG.get(ServerConfig.class).barterDelay;
        boolean tryPerformBarter = this.barterDelay % totalBarterDelay == 0;
        // do this more often then handing out gold to update the piglin count in the client screen
        if (tryPerformBarter || this.barterDelay % (totalBarterDelay / 4) == 0) {
            List<Piglin> piglins = findNearbyPiglins(this.getLevel(), this.getBlockPos());
            this.nearbyPiglins = piglins.size();
            if (tryPerformBarter) {
                this.barterDelay = totalBarterDelay * 2;
                this.barterWithPiglins(this.getBlockPos(), piglins);
            }
        }
    }

    private static List<Piglin> findNearbyPiglins(Level level, BlockPos pos) {
        Vec3 vec3 = Vec3.atCenterOf(pos);
        int horizontalRange = BarteringStation.CONFIG.get(ServerConfig.class).horizontalRange;
        int verticalRange = BarteringStation.CONFIG.get(ServerConfig.class).verticalRange;
        return level.getEntitiesOfClass(Piglin.class,
                new AABB(vec3.add(-horizontalRange, -verticalRange, -horizontalRange),
                        vec3.add(horizontalRange, verticalRange, horizontalRange)),
                AbstractPiglin::isAdult);
    }

    private void barterWithPiglins(BlockPos pos, List<Piglin> piglins) {
        // stop giving out gold when not slots are available
        if (piglins.isEmpty() || this.findFreeResponseSlot().isEmpty()) return;
        int currentPiglin = 0;
        for (int i = 0; i < CURRENCY_SLOTS; i++) {
            ItemStack stack = this.getItem(i);
            if (!stack.isEmpty()) {
                while (currentPiglin < piglins.size()) {
                    if (mobInteract((ServerLevel) this.getLevel(), piglins.get(currentPiglin++), stack, pos)) {
                        // only the item stack is changed, nothing in the container itself is updated, therefore manually mark block entity as changed
                        this.setChanged();
                        this.barterDelay = BarteringStation.CONFIG.get(ServerConfig.class).barterDelay;
                        break;
                    }
                }
            }
        }
    }

    private OptionalInt findFreeResponseSlot() {
        for (int i = CURRENCY_SLOTS; i < this.getContainerSize(); i++) {
            if (this.getItem(i).isEmpty()) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private static boolean mobInteract(ServerLevel serverLevel, Piglin piglin, ItemStack itemStack, BlockPos blockPos) {
        if (PiglinAi.canAdmire(piglin, itemStack)) {
            ItemStack currencyStack = itemStack.split(1);
            PiglinAi.holdInOffhand(serverLevel, piglin, currencyStack);
            admireGoldItem(piglin);
            PiglinAi.stopWalking(piglin);
            ModRegistry.BARTERING_STATION_ATTACHMENT_TYPE.set(piglin, blockPos);
            return true;
        }
        return false;
    }

    private static void admireGoldItem(LivingEntity livingEntity) {
        // same as vanilla but with custom delay
        livingEntity.getBrain()
                .setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM,
                        true,
                        BarteringStation.CONFIG.get(ServerConfig.class).barterDelay / 2);
    }

    public ItemStationAnimationController getAnimationController() {
        return this.animationController;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return direction == Direction.DOWN ? SLOTS_FOR_OUTPUT : SLOTS_FOR_INPUT;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(index, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    public NonNullList<ItemStack> getContainerItems() {
        return this.items;
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index >= 0 && index < CURRENCY_SLOTS && CommonAbstractions.INSTANCE.isPiglinCurrency(stack);
    }

    public boolean placeBarterResponseItem(ItemStack stack) {
        while (true) {
            if (!stack.isEmpty()) {
                OptionalInt slot = this.findResponseSlotWithSpace(stack);
                if (slot.isEmpty()) {
                    slot = this.findFreeResponseSlot();
                }
                if (slot.isPresent()) {
                    this.mergeStackToSlot(stack, slot.getAsInt());
                    if (stack.isEmpty()) {
                        return true;
                    }
                    continue;
                }
            }
            return false;
        }
    }

    private OptionalInt findResponseSlotWithSpace(ItemStack stack) {
        for (int i = CURRENCY_SLOTS; i < this.getContainerSize(); i++) {
            if (this.hasSpaceForItem(this.getItem(i), stack)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private void mergeStackToSlot(ItemStack stackToMerge, int targetSlot) {
        ItemStack stackInSlot = this.getItem(targetSlot);
        ItemStack stackToInsert;
        if (stackInSlot.isEmpty()) {
            stackToInsert = stackToMerge.copyAndClear();
        } else {
            stackToInsert = stackInSlot.copy();
            int transferAmount = stackToMerge.getCount();
            transferAmount = Math.min(transferAmount, stackToInsert.getMaxStackSize() - stackToInsert.getCount());
            stackToInsert.grow(transferAmount);
            stackToMerge.shrink(transferAmount);
        }
        this.setItem(targetSlot, stackToInsert);
    }

    private boolean hasSpaceForItem(ItemStack itemStack, ItemStack otherItemStack) {
        return !itemStack.isEmpty() && ItemStack.isSameItemSameComponents(itemStack, otherItemStack)
                && itemStack.isStackable() && itemStack.getCount() < itemStack.getMaxStackSize()
                && itemStack.getCount() < this.getMaxStackSize();
    }
}
