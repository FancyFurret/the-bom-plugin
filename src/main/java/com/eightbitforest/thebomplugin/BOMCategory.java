package com.eightbitforest.thebomplugin;

import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BOMCategory implements IRecipeCategory<BOMWrapper> {
    @Override
    public String getUid() {
        return TheBOMPlugin.uid;
    }

    @Override
    public String getTitle() {
        return "Bill Of Materials";
    }

    @Override
    public String getModName() {
        return "The BOM Plugin";
    }

    @Override
    public IDrawable getBackground() {
        return new IDrawable() {
            @Override
            public int getWidth() {
                return 163;
            }

            @Override
            public int getHeight() {
                return 125;
            }

            @Override
            public void draw(Minecraft minecraft, int i, int i1) {

            }
        };
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, BOMWrapper bomWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
        int currentSlot = 0;

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                int index = x + (y * 9);
                guiItemStacks.init(index, true, x * 18, y * 18);
            }
        }

        List<List<ItemStack>> baseIngredients =
                BOMCalculator.getBaseIngredients(
                        ingredients.getInputs(ItemStack.class),
                        ingredients.getOutputs(ItemStack.class).get(0));

        for (int i = 0; i < baseIngredients.size(); i++) {
            guiItemStacks.set(i, baseIngredients.get(i));
        }
    }
}
