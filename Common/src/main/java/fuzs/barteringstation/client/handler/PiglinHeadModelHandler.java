package fuzs.barteringstation.client.handler;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import fuzs.barteringstation.BarteringStation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.function.Function;

/**
 * why this quirky way with loading an actual model file just for the transformations? why not lol
 *
 * <p>thanks to henkelmax for the idea from <a href="https://github.com/henkelmax/pipez/blob/master/src/main/java/de/maxhenkel/pipez/ModelRegistry.java">ModelRegistry.java</a>
 */
public class PiglinHeadModelHandler {
    public static final PiglinHeadModelHandler INSTANCE = new PiglinHeadModelHandler();
    public static final ResourceLocation PIGLIN_ITEM_MODEL_LOCATION = new ResourceLocation(BarteringStation.MOD_ID, "item/piglin_head");
    private static final ResourceLocation PIGLIN_ENTITY_TEXTURE_LOCATION = new ResourceLocation("textures/entity/piglin/piglin.png");

    private BakedModel piglinHeadModel;

    public void bakeModel(Function<ResourceLocation, BakedModel> bakery) {
        this.piglinHeadModel = bakery.apply(PIGLIN_ITEM_MODEL_LOCATION);
    }

    public void renderPiglinHeadGuiModel(int posX, int posY, int blitOffset, SkullModelBase skullModel) {
        Objects.requireNonNull(this.piglinHeadModel, "Piglin head model has not been loaded yet");
        RenderType rendertype = RenderType.entityCutoutNoCullZOffset(PIGLIN_ENTITY_TEXTURE_LOCATION);
        PiglinHeadModelHandler.renderItemLikeGuiModel(posX, posY, blitOffset, skullModel, rendertype, this.piglinHeadModel);
    }

    public static void renderItemLikeGuiModel(int posX, int posY, int blitOffset, SkullModelBase skullModel, RenderType renderType, BakedModel bakedModel) {
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(posX, posY, 100.0F + blitOffset);
        posestack.translate(8.0D, 8.0D, 0.0D);
        posestack.scale(1.0F, -1.0F, 1.0F);
        posestack.scale(16.0F, 16.0F, 16.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean flag = !bakedModel.usesBlockLight();
        if (flag) {
            Lighting.setupForFlatItems();
        }

        applyTransformsAndRender(posestack1, skullModel, renderType, bakedModel);
        multibuffersource$buffersource.endBatch();
        RenderSystem.enableDepthTest();
        if (flag) {
            Lighting.setupFor3DItems();
        }

        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    private static void applyTransformsAndRender(PoseStack poseStack, SkullModelBase skullModel, RenderType renderType, BakedModel bakedModel) {
        poseStack.pushPose();
        bakedModel.getTransforms().getTransform(ItemTransforms.TransformType.GUI).apply(false, poseStack);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        renderSkullItemModel(poseStack, skullModel, renderType);
        poseStack.popPose();
    }

    private static void renderSkullItemModel(PoseStack poseStack, SkullModelBase skullModel, RenderType renderType) {
        MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        SkullBlockRenderer.renderSkull(null, -90.0F, 0.0F, poseStack, multibuffersource$buffersource, 15728880, skullModel, renderType);
        multibuffersource$buffersource.endBatch();
    }

    public static LayerDefinition createPiglinHeadLayer(boolean zombified) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -8.0F, -4.0F, 10.0F, 8.0F, 8.0F).texOffs(31, 1).addBox(-2.0F, -4.0F, -5.0F, 4.0F, 4.0F, 1.0F).texOffs(2, 4).addBox(2.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F).texOffs(2, 0).addBox(-3.0F, -2.0F, -5.0F, 1.0F, 2.0F, 1.0F), PartPose.ZERO);
        partdefinition1.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(51, 6).addBox(0.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F), PartPose.offsetAndRotation(4.5F, -6.0F, 0.0F, 0.0F, 0.0F, (-(float)Math.PI / 6F)));
        if (!zombified) {
            partdefinition1.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(39, 6).addBox(-1.0F, 0.0F, -2.0F, 1.0F, 5.0F, 4.0F), PartPose.offsetAndRotation(-4.5F, -6.0F, 0.0F, 0.0F, 0.0F, ((float)Math.PI / 6F)));
        }
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
