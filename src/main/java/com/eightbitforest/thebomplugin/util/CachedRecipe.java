package com.eightbitforest.thebomplugin.util;

import net.minecraft.item.ItemStack;

import java.util.List;

public class CachedRecipe {
    private List<List<ItemStack>> baseIngredients;
    private List<ItemStack> output;
    private List<List<ItemStack>> extraOutputs;

    public List<List<ItemStack>> getBaseIngredients() {
        return Utils.copyItemStackList(baseIngredients);
    }

    public List<ItemStack> getOutput() {
        return output;
    }

    public List<List<ItemStack>> getExtraOutputs() {
        return Utils.copyItemStackList(extraOutputs);
    }

    public CachedRecipe(List<List<ItemStack>> baseIngredients, List<ItemStack> output, List<List<ItemStack>> extraOutputs) {
        this.baseIngredients = Utils.copyItemStackList(baseIngredients);
        this.output = output;
        this.extraOutputs = Utils.copyItemStackList(extraOutputs);
    }
}
