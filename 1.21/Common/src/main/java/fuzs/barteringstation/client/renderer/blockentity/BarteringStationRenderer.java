package fuzs.barteringstation.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import fuzs.barteringstation.world.level.block.entity.BarteringStationAnimationController;
import fuzs.barteringstation.world.level.block.entity.BarteringStationBlockEntity;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * mostly copied from Quark's matrix enchanting table by Vazkii
 * <a href="https://github.com/Vazkii/Quark/blob/master/src/main/java/vazkii/quark/addons/oddities/client/render/MatrixEnchantingTableTileEntityRenderer.java">MatrixEnchantingTableTileEntityRenderer.java</a>
 */
public class BarteringStationRenderer implements BlockEntityRenderer<BarteringStationBlockEntity> {
    private final ItemRenderer itemRenderer;

    public BarteringStationRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BarteringStationBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        // light is normally always 0 since it checks inside the crafting table block which is solid, but contents are rendered in the block above
        packedLight = blockEntity.getLevel() != null ? LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos().above()) : 15728880;
        BarteringStationAnimationController animationController = blockEntity.getAnimationController();
        float ageInTicks = animationController.time + partialTick;
        float nextRotation = animationController.rot - animationController.oRot;
        while (nextRotation >= Math.PI) {
            nextRotation -= (float) (Math.PI * 2F);
        }
        while (nextRotation < -Math.PI) {
            nextRotation += (float) (Math.PI * 2F);
        }
        float bookRotation = animationController.oRot + nextRotation * partialTick;
        float bookOpen = Mth.lerp(partialTick, animationController.oOpen, animationController.open);
        this.renderItem(new ItemStack(Items.GOLD_INGOT), ageInTicks, bookOpen, bookRotation, poseStack, multiBufferSource, packedLight, packedOverlay, blockEntity.getLevel());
    }

    private void renderItem(ItemStack itemStack, float ageInTicks, float bookOpen, float bookRotation, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay, Level level) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.03125F, 0.5F);
        poseStack.scale(0.8F, 0.8F, 0.8F);
        bookRotation *= -180.0F / (float) Math.PI;
        bookRotation -= 90.0F;
        bookRotation *= bookOpen;
        poseStack.mulPose(Axis.YP.rotationDegrees(bookRotation));
        poseStack.translate(0.0F, bookOpen, Math.sin(bookOpen * Math.PI));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F * (bookOpen - 1.0F)));
        float hoveringHeight = (float) Math.sin(ageInTicks * 0.06F) * bookOpen * 0.2F;
        poseStack.translate(0.0F, hoveringHeight, 0.0F);
        this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, level, 0);
        poseStack.popPose();
    }
}
