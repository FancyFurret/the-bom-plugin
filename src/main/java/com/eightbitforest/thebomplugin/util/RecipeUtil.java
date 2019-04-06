package com.eightbitforest.thebomplugin.util;

import com.eightbitforest.thebomplugin.jei.BOMJeiPlugin;
import com.eightbitforest.thebomplugin.jei.BOMRecipe;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.ingredients.Ingredients;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecipeUtil {
    private RecipeUtil() {
    }

    public static List<BOMRecipe> getValidRecipes(IJeiHelpers jeiHelpers) {
        Iterator<IRecipe> recipeIterator = CraftingManager.REGISTRY.iterator();
        List<BOMRecipe> validRecipes = new ArrayList<>();
        while (recipeIterator.hasNext()) {
            IRecipe recipe = recipeIterator.next();
            validRecipes.add(new BOMRecipe(recipe, jeiHelpers));
        }
        return validRecipes;
    }

    public static List<IIngredients> getRecipesForItemStack(ItemStack stackIn) {
        IRecipeRegistry registry = BOMJeiPlugin.getInstance().getRuntime().getRecipeRegistry();

        //noinspection unchecked
        IRecipeCategory<IRecipeWrapper> craftingCategory = registry.getRecipeCategories().get(0);
        List<IRecipeWrapper> wrappers = registry.getRecipeWrappers(craftingCategory,
                registry.createFocus(IFocus.Mode.OUTPUT, stackIn));

        List<IIngredients> recipes = new ArrayList<>();
        for (IRecipeWrapper wrapper : wrappers) {
            IIngredients ingredients = new Ingredients();
            wrapper.getIngredients(ingredients);
            recipes.add(ingredients);
        }

        return recipes;
    }
}