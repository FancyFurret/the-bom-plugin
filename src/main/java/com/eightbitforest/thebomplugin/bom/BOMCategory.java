package com.eightbitforest.thebomplugin.bom;

import com.eightbitforest.thebomplugin.TheBOMPlugin;
import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import com.eightbitforest.thebomplugin.util.BOMCalculator;
import com.eightbitforest.thebomplugin.util.Resources;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class BOMCategory implements IRecipeCategory<BOMWrapper> {

    private final IDrawable background;

    public BOMCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation(TheBOMPluginMod.MODID, Resources.BOM_BACKGROUND_TEXTURE);
        background = guiHelper.createDrawable(location, 0, 0, 163, 119);
    }

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
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, BOMWrapper bomWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();


        guiItemStacks.init(0, true, 72, 91);
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 9; x++) {
                int index = x + (y * 9);
                guiItemStacks.init(index + 1, true, x * 18, y * 18);
            }
        }



        List<List<ItemStack>> baseIngredients =
                BOMCalculator.getBaseIngredients(
                        ingredients.getInputs(ItemStack.class),
                        ingredients.getOutputs(ItemStack.class).get(0));

        guiItemStacks.set(0, ingredients.getOutputs(ItemStack.class).get(0));
        for (int i = 1; i < baseIngredients.size(); i++) {
            guiItemStacks.set(i, baseIngredients.get(i));
        }
    }
}
