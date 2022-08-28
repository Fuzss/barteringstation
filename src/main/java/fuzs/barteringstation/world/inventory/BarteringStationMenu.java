package fuzs.barteringstation.world.inventory;

import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

public class BarteringStationMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;

    public BarteringStationMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(BarteringStationBlockEntity.ALL_SLOTS), new SimpleContainerData(BarteringStationBlockEntity.DATA_SLOTS));
    }

    public BarteringStationMenu(int containerId, Inventory inventory, Container container, ContainerData containerData) {
        super(ModRegistry.BARTERING_STATION_MENU_TYPE.get(), containerId);
        checkContainerSize(container, BarteringStationBlockEntity.ALL_SLOTS);
        checkContainerDataCount(containerData, BarteringStationBlockEntity.DATA_SLOTS);
        this.container = container;
        this.data = containerData;
        for (int i = 0; i < BarteringStationBlockEntity.CURRENCY_SLOTS; i++) {
            this.addSlot(new Slot(container, i, 11 + (i % 2) * 18, 17 + (i / 2) * 18) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return stack.isPiglinCurrency();
                }
            });
        }
        for (int i = BarteringStationBlockEntity.CURRENCY_SLOTS; i < BarteringStationBlockEntity.ALL_SLOTS; i++) {
            this.addSlot(new Slot(container, i, 77 + ((i - BarteringStationBlockEntity.CURRENCY_SLOTS) % 5) * 18, 17 + ((i - BarteringStationBlockEntity.CURRENCY_SLOTS) / 5) * 18) {
                @Override
                public boolean mayPlace(ItemStack p_40231_) {
                    return false;
                }
            });
        }
        for (int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
        this.addDataSlots(containerData);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index >= BarteringStationBlockEntity.CURRENCY_SLOTS && index < BarteringStationBlockEntity.ALL_SLOTS) {
                if (!this.moveItemStackTo(itemstack1, BarteringStationBlockEntity.ALL_SLOTS, BarteringStationBlockEntity.ALL_SLOTS + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (index >= BarteringStationBlockEntity.ALL_SLOTS) {
                if (itemstack1.isPiglinCurrency()) {
                    if (!this.moveItemStackTo(itemstack1, 0, BarteringStationBlockEntity.CURRENCY_SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < BarteringStationBlockEntity.ALL_SLOTS + 27) {
                    if (!this.moveItemStackTo(itemstack1, BarteringStationBlockEntity.ALL_SLOTS + 27, BarteringStationBlockEntity.ALL_SLOTS + 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < BarteringStationBlockEntity.ALL_SLOTS + 36) {
                    if (!this.moveItemStackTo(itemstack1, BarteringStationBlockEntity.ALL_SLOTS, BarteringStationBlockEntity.ALL_SLOTS + 27, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.moveItemStackTo(itemstack1, BarteringStationBlockEntity.ALL_SLOTS, BarteringStationBlockEntity.ALL_SLOTS + 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    public int getArrow1Progress() {
        return Mth.clamp((this.data.get(0) * 2 * BarteringStationScreen.ARROW_SIZE_X) / BarteringStationBlockEntity.BARTER_COOLDOWN, 0, BarteringStationScreen.ARROW_SIZE_X);
    }

    public int getArrow2Progress() {
        return Mth.clamp((this.data.get(0) * 2 * BarteringStationScreen.ARROW_SIZE_X) / BarteringStationBlockEntity.BARTER_COOLDOWN - BarteringStationScreen.ARROW_SIZE_X, 0, BarteringStationScreen.ARROW_SIZE_X);
    }

    public float getCooldownProgress() {
        int cooldown = this.data.get(0);
        if (cooldown == -1) {
            return -1;
        }
        return (BarteringStationBlockEntity.BARTER_COOLDOWN - this.data.get(0)) / (float) BarteringStationBlockEntity.BARTER_COOLDOWN;
    }

    public int getLocalPiglins() {
        return this.data.get(1);
    }
}
