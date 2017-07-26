package com.eightbitforest.thebomplugin;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

import java.util.ArrayList;
import java.util.List;

public class BOMCalculator {
    private BOMCalculator() {}

    public static List<List<ItemStack>> getBaseIngredients(List<List<ItemStack>> inputs) {
        List<List<ItemStack>> baseIngredients = new ArrayList<>();

        getBaseIngredients(baseIngredients, inputs);

        return baseIngredients;
    }

    private static void getBaseIngredients(List<List<ItemStack>> baseIngredients, List<List<ItemStack>> inputs) {
        for (int i = 0; i < inputs.size(); i++) {
            List<ItemStack> currentStack = inputs.get(i);

            if (currentStack.size() == 1) {
                List<List<List<ItemStack>>> recipes = CraftingRecipeChecker.getRecipesForItemStack(currentStack.get(0));
                if (recipes.size() > 0) {
                    getBaseIngredients(baseIngredients, recipes.get(0));
                }
                else {
                    addItemStack(baseIngredients, currentStack);
                }
            }
            else if (currentStack.size() != 0){
                addItemStack(baseIngredients, currentStack);
            }
        }
    }

    private static void addItemStack(List<List<ItemStack>> stacks, List<ItemStack> stackToAdd) {
        for (List<ItemStack> stack : stacks) {
            if (stack.get(0).isItemEqual(stackToAdd.get(0))) {
                for (ItemStack oreDictStack : stack) {
                    oreDictStack.setCount(oreDictStack.getCount() + stackToAdd.get(0).getCount());
                }
                return;
            }
        }

        List<ItemStack> stackToAddCopy = new ArrayList<>(stackToAdd.size());
        for (ItemStack stack : stackToAdd) {
            stackToAddCopy.add(stack.copy());
        }
        stacks.add(stackToAddCopy);
    }
}
