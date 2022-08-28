package fuzs.barteringstation.world.level.block.entity;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.core.ModServices;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.barteringstation.world.entity.monster.piglin.PiglinAiHelper;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.BlockLightingUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.IntUnaryOperator;

public class BarteringStationBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final String TAG_DELAY = "Delay";
    public static final int ALL_SLOTS = 21;
    public static final int CURRENCY_SLOTS = 6;
    public static final int DATA_SLOTS = 2;
    private static final int[] SLOTS_FOR_INPUT = Util.make(new int[CURRENCY_SLOTS], arr -> Arrays.setAll(arr, IntUnaryOperator.identity()));
    private static final int[] SLOTS_FOR_OUTPUT = Util.make(new int[ALL_SLOTS - CURRENCY_SLOTS], arr -> Arrays.setAll(arr, i -> i + CURRENCY_SLOTS));
    private final ContainerData dataAccess = new ContainerData() {

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
            throw new UnsupportedOperationException("Writing to bartering station data access not supported");
        }

        @Override
        public int getCount() {
            return DATA_SLOTS;
        }
    };
    private NonNullList<ItemStack> items = NonNullList.withSize(ALL_SLOTS, ItemStack.EMPTY);
    private int barterDelay;
    private int nearbyPiglins;
    public int time;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    private float tRot;
    public int combinedLight;

    public BarteringStationBlockEntity(BlockPos p_155077_, BlockState p_155078_) {
        super(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), p_155077_, p_155078_);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        this.barterDelay = tag.getShort(TAG_DELAY);

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putShort(TAG_DELAY, (short) this.barterDelay);
    }

    public int getBarterDelay() {
        return Math.min(BarteringStation.CONFIG.server().barterDelay, this.barterDelay);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BarteringStationBlockEntity blockEntity) {
        blockEntity.oOpen = blockEntity.open;
        blockEntity.oRot = blockEntity.rot;
        Player player = level.getNearestPlayer((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 3.0, false);
        if (player != null) {
            double d0 = player.getX() - ((double) pos.getX() + 0.5);
            double d1 = player.getZ() - ((double) pos.getZ() + 0.5);
            blockEntity.tRot = (float) Mth.atan2(d1, d0);
            blockEntity.open += 0.1F;
        } else {
            blockEntity.tRot += 0.02F;
            blockEntity.open -= 0.1F;
        }
        while (blockEntity.rot >= (float) Math.PI) {
            blockEntity.rot -= ((float) Math.PI * 2.0F);
        }
        while (blockEntity.rot < -(float) Math.PI) {
            blockEntity.rot += ((float) Math.PI * 2.0F);
        }
        while (blockEntity.tRot >= (float) Math.PI) {
            blockEntity.tRot -= ((float) Math.PI * 2.0F);
        }
        while (blockEntity.tRot < -(float) Math.PI) {
            blockEntity.tRot += ((float) Math.PI * 2.0F);
        }
        float f2;
        f2 = blockEntity.tRot - blockEntity.rot;
        while (f2 >= (float) Math.PI) {
            f2 -= ((float) Math.PI * 2.0F);
        }
        while (f2 < -(float) Math.PI) {
            f2 += ((float) Math.PI * 2.0F);
        }
        blockEntity.rot += f2 * 0.4F;
        blockEntity.open = Mth.clamp(blockEntity.open, 0.0F, 1.0F);
        ++blockEntity.time;
        blockEntity.combinedLight = BlockLightingUtil.getLightColor(level, blockEntity.getBlockPos().above());
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BarteringStationBlockEntity blockEntity) {
        if (blockEntity.barterDelay > 0) blockEntity.barterDelay--;
        final int totalBarterDelay = BarteringStation.CONFIG.server().barterDelay;
        boolean tryPerformBarter = blockEntity.barterDelay % totalBarterDelay == 0;
        // do this more often then handing out gold to update the piglin count in the client screen
        if (tryPerformBarter || blockEntity.barterDelay % (totalBarterDelay / 4) == 0) {
            List<Piglin> piglins = findNearbyPiglins(level, pos);
            blockEntity.nearbyPiglins = piglins.size();
            if (tryPerformBarter) {
                blockEntity.barterDelay = totalBarterDelay * 2;
                barterWithPiglins(pos, blockEntity, piglins);
            }
        }
    }

    private static List<Piglin> findNearbyPiglins(Level level, BlockPos pos) {
        Vec3 centerPos = Vec3.atCenterOf(pos);
        final int horizontalRange = BarteringStation.CONFIG.server().horizontalRange;
        final int verticalRange = BarteringStation.CONFIG.server().verticalRange;
        return level.getEntitiesOfClass(Piglin.class, new AABB(centerPos.add(-horizontalRange, -verticalRange, -horizontalRange), centerPos.add(horizontalRange, verticalRange, horizontalRange)), AbstractPiglin::isAdult);
    }

    private static void barterWithPiglins(BlockPos pos, BarteringStationBlockEntity blockEntity, List<Piglin> piglins) {
        // stop giving out gold when not slots are available
        if (piglins.isEmpty() || blockEntity.findFreeResponseSlot().isEmpty()) return;
        int currentPiglin = 0;
        for (int i = 0; i < CURRENCY_SLOTS; i++) {
            ItemStack stack = blockEntity.getItem(i);
            if (!stack.isEmpty()) {
                while (currentPiglin < piglins.size()) {
                    if (PiglinAiHelper.mobInteract(piglins.get(currentPiglin++), stack, pos)) {
                        blockEntity.barterDelay = BarteringStation.CONFIG.server().barterDelay;
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_OUTPUT;
        }
        return SLOTS_FOR_INPUT;
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_58996_, ItemStack p_58997_, @Nullable Direction p_58998_) {
        return this.canPlaceItem(p_58996_, p_58997_);
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, ItemStack p_19240_, Direction p_19241_) {
        return true;
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.barteringstation.bartering_station");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new BarteringStationMenu(id, inventory, this, this.dataAccess);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return index >= 0 && index < this.items.size() ? this.items.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int p_58330_, int p_58331_) {
        ItemStack stack = ContainerHelper.removeItem(this.items, p_58330_, p_58331_);
        if (!stack.isEmpty()) this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_58387_) {
        ItemStack stack = ContainerHelper.takeItem(this.items, p_58387_);
        if (!stack.isEmpty()) this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.getContainerSize()) {
            this.items.set(index, stack);
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player p_58340_) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return p_58340_.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        if (index >= 0 && index < CURRENCY_SLOTS) {
            return ModServices.ABSTRACTIONS.isStackPiglinCurrency(stack);
        }
        return false;
    }

    @Override
    public void clearContent() {
        this.items.clear();
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

    private boolean hasSpaceForItem(ItemStack stack1, ItemStack stack2) {
        return !stack1.isEmpty() && ItemStack.isSameItemSameTags(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < this.getMaxStackSize();
    }

    private OptionalInt findFreeResponseSlot() {
        for (int i = CURRENCY_SLOTS; i < this.getContainerSize(); i++) {
            if (this.getItem(i).isEmpty()) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    private void mergeStackToSlot(ItemStack stackToMerge, int targetSlot) {
        ItemStack stackInSlot = this.getItem(targetSlot);
        ItemStack stackToInsert;
        if (stackInSlot.isEmpty()) {
            stackToInsert = stackToMerge.copy();
            stackToMerge.setCount(0);
            if (stackToMerge.hasTag()) {
                stackToInsert.setTag(stackToMerge.getTag().copy());
            }
        } else {
            stackToInsert = stackInSlot.copy();
            int transferAmount = stackToMerge.getCount();
            transferAmount = Math.min(transferAmount, stackToInsert.getMaxStackSize() - stackToInsert.getCount());
            stackToInsert.grow(transferAmount);
            stackToMerge.shrink(transferAmount);
        }
        this.setItem(targetSlot, stackToInsert);
    }
}
