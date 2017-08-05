package com.eightbitforest.thebomplugin.plugin;

import com.eightbitforest.thebomplugin.jei.ingredients.Ingredients;
import com.eightbitforest.thebomplugin.util.BOMCalculator;
import com.eightbitforest.thebomplugin.gui.ItemListGui;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class BOMWrapper implements ICraftingRecipeWrapper {

    private BOMRecipe recipe;
    private IJeiHelpers jeiHelpers;
    private GuiButton hudListButton;

    public BOMWrapper(BOMRecipe recipe, IJeiHelpers helpers) {
        this.recipe = recipe;
        this.jeiHelpers = helpers;
        this.hudListButton = new GuiButton(0, 109, 90, 55, 20, "Track");
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, recipe.inputs);
        ingredients.setOutput(ItemStack.class, recipe.output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (ItemListGui.isGuiOpen()) {
            hudListButton.displayString = "Untrack";
        }
        else {
            hudListButton.displayString = "Track";
        }
        hudListButton.drawButton(minecraft, mouseX, mouseY, 1f);
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (hudListButton.mousePressed(minecraft, mouseX, mouseY)) {
            if (ItemListGui.isGuiOpen()) {
                ItemListGui.dismissItems();
            }
            else {
                Ingredients i = new Ingredients();
                getIngredients(i);
                ItemListGui.showItems(BOMCalculator.getBaseIngredients(i));
            }
            return true;
        }
        return false;
    }
}
