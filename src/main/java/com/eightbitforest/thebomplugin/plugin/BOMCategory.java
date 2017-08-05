package com.eightbitforest.thebomplugin.plugin;

import com.eightbitforest.thebomplugin.TheBOMPlugin;
import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import com.eightbitforest.thebomplugin.util.BOMCalculator;
import com.eightbitforest.thebomplugin.render.BOMIngredientRenderer;
import com.eightbitforest.thebomplugin.util.Constants;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class BOMCategory implements IRecipeCategory<BOMWrapper> {

    private final IDrawable background;
    private final IDrawable icon;
    private final BOMIngredientRenderer ingredientRenderer;

    private List<List<ItemStack>> baseIngredients;
    private IIngredients recipe;
    private int outputAmount;

    private IGuiItemStackGroup guiItemStacks;

    public BOMCategory(IGuiHelper guiHelper) {
        background = TheBOMPlugin.getInstance().getGuiDrawables().getCategoryBackground();
        icon = TheBOMPlugin.getInstance().getGuiDrawables().getCategoryIcon();
        ingredientRenderer = new BOMIngredientRenderer();

        outputAmount = 1;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public String getUid() {
        return TheBOMPlugin.uid;
    }

    @Override
    public String getTitle() {
        return "Bill of Materials";
    }

    @Override
    public String getModName() {
        return "TheBOMPlugin";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, BOMWrapper bomWrapper, IIngredients ingredients) {
        guiItemStacks = iRecipeLayout.getItemStacks();
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

        bomWrapper.fixDecreaseButton();
    }

    private void fillGuiItemStacks() {
        ItemStack output = recipe.getOutputs(ItemStack.class).get(0).get(0).copy();
        output.setCount(output.getCount() * outputAmount);

        // Get base ingredients
        baseIngredients = BOMCalculator.getBaseIngredients(recipe, outputAmount);

        // Fill gui stacks
        guiItemStacks.set(0, output);
        for (int i = 0; i < Math.min(baseIngredients.size(), 6 * 9); i++) {
            guiItemStacks.set(i + 1, baseIngredients.get(i));
        }
    }

    public void increaseOutput() {
        outputAmount++;
        fillGuiItemStacks();
    }

    public void decreaseOutput() {
        if (outputAmount > 1) {
            outputAmount--;
            fillGuiItemStacks();
        }
    }

    public int getOutputAmount() {
        return outputAmount;
    }

    public List<List<ItemStack>> getBaseIngredients() {
        return baseIngredients;
    }
}
