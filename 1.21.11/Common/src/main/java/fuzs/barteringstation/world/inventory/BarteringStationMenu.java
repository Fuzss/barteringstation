package fuzs.barteringstation.world.inventory;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.client.gui.screens.inventory.BarteringStationScreen;
import fuzs.barteringstation.config.ServerConfig;
import fuzs.barteringstation.init.ModRegistry;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.api.container.v1.QuickMoveRuleSet;
import fuzs.puzzleslib.api.util.v1.EntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.level.Level;

public class BarteringStationMenu extends AbstractContainerMenu {
    private final Container container;
    private final ContainerData data;
    private final ContainerLevelAccess access;

    public BarteringStationMenu(int containerId, Inventory inventory) {
        this(containerId,
                inventory,
                new SimpleContainer(BarteringStationBlockEntity.ALL_SLOTS),
                new SimpleContainerData(BarteringStationBlockEntity.DATA_SLOTS),
                ContainerLevelAccess.NULL);
    }

    public BarteringStationMenu(int containerId, Inventory inventory, Container container, ContainerData containerData, ContainerLevelAccess containerLevelAccess) {
        super(ModRegistry.BARTERING_STATION_MENU_TYPE.value(), containerId);
        checkContainerSize(container, BarteringStationBlockEntity.ALL_SLOTS);
        checkContainerDataCount(containerData, BarteringStationBlockEntity.DATA_SLOTS);
        this.container = container;
        this.data = containerData;
        this.access = containerLevelAccess;
        this.addContainerSlots(container);
        this.addStandardInventorySlots(inventory, 8, 84);
        this.addDataSlots(containerData);
    }

    private void addContainerSlots(Container container) {
        for (int i = 0; i < BarteringStationBlockEntity.CURRENCY_SLOTS; i++) {
            this.addSlot(new Slot(container, i, 11 + (i % 2) * 18, 17 + (i / 2) * 18) {
                @Override
                public boolean mayPlace(ItemStack itemStack) {
                    return EntityHelper.isPiglinCurrency(itemStack);
                }

                @Override
                public Identifier getNoItemIcon() {
                    return SmithingTemplateItem.EMPTY_SLOT_INGOT;
                }
            });
        }

        for (int i = BarteringStationBlockEntity.CURRENCY_SLOTS; i < BarteringStationBlockEntity.ALL_SLOTS; i++) {
            this.addSlot(new Slot(container,
                    i,
                    77 + ((i - BarteringStationBlockEntity.CURRENCY_SLOTS) % 5) * 18,
                    17 + ((i - BarteringStationBlockEntity.CURRENCY_SLOTS) / 5) * 18) {

                @Override
                public boolean mayPlace(ItemStack itemStack) {
                    return false;
                }
            });
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            this.access.execute((Level level, BlockPos blockPos) -> {
                for (Piglin piglin : BarteringStationBlockEntity.findNearbyPiglins(level, blockPos)) {
                    piglin.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60));
                }

                level.playSound(null, blockPos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
            });
            return true;
        } else {
            return super.clickMenuButton(player, id);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return QuickMoveRuleSet.of(this, this::moveItemStackTo, QuickMoveRuleSet.Type.LENIENT)
                .addContainerSlotRule(BarteringStationBlockEntity.SLOTS_FOR_INPUT)
                .addInventoryRules()
                .addInventoryCompartmentRules()
                .quickMoveStack(player, index);
    }

    public int getTopArrowProgress() {
        return ((BarteringStation.CONFIG.get(ServerConfig.class).barterDelay
                - this.data.get(BarteringStationBlockEntity.BARTER_DELAY_DATA_INDEX)) * 2
                * BarteringStationScreen.ARROW_SIZE_X) / BarteringStation.CONFIG.get(ServerConfig.class).barterDelay;
    }

    public int getBottomArrowProgress() {
        return ((BarteringStation.CONFIG.get(ServerConfig.class).barterDelay
                - this.data.get(BarteringStationBlockEntity.BARTER_DELAY_DATA_INDEX)) * 2
                * BarteringStationScreen.ARROW_SIZE_X) / BarteringStation.CONFIG.get(ServerConfig.class).barterDelay
                - BarteringStationScreen.ARROW_SIZE_X;
    }

    public float getCooldownProgress() {
        return this.data.get(BarteringStationBlockEntity.BARTER_DELAY_DATA_INDEX) / (float) BarteringStation.CONFIG.get(
                ServerConfig.class).barterDelay;
    }

    public int getNearbyPiglins() {
        return this.data.get(BarteringStationBlockEntity.NEARBY_PIGLINS_DATA_INDEX);
    }
}
