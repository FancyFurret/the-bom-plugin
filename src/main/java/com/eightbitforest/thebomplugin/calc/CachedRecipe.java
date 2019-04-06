package com.eightbitforest.thebomplugin.calc;

import com.eightbitforest.thebomplugin.util.ItemStackUtil;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CachedRecipe {
    private IIngredients inputs;
    private List<List<ItemStack>> baseIngredients;
    private List<ItemStack> output;
    private List<List<ItemStack>> extraOutputs;

    public IIngredients getInputs() {
        return inputs;
    }

    public List<List<ItemStack>> getBaseIngredients() {
        return ItemStackUtil.copyItemStackList(baseIngredients);
    }

    public List<ItemStack> getOutput() {
        return output;
    }

    public List<List<ItemStack>> getExtraOutputs() {
        return ItemStackUtil.copyItemStackList(extraOutputs);
    }

    public CachedRecipe(List<List<ItemStack>> baseIngredients, List<ItemStack> output, List<List<ItemStack>> extraOutputs) {
        this.baseIngredients = ItemStackUtil.copyItemStackList(baseIngredients);
        this.output = output;
        this.extraOutputs = ItemStackUtil.copyItemStackList(extraOutputs);
    }

    public CachedRecipe(IIngredients inputs, List<List<ItemStack>> baseIngredients, List<ItemStack> output, List<List<ItemStack>> extraOutputs) {
        this.inputs = inputs;
        this.baseIngredients = baseIngredients;
        this.output = output;
        this.extraOutputs = extraOutputs;
    }
}
