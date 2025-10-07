package fuzs.barteringstation.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fuzs.barteringstation.client.renderer.blockentity.state.BarteringStationRenderState;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Mostly copied from Quark's <a
 * href="https://github.com/VazkiiMods/Quark/blob/master/src/main/java/vazkii/quark/addons/oddities/client/render/be/MatrixEnchantingTableRenderer.java">MatrixEnchantingTableRenderer.java</a>
 * by Vazkii, thanks!
 */
public class BarteringStationRenderer implements BlockEntityRenderer<BarteringStationBlockEntity, BarteringStationRenderState> {
    private final ItemModelResolver itemModelResolver;

    public BarteringStationRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public BarteringStationRenderState createRenderState() {
        return new BarteringStationRenderState();
    }

    @Override
    public void extractRenderState(BarteringStationBlockEntity blockEntity, BarteringStationRenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity,
                renderState,
                partialTick,
                cameraPosition,
                breakProgress);
        renderState.item.clear();
        this.itemModelResolver.updateForTopItem(renderState.item,
                new ItemStack(Items.GOLD_INGOT),
                ItemDisplayContext.FIXED,
                blockEntity.getLevel(),
                null,
                (int) blockEntity.getBlockPos().asLong());
        // light is normally always 0 since it checks inside the crafting table block which is solid, but contents are rendered in the block above
        renderState.itemLightCoords = blockEntity.getLevel() != null ?
                LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above()) : 0XF000F0;
        blockEntity.getAnimationController().extractRenderState(renderState.animationController, partialTick);
    }

    @Override
    public void submit(BarteringStationRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.03125F, 0.5F);
        poseStack.scale(0.8F, 0.8F, 0.8F);
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.animationController.rotation));
        float open = renderState.animationController.open;
        poseStack.translate(0.0F, open, Math.sin(open * Math.PI));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F * (open - 1.0F)));
        float hoverAmount = (float) Math.sin(renderState.animationController.time * 0.06F) * open * 0.2F;
        poseStack.translate(0.0F, hoverAmount, 0.0F);
        renderState.item.submit(poseStack, nodeCollector, renderState.itemLightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }
}
