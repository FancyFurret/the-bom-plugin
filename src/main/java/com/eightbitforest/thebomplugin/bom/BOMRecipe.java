package com.eightbitforest.thebomplugin.bom;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class BOMRecipe implements IRecipeWrapper {

    IRecipe recipe;
    ItemStack output;
    List<List<ItemStack>> inputs;

    public BOMRecipe(IRecipe recipe, IJeiHelpers helpers) {
        this.recipe = recipe;

        output = recipe.getRecipeOutput();
        IStackHelper stackHelper = helpers.getStackHelper();

        try {
            inputs = stackHelper.expandRecipeItemStackInputs(recipe.getIngredients());
        } catch (RuntimeException e) {

        }
    }

    @Override
    public void getIngredients(IIngredients iIngredients) {

    }
}
