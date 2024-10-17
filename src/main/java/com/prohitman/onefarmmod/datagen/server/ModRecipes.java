package com.prohitman.onefarmmod.datagen.server;

import com.prohitman.onefarmmod.OneFarmMod;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipes extends RecipeProvider {
    public ModRecipes(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, OneFarmMod.ONE_FARM_BLOCK.get(), 1)
                .requires(Items.REDSTONE, 3)
                .requires(Items.IRON_INGOT, 3)
                .requires(Items.GLASS_PANE)
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, OneFarmMod.BREEDER_BLOCK.get(), 1)
                .requires(Items.REDSTONE, 1)
                .requires(Items.IRON_INGOT, 4)
                .unlockedBy("has_redstone", has(Items.REDSTONE))
                .save(consumer);
    }
}
