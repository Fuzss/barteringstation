package fuzs.barteringstation.world.level.block.entity;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.registry.ModRegistry;
import fuzs.barteringstation.world.entity.monster.piglin.PiglinAiHelper;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

public class BarteringStationBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    public static final int ALL_SLOTS = 21;
    public static final int CURRENCY_SLOTS = 6;
    public static final int DATA_SLOTS = 2;
    public static final int BARTER_COOLDOWN = 80;
    private static final int[] SLOTS_FOR_INPUT = IntStream.range(0, CURRENCY_SLOTS).toArray();
    private static final int[] SLOTS_FOR_OUTPUT = IntStream.range(CURRENCY_SLOTS, ALL_SLOTS).toArray();
    private final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int id) {
            return switch (id) {
                case 0 -> BarteringStationBlockEntity.this.barterCooldown;
                case 1 -> BarteringStationBlockEntity.this.localPiglins;
                default -> 0;
            };
        }

        @Override
        public void set(int id, int data) {
            switch (id) {
                case 0 -> BarteringStationBlockEntity.this.barterCooldown = data;
                case 1 -> BarteringStationBlockEntity.this.localPiglins = data;
            }
        }

        @Override
        public int getCount() {
            return DATA_SLOTS;
        }
    };
    private NonNullList<ItemStack> items = NonNullList.withSize(ALL_SLOTS, ItemStack.EMPTY);
    private int barterCooldown;
    private int localPiglins;
    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

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
        this.barterCooldown = tag.getInt("BarterCooldown");

    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BarterCooldown", this.barterCooldown);
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
        if (blockEntity.barterCooldown > 0) {
            blockEntity.barterCooldown--;
        }
        if (blockEntity.barterCooldown == 0) {
            Vec3 blockCenterPos = Vec3.atCenterOf(pos);
            final int horizontalRange = BarteringStation.CONFIG.server().horizontalRange;
            final int verticalRange = BarteringStation.CONFIG.server().verticalRange;
            List<Piglin> piglins = level.getEntitiesOfClass(Piglin.class, new AABB(blockCenterPos.add(-horizontalRange, -verticalRange, -horizontalRange), blockCenterPos.add(horizontalRange, verticalRange, horizontalRange)), AbstractPiglin::isAdult);
            blockEntity.localPiglins = piglins.size();
            int piglinIndex = 0;
            for (int i = 0; i < CURRENCY_SLOTS; i++) {
                ItemStack stack = blockEntity.getItem(i);
                if (!stack.isEmpty()) {
                    while (true) {
                        boolean outOfBounds = piglinIndex >= piglins.size();
                        if (outOfBounds || PiglinAiHelper.mobInteract(piglins.get(piglinIndex++), stack, blockEntity.worldPosition))
                            if (!outOfBounds) {
                                blockEntity.barterCooldown = BARTER_COOLDOWN;
                            }
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
                }
                continue;
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

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.DOWN) {
                return this.handlers[1].cast();
            } else
                return this.handlers[0].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (LazyOptional<? extends IItemHandler> handler : this.handlers) {
            handler.invalidate();
        }
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);
    }
}
