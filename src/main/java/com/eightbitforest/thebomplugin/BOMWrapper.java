package com.eightbitforest.thebomplugin;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.awt.*;
import java.util.List;


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
//        minecraft.fontRenderer.drawString(recipe.inputs.get(0).size() + " recipe(s).", 0, 10, Color.black.getRGB());
//        minecraft.fontRenderer.drawString("1. " + recipe.inputs.get(0).get(0).getDisplayName(), 0, 20, Color.black.getRGB());
//        GuiButton b = new GuiButton(0, 0, 0, 100, 100, "This is a test button");
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
