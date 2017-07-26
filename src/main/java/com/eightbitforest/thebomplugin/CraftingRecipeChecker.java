package com.eightbitforest.thebomplugin;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
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

    public static List<List<List<ItemStack>>> getRecipesForItemStack(ItemStack stackIn) {
        Iterator<IRecipe> recipeIterator = CraftingManager.REGISTRY.iterator();
        List<List<List<ItemStack>>> recipes = new ArrayList<>();
        while (recipeIterator.hasNext()) {
            IRecipe recipe = recipeIterator.next();
            // FIXME:
            if (stackIn.isItemEqual(recipe.getRecipeOutput())) {
                List<List<ItemStack>> recipeList = new ArrayList<>();
                for (Ingredient ingredient : recipe.getIngredients()) {
                    List<ItemStack> ingredients = Arrays.asList(ingredient.getMatchingStacks());
                    recipeList.add(ingredients);
                }
                if (!isBlockRecipe(recipeList, recipe.getRecipeOutput(), stackIn) && (!isNuggetRecipe(recipeList, recipe.getRecipeOutput(), stackIn)))
                    recipes.add(recipeList);
            }
        }
        return recipes;
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