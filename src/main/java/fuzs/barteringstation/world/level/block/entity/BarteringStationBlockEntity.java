package fuzs.barteringstation.world.level.block.entity;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

public class BarteringStationBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int TOTAL_SLOTS = 21;
    private static final int CURRENCY_SLOTS = 6;
    private static final int BARTERING_COOLDOWN = 40;
    private static final int[] SLOTS_FOR_INPUT = IntStream.range(0, CURRENCY_SLOTS).toArray();
    private static final int[] SLOTS_FOR_OUTPUT = IntStream.range(CURRENCY_SLOTS, TOTAL_SLOTS).toArray();

    private final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int id) {
            return id == 0 ? BarteringStationBlockEntity.this.cooldown : 0;
        }

        @Override
        public void set(int id, int data) {
            if (id == 0) {
                BarteringStationBlockEntity.this.cooldown = data;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    private NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    private int cooldown;

    public int time;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    private float tRot;

    public BarteringStationBlockEntity(BlockPos p_155077_, BlockState p_155078_) {
        super(ModRegistry.BARTERING_STATION_BLOCK_ENTITY_TYPE.get(), p_155077_, p_155078_);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        this.cooldown = tag.getInt("BarterCooldown");

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BarterCooldown", this.cooldown);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, BarteringStationBlockEntity blockEntity) {
        if (level == null || !level.isClientSide) return;
        blockEntity.oOpen = blockEntity.open;
        blockEntity.oRot = blockEntity.rot;
        Player playerentity = level.getNearestPlayer((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, 3.0, false);
        if (playerentity != null) {
            double d0 = playerentity.getX() - ((double) pos.getX() + 0.5);
            double d1 = playerentity.getZ() - ((double) pos.getZ() + 0.5);
            blockEntity.tRot = (float) Mth.atan2(d1, d0);
            blockEntity.open += 0.1F;
        } else {
            blockEntity.tRot += 0.02F;
            blockEntity.open -= 0.1F;
        }
        while(blockEntity.rot >= (float) Math.PI) {
            blockEntity.rot -= ((float) Math.PI * 2.0F);
        }
        while(blockEntity.rot < -(float) Math.PI) {
            blockEntity.rot += ((float) Math.PI * 2.0F);
        }
        while(blockEntity.tRot >= (float) Math.PI) {
            blockEntity.tRot -= ((float) Math.PI * 2.0F);
        }
        while(blockEntity.tRot < -(float) Math.PI) {
            blockEntity.tRot += ((float) Math.PI * 2.0F);
        }
        float f2;
        f2 = blockEntity.tRot - blockEntity.rot;
        while (f2 >= (float) Math.PI) {
            f2 -= ((float) Math.PI * 2.0F);
        }
        while(f2 < -(float) Math.PI) {
            f2 += ((float) Math.PI * 2.0F);
        }
        blockEntity.rot += f2 * 0.4F;
        blockEntity.open = Mth.clamp(blockEntity.open, 0.0F, 1.0F);
        ++blockEntity.time;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BarteringStationBlockEntity blockEntity) {
        if (level.getGameTime() % BARTERING_COOLDOWN == 0) {
            Vec3 blockCenterPos = Vec3.atCenterOf(pos);
            final int horizontalRange = BarteringStation.CONFIG.server().horizontalRange;
            final int verticalRange = BarteringStation.CONFIG.server().verticalRange;
            List<Piglin> piglins = level.getEntitiesOfClass(Piglin.class, new AABB(blockCenterPos.add(-horizontalRange, -verticalRange, -horizontalRange), blockCenterPos.add(horizontalRange, verticalRange, horizontalRange)), AbstractPiglin::isAdult);
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
        return null;
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new BrewingStandMenu(id, inventory, this, this.dataAccess);
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
        return ContainerHelper.removeItem(this.items, p_58330_, p_58331_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_58387_) {
        return ContainerHelper.takeItem(this.items, p_58387_);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index >= 0 && index < this.getContainerSize()) {
            this.items.set(index, stack);
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
            return stack.isPiglinCurrency();
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
                int slot = -1;
                slot = this.getSlotWithRemainingSpace(stack, slot);
                if (slot == -1) {
                    slot = this.getFreeSlot(slot);
                }
                if (slot != -1) {
                    this.transferStackInSlot(stack, slot);
                    if (stack.isEmpty()) {
                        return true;
                    }
                    continue;
                }
            }
            return false;
        }
    }

    private int getSlotWithRemainingSpace(ItemStack stack, int slot) {
        for (int i = CURRENCY_SLOTS; i < this.getContainerSize(); i++) {
            if (this.hasRemainingSpaceForItem(this.getItem(i), stack)) {
                slot = i;
            }
        }
        return slot;
    }

    private boolean hasRemainingSpaceForItem(ItemStack stack1, ItemStack stack2) {
        return !stack1.isEmpty() && ItemStack.isSameItemSameTags(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < this.getMaxStackSize();
    }

    private int getFreeSlot(int slot) {
        for (int i = CURRENCY_SLOTS; i < this.getContainerSize(); i++) {
            if (this.getItem(i).isEmpty()) {
                slot = i;
            }
        }
        return slot;
    }

    private void transferStackInSlot(ItemStack stack, int slot) {
        ItemStack stackInSlot = this.getItem(slot).copy();
        int transferAmount = stack.getMaxStackSize() - stackInSlot.getCount();
        stackInSlot.grow(transferAmount);
        stack.shrink(transferAmount);
    }
}
