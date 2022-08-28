package fuzs.barteringstation.data;

import fuzs.barteringstation.init.ModRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(DataGenerator p_125973_) {
        super(p_125973_);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_176532_) {
        ShapedRecipeBuilder.shaped(ModRegistry.BARTERING_STATION_BLOCK.get())
                .define('#', ItemTags.PLANKS)
                .define('@', Items.GOLD_INGOT)
                .pattern("@@")
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(p_176532_);
    }
}
