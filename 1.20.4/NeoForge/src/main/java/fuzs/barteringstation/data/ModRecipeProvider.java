package fuzs.barteringstation.data;

import fuzs.barteringstation.init.ModRegistry;
import fuzs.puzzleslib.api.data.v1.AbstractRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends AbstractRecipeProvider {

    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> exporter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModRegistry.BARTERING_STATION_BLOCK.get())
                .define('#', ItemTags.PLANKS)
                .define('@', Items.GOLD_INGOT)
                .pattern("@@")
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(exporter);
    }
}
