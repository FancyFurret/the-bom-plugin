package com.eightbitforest.thebomplugin;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.ingredients.Ingredients;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CraftingRecipeChecker {
    private CraftingRecipeChecker() {
    }

    public static List<BOMRecipe> getValidRecipes(final IJeiHelpers jeiHelpers) {
        Iterator<IRecipe> recipeIterator = CraftingManager.REGISTRY.iterator();
        List<BOMRecipe> validRecipes = new ArrayList<>();
        while (recipeIterator.hasNext()) {
            IRecipe recipe = recipeIterator.next();
            validRecipes.add(new BOMRecipe(recipe, jeiHelpers));
        }
        return validRecipes;
    }

    @SuppressWarnings("unchecked")
    public static List<IIngredients> getRecipesForItemStack(ItemStack stackIn) {
        IJeiRuntime runtime = TheBOMPlugin.getInstance().getRuntime();
        List<IRecipeWrapper> wrappers = runtime.getRecipeRegistry().getRecipeWrappers(runtime.getRecipeRegistry().getRecipeCategories().get(0),
                runtime.getRecipeRegistry().createFocus(IFocus.Mode.OUTPUT, stackIn));

        List<IIngredients> recipes = new ArrayList<>();
        for (IRecipeWrapper wrapper : wrappers) {
            IIngredients ingredients = new Ingredients();
            wrapper.getIngredients(ingredients);
            recipes.add(ingredients);
        }

        return recipes;
    }

    private static boolean isRecipeBase(List<List<ItemStack>> recipeInput, ItemStack recipeOutput, ItemStack item) {

        if (isBlockRecipe(recipeInput, recipeOutput, item)) {
            return true;
        }
        else if (isNuggetRecipe(recipeInput, recipeOutput, item)) {
            return true;
        }

        return false;
    }

    private static boolean isBlockRecipe(List<List<ItemStack>> recipeInput, ItemStack recipeOutput, ItemStack item) {
        if (recipeInput.get(0).size() == 1) {
            if (recipeInput.get(0).get(0).getUnlocalizedName().toLowerCase().contains("block")) {
                if (recipeOutput.getCount() == 9 || recipeOutput.getCount() == 4) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isNuggetRecipe(List<List<ItemStack>> recipeInput, ItemStack recipeOutput, ItemStack item) {
        if (recipeInput.size() == 9) {
            ItemStack initalItem = recipeInput.get(0).get(0);
            if (initalItem.getUnlocalizedName().toLowerCase().contains("nugget")) {
                for (List<ItemStack> stack : recipeInput) {
                    if (!stack.get(0).isItemEqual(initalItem)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}