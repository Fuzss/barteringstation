package fuzs.barteringstation.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

/**
 * required for rendering models outside full blocks as the light color is taken from within the block where it is pitch black
 *
 * <p>copied from {@link net.minecraft.client.renderer.LevelRenderer#getLightColor} to avoid client only class on server
 */
public class BlockLightingUtil {

    public static int getLightColor(BlockAndTintGetter displayReader, BlockPos pos) {
        return getLightColor(displayReader, displayReader.getBlockState(pos), pos);
    }

    public static int getLightColor(BlockAndTintGetter displayReader, BlockState state, BlockPos pos) {
        if (state.emissiveRendering(displayReader, pos)) {
            return 15728880;
        } else {
            int i = displayReader.getBrightness(LightLayer.SKY, pos);
            int j = displayReader.getBrightness(LightLayer.BLOCK, pos);
            int k = state.getLightEmission(displayReader, pos);
            if (j < k) {
                j = k;
            }

            return i << 20 | j << 4;
        }
    }
}
