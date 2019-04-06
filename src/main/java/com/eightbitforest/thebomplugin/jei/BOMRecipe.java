package com.eightbitforest.thebomplugin.jei;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import javax.annotation.Nonnull;
import java.util.List;

public class BOMRecipe implements IRecipeWrapper {
    private ItemStack output;
    private List<List<ItemStack>> inputs;

    public BOMRecipe(IRecipe recipe, IJeiHelpers helpers) {
        IStackHelper stackHelper = helpers.getStackHelper();

        output = recipe.getRecipeOutput();
        inputs = stackHelper.expandRecipeItemStackInputs(recipe.getIngredients());
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}
