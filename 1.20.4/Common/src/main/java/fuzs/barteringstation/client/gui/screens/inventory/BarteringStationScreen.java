package fuzs.barteringstation.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.client.handler.PiglinHeadModelHandler;
import fuzs.barteringstation.client.init.ModClientRegistry;
import fuzs.barteringstation.config.ClientConfig;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class BarteringStationScreen extends AbstractContainerScreen<BarteringStationMenu> {
    public static final int ARROW_SIZE_X = 24;
    public static final int ARROW_SIZE_Y = 18;
    private static final ResourceLocation BARTERING_STATION_LOCATION = new ResourceLocation(BarteringStation.MOD_ID, "textures/gui/container/bartering_station.png");
    private SkullModelBase skullModel;

    public BarteringStationScreen(BarteringStationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    private static MutableComponent makePiglinComponent(int nearbyPiglins) {
        return Component.literal(String.valueOf(nearbyPiglins)).withStyle(nearbyPiglins > 0 ? ChatFormatting.GOLD : ChatFormatting.RED);
    }

    @Override
    protected void init() {
        super.init();
        this.skullModel = new SkullModel(this.minecraft.getEntityModels().bakeLayer(ModClientRegistry.PIGLIN_HEAD_MODEL_LAYER_LOCATION));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (BarteringStation.CONFIG.get(ClientConfig.class).cooldownRenderType.overlay()) {
            this.renderCooldownOverlays(guiGraphics);
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        int startX = this.leftPos + 53;
        int startY = this.topPos + 20;
        if (startX <= mouseX && startY <= mouseY && mouseX < startX + 16 && mouseY < startY + 16) {
            Component component = Component.translatable("gui.barteringstation.bartering_station.piglins", makePiglinComponent(this.menu.getNearbyPiglins()));
            guiGraphics.renderTooltip(this.font, component, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(BARTERING_STATION_LOCATION, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (BarteringStation.CONFIG.get(ClientConfig.class).cooldownRenderType.arrows()) {
            this.renderBgCooldownArrows(guiGraphics);
        }
        this.renderPiglinHead(53, 20, 150);
        this.decoratePiglinHead(53, 20, 150);
    }

    private void renderPiglinHead(int posX, int posY, int blitOffset) {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.translate(this.leftPos, this.topPos, 0.0);
        PiglinHeadModelHandler.INSTANCE.renderPiglinHeadGuiModel(posX, posY, blitOffset, this.skullModel);
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.enableDepthTest();
    }

    private void decoratePiglinHead(int posX, int posY, int blitOffset) {
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(this.leftPos, this.topPos, blitOffset + 200.0);
        MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        Component component = makePiglinComponent(this.menu.getNearbyPiglins());
        this.font.drawInBatch(component, (float) (posX + 19 - 2 - this.font.width(component)), (float) (posY + 6 + 3), -1, true, posestack.last().pose(), multibuffersource$buffersource, Font.DisplayMode.NORMAL, 0, 15728880);
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }

    private void renderBgCooldownArrows(GuiGraphics guiGraphics) {
        int arrow1Progress = this.menu.getTopArrowProgress();
        guiGraphics.blit(BARTERING_STATION_LOCATION, this.leftPos + 49, this.topPos + 40, 176, 0, arrow1Progress, ARROW_SIZE_Y);
        int arrow2Progress = this.menu.getBottomArrowProgress();
        guiGraphics.blit(BARTERING_STATION_LOCATION, this.leftPos + 49 + ARROW_SIZE_X - arrow2Progress, this.topPos + 53, 176 + ARROW_SIZE_X - arrow2Progress, ARROW_SIZE_Y, arrow2Progress, ARROW_SIZE_Y);
    }

    private void renderCooldownOverlays(GuiGraphics guiGraphics) {
        float cooldownProgress = this.menu.getCooldownProgress();
        if (cooldownProgress > 0.0F && cooldownProgress < 1.0F) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(this.leftPos, this.topPos, 0.0);
            RenderSystem.disableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            for (int i = 0; i < BarteringStationBlockEntity.CURRENCY_SLOTS && i < this.menu.slots.size(); i++) {
                Slot slot = this.menu.slots.get(i);
                if (slot.isActive() && slot.hasItem()) {
                    int startY = Mth.floor(16.0F * (1.0F - cooldownProgress));
                    guiGraphics.fill(RenderType.guiOverlay(), slot.x, slot.y + startY, slot.x + 16, slot.y + startY + Mth.ceil(16.0F * cooldownProgress), -2130706433);
                }
            }
            guiGraphics.pose().popPose();
            RenderSystem.enableDepthTest();
        }
    }
}
