package fuzs.barteringstation.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.client.handler.PiglinHeadModelHandler;
import fuzs.barteringstation.client.init.ModClientRegistry;
import fuzs.barteringstation.config.ClientConfig;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class BarteringStationScreen extends AbstractContainerScreen<BarteringStationMenu> {
    private static final ResourceLocation BARTERING_STATION_LOCATION = new ResourceLocation(BarteringStation.MOD_ID, "textures/gui/container/bartering_station.png");
    private static final ResourceLocation PIGLIN_ENTITY_TEXTURE_LOCATION = new ResourceLocation("textures/entity/piglin/piglin.png");
    public static final int ARROW_SIZE_X = 24;
    public static final int ARROW_SIZE_Y = 18;

    private SkullModelBase skullModel;

    public BarteringStationScreen(BarteringStationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.skullModel = new SkullModel(this.minecraft.getEntityModels().bakeLayer(ModClientRegistry.PIGLIN_HEAD_MODEL_LAYER_LOCATION));
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        if (BarteringStation.CONFIG.get(ClientConfig.class).cooldownRenderType.overlay()) {
            this.renderCooldownOverlays();
        }
        this.renderTooltip(poseStack, mouseX, mouseY);
        int startX = this.leftPos + 53;
        int startY = this.topPos + 20;
        if (startX <= mouseX && startY <= mouseY && mouseX < startX + 16 && mouseY < startY + 16) {
            Component component = Component.translatable("gui.barteringstation.bartering_station.piglins", makePiglinComponent(this.menu.getNearbyPiglins()));
            this.renderTooltip(poseStack, component, mouseX, mouseY);
        }
    }

    private static MutableComponent makePiglinComponent(int nearbyPiglins) {
        return Component.literal(String.valueOf(nearbyPiglins)).withStyle(nearbyPiglins > 0 ? ChatFormatting.GOLD : ChatFormatting.RED);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BARTERING_STATION_LOCATION);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (BarteringStation.CONFIG.get(ClientConfig.class).cooldownRenderType.arrows()) {
            this.renderBgCooldownArrows(poseStack);
        }
        this.renderPiglinHead(53, 20, 150);
        this.decoratePiglinHead(53, 20, 150);
    }

    private void renderPiglinHead(int posX, int posY, int blitOffset) {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.translate(this.leftPos, this.topPos, 0.0);
        RenderType rendertype = RenderType.entityCutoutNoCullZOffset(PIGLIN_ENTITY_TEXTURE_LOCATION);
        PiglinHeadModelHandler.renderItemLikeGuiModel(posX, posY, blitOffset, this.skullModel, rendertype, PiglinHeadModelHandler.INSTANCE.getPiglinHeadModel());
        modelViewStack.popPose();
    }

    private void decoratePiglinHead(int posX, int posY, int blitOffset) {
        PoseStack posestack = new PoseStack();
        Component component = makePiglinComponent(this.menu.getNearbyPiglins());
        posestack.translate(0.0, 0.0, blitOffset + 200.0);
        MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        this.font.drawInBatch(component, (float) (posX + 19 - 2 - this.font.width(component)), (float) (posY + 6 + 3), -1, true, posestack.last().pose(), multibuffersource$buffersource, false, 0, 15728880);
        multibuffersource$buffersource.endBatch();
    }

    private void renderBgCooldownArrows(PoseStack poseStack) {
        int arrow1Progress = this.menu.getTopArrowProgress();
        this.blit(poseStack, this.leftPos + 49, this.topPos + 40, 176, 0, arrow1Progress, ARROW_SIZE_Y);
        int arrow2Progress = this.menu.getBottomArrowProgress();
        this.blit(poseStack, this.leftPos + 49 + ARROW_SIZE_X - arrow2Progress, this.topPos + 53, 176 + ARROW_SIZE_X - arrow2Progress, ARROW_SIZE_Y, arrow2Progress, ARROW_SIZE_Y);
    }

    private void renderCooldownOverlays() {
        float cooldownProgress = this.menu.getCooldownProgress();
        if (cooldownProgress > 0.0F && cooldownProgress < 1.0F) {
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.translate(this.leftPos, this.topPos, 0.0);
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            for (int i = 0; i < BarteringStationBlockEntity.CURRENCY_SLOTS && i < this.menu.slots.size(); i++) {
                Slot slot = this.menu.slots.get(i);
                if (slot.isActive() && slot.hasItem()) {
                    this.renderSlotCooldownOverlay(slot.x, slot.y, cooldownProgress);
                }
            }
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.enableDepthTest();
        }
    }

    private void renderSlotCooldownOverlay(int posX, int posY, float cooldownProgress) {
        if (cooldownProgress > 0.0F) {
            this.setBlitOffset(100);
            this.itemRenderer.blitOffset = 100.0F;
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Tesselator tesselator1 = Tesselator.getInstance();
            BufferBuilder bufferbuilder1 = tesselator1.getBuilder();
            this.fillRect(bufferbuilder1, posX, posY + Mth.floor(16.0F * (1.0F - cooldownProgress)), 16, Mth.ceil(16.0F * cooldownProgress), 255, 255, 255, 127);
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();
            this.itemRenderer.blitOffset = 0.0F;
            this.setBlitOffset(0);
        }
    }

    /**
     * private method copied from {@link net.minecraft.client.renderer.entity.ItemRenderer}
     */
    private void fillRect(BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        renderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        renderer.vertex(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        renderer.vertex(x, y + height, 0.0).color(red, green, blue, alpha).endVertex();
        renderer.vertex(x + width, y + height, 0.0).color(red, green, blue, alpha).endVertex();
        renderer.vertex(x + width, y, 0.0).color(red, green, blue, alpha).endVertex();
        BufferUploader.drawWithShader(renderer.end());
    }
}
