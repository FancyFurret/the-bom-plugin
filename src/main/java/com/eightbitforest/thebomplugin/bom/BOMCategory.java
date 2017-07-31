package com.eightbitforest.thebomplugin.bom;

import com.eightbitforest.thebomplugin.TheBOMPlugin;
import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import com.eightbitforest.thebomplugin.util.BOMCalculator;
import com.eightbitforest.thebomplugin.util.BOMIngredientRenderer;
import com.eightbitforest.thebomplugin.util.ItemStackComparator;
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
    private final ItemStackComparator itemStackComparator;
    private final BOMIngredientRenderer ingredientRenderer;

    public BOMCategory(IGuiHelper guiHelper) {
        ResourceLocation backgroundLocation = new ResourceLocation(TheBOMPluginMod.MODID, Constants.BOM_BACKGROUND_TEXTURE);
        ResourceLocation iconLocation = new ResourceLocation(TheBOMPluginMod.MODID, Constants.BOM_TAB_ICON_TEXTURE);
        background = guiHelper.createDrawable(backgroundLocation, 0, 0, 163, 119);
        icon = guiHelper.createDrawable(iconLocation, 0, 0, 16, 16, 16, 16);
        itemStackComparator = new ItemStackComparator();
        ingredientRenderer = new BOMIngredientRenderer();
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
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();

        // Init gui stacks, 0 being the output
        guiItemStacks.init(0, false, 72, 91);
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                int index = x + (y * 9);
                guiItemStacks.init(index + 1, true, ingredientRenderer, x * 18 + 1, y * 18 + 1, 16, 16, 0, 0);
            }
        }

        // Get base ingredients
        List<List<ItemStack>> baseIngredients =
                BOMCalculator.getBaseIngredients(
                        ingredients.getInputs(ItemStack.class),
                        ingredients.getOutputs(ItemStack.class).get(0));

        // Sort by number of items in each stack
        baseIngredients.sort(itemStackComparator);

        // Fill gui stacks
        guiItemStacks.set(0, ingredients.getOutputs(ItemStack.class).get(0));
        for (int i = 0; i < baseIngredients.size(); i++) {
            guiItemStacks.set(i + 1, baseIngredients.get(i));
        }
    }
}
