package com.eightbitforest.thebomplugin.bom;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;


public class BOMWrapper implements ICraftingRecipeWrapper {

    BOMRecipe recipe;
    IJeiHelpers jeiHelpers;

    public BOMWrapper(BOMRecipe recipe, IJeiHelpers helpers) {
        this.recipe = recipe;
        this.jeiHelpers = helpers;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, recipe.inputs);
        ingredients.setOutput(ItemStack.class, recipe.output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
