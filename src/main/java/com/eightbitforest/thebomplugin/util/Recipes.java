package com.eightbitforest.thebomplugin.util;

import com.eightbitforest.thebomplugin.TheBOMPlugin;
import com.eightbitforest.thebomplugin.bom.BOMRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.ingredients.Ingredients;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Recipes {
    private Recipes() {
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
}