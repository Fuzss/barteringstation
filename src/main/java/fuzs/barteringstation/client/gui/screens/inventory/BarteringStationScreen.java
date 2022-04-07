package fuzs.barteringstation.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class BarteringStationScreen extends AbstractContainerScreen<BarteringStationMenu> {
    private static final ResourceLocation BARTERING_STATION_LOCATION = new ResourceLocation(BarteringStation.MOD_ID, "textures/gui/container/bartering_station.png");
    public static final int ARROW_SIZE_X = 24;
    public static final int ARROW_SIZE_Y = 18;

    public BarteringStationScreen(BarteringStationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void render(PoseStack p_97858_, int p_97859_, int p_97860_, float p_97861_) {
        this.renderBackground(p_97858_);
        super.render(p_97858_, p_97859_, p_97860_, p_97861_);
        if (BarteringStation.CONFIG.client().cooldownRenderType.overlay()) {
            this.renderCooldownOverlays();
        }
        this.renderTooltip(p_97858_, p_97859_, p_97860_);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BARTERING_STATION_LOCATION);
        this.blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (BarteringStation.CONFIG.client().cooldownRenderType.arrows()) {
            this.renderBgCooldownArrows(poseStack);
        }
    }

    private void renderBgCooldownArrows(PoseStack poseStack) {
        int arrow1Progress = this.menu.getArrow1Progress();
        this.blit(poseStack, this.leftPos + 49, this.topPos + 40, 176, 0, arrow1Progress, ARROW_SIZE_Y);
        int arrow2Progress = this.menu.getArrow2Progress();
        this.blit(poseStack, this.leftPos + 49 + ARROW_SIZE_X - arrow2Progress, this.topPos + 53, 176 + ARROW_SIZE_X - arrow2Progress, ARROW_SIZE_Y, arrow2Progress, ARROW_SIZE_Y);
    }

    private void renderCooldownOverlays() {
        float cooldownProgress = this.menu.getCooldownProgress();
        if (cooldownProgress > 0.0F) {
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

    private void fillRect(BufferBuilder p_115153_, int p_115154_, int p_115155_, int p_115156_, int p_115157_, int p_115158_, int p_115159_, int p_115160_, int p_115161_) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        p_115153_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        p_115153_.vertex((double)(p_115154_ + 0), (double)(p_115155_ + 0), 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        p_115153_.vertex((double)(p_115154_ + 0), (double)(p_115155_ + p_115157_), 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        p_115153_.vertex((double)(p_115154_ + p_115156_), (double)(p_115155_ + p_115157_), 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        p_115153_.vertex((double)(p_115154_ + p_115156_), (double)(p_115155_ + 0), 0.0D).color(p_115158_, p_115159_, p_115160_, p_115161_).endVertex();
        p_115153_.end();
        BufferUploader.end(p_115153_);
    }
}
