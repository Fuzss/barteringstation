package fuzs.barteringstation.client.gui.screens.inventory;

import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.config.ClientConfig;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import fuzs.puzzleslib.api.client.gui.v2.ScreenHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BarteringStationScreen extends AbstractContainerScreen<BarteringStationMenu> {
    public static final String KEY_NEARBY_PIGLINS = "gui.barteringstation.bartering_station.piglins";
    public static final ResourceLocation BARTERING_STATION_LOCATION = BarteringStation.id(
            "textures/gui/container/bartering_station.png");
    public static final int ARROW_SIZE_X = 24;
    public static final int ARROW_SIZE_Y = 18;

    public BarteringStationScreen(BarteringStationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    private static Component getPiglinComponent(int nearbyPiglins) {
        return Component.literal(String.valueOf(nearbyPiglins))
                .withStyle(nearbyPiglins > 0 ? ChatFormatting.GOLD : ChatFormatting.RED);
    }

    private static Component getFullPiglinComponent(int nearbyPiglins) {
        return Component.translatable(KEY_NEARBY_PIGLINS,
                        Component.literal(String.valueOf(nearbyPiglins)),
                        Component.empty().append(EntityType.PIGLIN.getDescription()).withStyle(ChatFormatting.WHITE))
                .withStyle(nearbyPiglins > 0 ? ChatFormatting.GOLD : ChatFormatting.RED);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (BarteringStation.CONFIG.get(ClientConfig.class).cooldownRenderType.overlay()) {
            this.renderCooldownOverlays(guiGraphics);
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (ScreenHelper.isHovering(this.leftPos + 53, this.topPos + 20, 16, 16, mouseX, mouseY)) {
            Component component = getFullPiglinComponent(this.menu.getNearbyPiglins());
            guiGraphics.setTooltipForNextFrame(this.font, component, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                BARTERING_STATION_LOCATION,
                this.leftPos,
                this.topPos,
                0,
                0,
                this.imageWidth,
                this.imageHeight,
                256,
                256);
        if (BarteringStation.CONFIG.get(ClientConfig.class).cooldownRenderType.arrows()) {
            this.renderCooldownArrows(guiGraphics);
        }
        int posX = this.leftPos + 53;
        int posY = this.topPos + 20;
        guiGraphics.renderFakeItem(new ItemStack(Items.PIGLIN_HEAD), posX, posY);
        Component component = getPiglinComponent(this.menu.getNearbyPiglins());
        guiGraphics.drawString(this.font, component, posX + 19 - 2 - this.font.width(component), posY + 6 + 3, -1);
    }

    private void renderCooldownArrows(GuiGraphics guiGraphics) {
        int topArrowProgress = this.menu.getTopArrowProgress();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                BARTERING_STATION_LOCATION,
                this.leftPos + 49,
                this.topPos + 40,
                176,
                0,
                topArrowProgress,
                ARROW_SIZE_Y,
                256,
                256);
        int bottomArrowProgress = this.menu.getBottomArrowProgress();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED,
                BARTERING_STATION_LOCATION,
                this.leftPos + 49 + ARROW_SIZE_X - bottomArrowProgress,
                this.topPos + 53,
                176 + ARROW_SIZE_X - bottomArrowProgress,
                ARROW_SIZE_Y,
                bottomArrowProgress,
                ARROW_SIZE_Y,
                256,
                256);
    }

    private void renderCooldownOverlays(GuiGraphics guiGraphics) {
        float cooldownProgress = this.menu.getCooldownProgress();
        if (cooldownProgress > 0.0F && cooldownProgress < 1.0F) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(this.leftPos, this.topPos);
            for (int i = 0; i < BarteringStationBlockEntity.CURRENCY_SLOTS && i < this.menu.slots.size(); i++) {
                Slot slot = this.menu.slots.get(i);
                if (slot.isActive() && slot.hasItem()) {
                    int startY = Mth.floor(16.0F * (1.0F - cooldownProgress));
                    guiGraphics.fill(RenderPipelines.GUI,
                            slot.x,
                            slot.y + startY,
                            slot.x + 16,
                            slot.y + startY + Mth.ceil(16.0F * cooldownProgress),
                            0X7FFFFFFF);
                }
            }
            guiGraphics.pose().popMatrix();
        }
    }
}
