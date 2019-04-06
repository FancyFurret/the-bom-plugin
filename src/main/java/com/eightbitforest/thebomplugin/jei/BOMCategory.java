package com.eightbitforest.thebomplugin.jei;

import com.eightbitforest.thebomplugin.render.BOMIngredientRenderer;
import com.eightbitforest.thebomplugin.calc.BOMCalculator;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BOMCategory implements IRecipeCategory<BOMWrapper> {
    private final IDrawable background;
    private final IDrawable icon;
    private final BOMIngredientRenderer ingredientRenderer;

    private List<List<ItemStack>> baseIngredients;
    private IIngredients recipe;

    private ItemStack output;
    private int outputAmount;

    private IGuiItemStackGroup guiItemStacks;

    private BOMCalculator calculator;

    BOMCategory() {
        background = BOMJeiPlugin.getInstance().getGuiDrawables().getCategoryBackground();
        icon = BOMJeiPlugin.getInstance().getGuiDrawables().getCategoryIcon();
        ingredientRenderer = new BOMIngredientRenderer();

        outputAmount = 1;

        calculator = new BOMCalculator();
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Nonnull
    @Override
    public String getUid() {
        return BOMJeiPlugin.UID;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return "Bill of Materials";
    }

    @Nonnull
    @Override
    public String getModName() {
        return "The BOM Plugin";
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, @Nonnull BOMWrapper bomWrapper, @Nonnull IIngredients ingredients) {
        guiItemStacks = recipeLayout.getItemStacks();
        outputAmount = 1;
        recipe = ingredients;

        // Init gui stacks, 0 being the output
        guiItemStacks.init(0, false, 72, 91);
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                int index = x + (y * 9);
                guiItemStacks.init(index + 1, true, ingredientRenderer, x * 18 + 1, y * 18 + 1, 16, 16, 0, 0);
            }
        }

        fillGuiItemStacks();

        bomWrapper.updateDecreaseButton();
    }

    private void fillGuiItemStacks() {
        this.output = recipe.getOutputs(VanillaTypes.ITEM).get(0).get(0);
        ItemStack output = this.output.copy();
        output.setCount(output.getCount() * outputAmount);

        // Get base ingredients
        baseIngredients = calculator.getBaseIngredients(recipe, outputAmount);

        // Fill gui stacks
        guiItemStacks.set(0, output);
        for (int i = 0; i < Math.min(baseIngredients.size(), 6 * 9); i++)
            guiItemStacks.set(i + 1, baseIngredients.get(i));
    }

    void increaseOutput(boolean byStack) {
        if (byStack) {
            int stackAmount = output.getMaxStackSize() / output.getCount();
            outputAmount += stackAmount;
            outputAmount = stackAmount * Math.round((float)(outputAmount) / stackAmount);
        }
        else {
            outputAmount++;
        }

        fillGuiItemStacks();
    }

    void decreaseOutput(boolean byStack) {
        if (outputAmount <= 1)
            return;

        if (byStack) {
            int stackAmount = output.getMaxStackSize() / output.getCount();
            outputAmount -= stackAmount;
            outputAmount = stackAmount * Math.round((float) (outputAmount) / stackAmount);
            if (outputAmount <= 0)
                outputAmount = 1;
        } else {
            outputAmount--;
        }

        fillGuiItemStacks();
    }

    int getOutputAmount() {
        return outputAmount;
    }

    List<List<ItemStack>> getBaseIngredients() {
        return baseIngredients;
    }
}
