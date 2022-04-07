package fuzs.barteringstation.client.gui.screens.inventory;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.barteringstation.BarteringStation;
import fuzs.barteringstation.world.inventory.BarteringStationMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BarteringStationScreen extends AbstractContainerScreen<BarteringStationMenu> {
    private static final ResourceLocation BARTERING_STATION_LOCATION = new ResourceLocation(BarteringStation.MOD_ID, "textures/gui/container/bartering_station.png");

    public BarteringStationScreen(BarteringStationMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void render(PoseStack p_97858_, int p_97859_, int p_97860_, float p_97861_) {
        this.renderBackground(p_97858_);
        super.render(p_97858_, p_97859_, p_97860_, p_97861_);
        this.renderTooltip(p_97858_, p_97859_, p_97860_);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BARTERING_STATION_LOCATION);
        int leftPos = this.leftPos;
        int topPos = this.topPos;
        this.blit(poseStack, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        this.blit(poseStack, leftPos + 49, topPos + 40, 176, 0, (int) (this.menu.getArrow1Progress() * 22.0F), 18);
        this.blit(poseStack, leftPos + 49, topPos + 53, 176 + (int) (this.menu.getArrow2Progress() * 22.0F), 18, (int) ((1.0F - this.menu.getArrow2Progress()) * 22.0F), 18);
    }
}
