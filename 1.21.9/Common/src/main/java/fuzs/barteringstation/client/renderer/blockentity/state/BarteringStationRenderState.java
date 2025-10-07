package fuzs.barteringstation.client.renderer.blockentity.state;

import fuzs.barteringstation.world.level.block.entity.ItemStationAnimationController;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;

public class BarteringStationRenderState extends BlockEntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public int itemLightCoords;
    public ItemStationAnimationController.RenderState animationController = new ItemStationAnimationController.RenderState();
}
