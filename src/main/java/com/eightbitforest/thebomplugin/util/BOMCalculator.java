package com.eightbitforest.thebomplugin.util;

import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;

import java.util.*;

public class BOMCalculator {
    private BOMCalculator() {}



    public static List<List<ItemStack>> getBaseIngredients(List<List<ItemStack>> recipe, List<ItemStack> stack) {
        List<List<ItemStack>> baseIngredients = new ArrayList<>();
        List<List<ItemStack>> extraOutputs = new ArrayList<>();

        for (List<ItemStack> recipeItem : recipe) {
            List<List<ItemStack>> seenItems = new ArrayList<>(Arrays.asList(stack));
            addToIngredients(baseIngredients, getBaseIngredientsForItem(recipeItem, seenItems, extraOutputs));
        }

        return baseIngredients;
    }

    private static List<List<ItemStack>> getBaseIngredientsForItem(List<ItemStack> stack, List<List<ItemStack>> seenItems, List<List<ItemStack>> extraOutputs) {
        List<List<ItemStack>> baseIngredients = new ArrayList<>();

        // Make sure stack isn't empty
        if (stack.size() == 0) {
            return baseIngredients;
        }

        // See if we have an extra output for recipeItem
        if (checkForExtraItem(extraOutputs, stack)) {
            return baseIngredients;
        }

        // Don't recurse too much
        if (seenItems.size() > 512) {
            addItemStack(baseIngredients, stack);
            return baseIngredients;
        }

        // Make sure we don't have a circular recipe
        for (List<ItemStack> item : seenItems) {
            if (item.equals(stack)) {
                addItemStack(baseIngredients, stack);
                return baseIngredients;
            }
        }

        // Make sure this item isn't a base item
        if (isBaseItem(stack)) {
            addItemStack(baseIngredients, stack);
            return baseIngredients;
        }

        // Get matching recipes
        List<IIngredients> recipes = Recipes.getRecipesForItemStack(stack.get(0));
        IIngredients chosenRecipe;

        // Pick recipe that doesn't include blacklisted items TODO: Pick best recipe
        chosenRecipe = pickBestRecipe(recipes);

        // Make sure that there is a good recipe
        if (chosenRecipe == null) {
            addItemStack(baseIngredients, stack);
            return baseIngredients;
        }

        // Make sure recipe doesn't need something we already saw TODO: Combine with pickBestRecipe
        for (List<ItemStack> item : seenItems) {
            for (List<ItemStack> recipeItem : chosenRecipe.getInputs(ItemStack.class)) {
                if (recipeItem.size() != 0 && areItemStacksEqualIgnoreSize(recipeItem.get(0), item.get(0))) {
                    addItemStack(baseIngredients, item);
                    return baseIngredients;
                }
            }
        }

        // See if we have extra outputs
        List<ItemStack> recipeOutput = chosenRecipe.getOutputs(ItemStack.class).get(0);
        if (recipeOutput.get(0).getCount() > 1) {
            List<ItemStack> extraStack = new ArrayList<>(Arrays.asList(recipeOutput.get(0).copy()));
            decreaseStackCount(extraStack);
            addToIngredients(extraOutputs, new ArrayList<>(Arrays.asList(extraStack)));
        }

        // Add recipe components to ingredients
        for (List<ItemStack> recipeItem : chosenRecipe.getInputs(ItemStack.class)) {
            if (recipeItem.size() != 0) {
                List<List<ItemStack>> newSeenItems = new ArrayList<>(seenItems);
                newSeenItems.add(stack);
                addToIngredients(baseIngredients, getBaseIngredientsForItem(recipeItem, newSeenItems, extraOutputs));
            }
        }

        return baseIngredients;
    }

    private static boolean checkForExtraItem(List<List<ItemStack>> extraItems, List<ItemStack> recipeItem) {
        for (List<ItemStack> extraItem : extraItems) {
            if (areItemStacksEqualIgnoreSize(extraItem.get(0), recipeItem.get(0))) {
                decreaseStackCount(extraItem);
                return true;
            }
        }
        return false;
    }

    private static void decreaseStackCount(List<ItemStack> stack) {
        stack.get(0).setCount(stack.get(0).getCount() - 1);
    }

    private static IIngredients pickBestRecipe(List<IIngredients> recipes) {
        IIngredients bestRecipe = null;
        for (IIngredients recipe : recipes) {
            boolean validRecipe = true;

            if (isBlockRecipe(recipe) || isNuggetRecipe(recipe)) {
                continue;
            }

            for (List<ItemStack> item : recipe.getInputs(ItemStack.class)) {
                if (!validRecipe)
                    break;

                for (String blacklistItem : TheBOMPluginMod.getInstance().getConfig().recipeItemBlacklist) {
                    if (item.size() != 0 &&
                            item.get(0).getItem().getRegistryName().toString().matches(blacklistItem)) {
                        validRecipe = false;
                        break;
                    }
                }
            }
            if (validRecipe) {
                return recipe;
            }
        }
        return null;
    }

    private static boolean isBlockRecipe(IIngredients recipe) {
        List<ItemStack> stack = null;

        // Make sure output gives 9
        if (recipe.getOutputs(ItemStack.class).get(0).get(0).getCount() != 9 &&
                recipe.getOutputs(ItemStack.class).get(0).get(0).getCount() != 4) {
            return false;
        }

        // Make sure there's only one input
        for (List<ItemStack> recipeItem : recipe.getInputs(ItemStack.class)) {
            if (stack == null && recipeItem.size() != 0) {
                stack = recipeItem;
            }
            else if (recipeItem.size() != 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean isNuggetRecipe(IIngredients recipe) {
        List<ItemStack> stack = null;

        // Make sure output gives 1
        if (recipe.getOutputs(ItemStack.class).get(0).get(0).getCount() != 1) {
            return false;
        }

        // Make sure there's 9 of the same item
        if (recipe.getInputs(ItemStack.class).size() != 9 &&
                recipe.getInputs(ItemStack.class).size() != 4) {
            return false;
        }
        for (List<ItemStack> recipeItem : recipe.getInputs(ItemStack.class)) {
            if (recipeItem.size() == 0) {
                return false;
            }
            else if (stack == null) {
                stack = recipeItem;
            }
            else if (!areItemStacksEqualIgnoreSize(recipeItem.get(0), stack.get(0))){
                return false;
            }
        }
        return true;
    }

    private static boolean isBaseItem(List<ItemStack> item) {
        if (item.size() > 1) // Is an ore dictionary item
            return true;
//        else if (item.get(0).getUnlocalizedName().toLowerCase().contains("ingot")) // Is an ingot
//            return true;
//        else if (item.get(0).getUnlocalizedName().toLowerCase().contains("ore")) // Is an ore
//            return true;

        for (String regex : TheBOMPluginMod.getInstance().getConfig().baseItems)
            if (item.get(0).getItem().getRegistryName().toString().matches(regex))
                return true;

        return false;
    }

    private static void addToIngredients(List<List<ItemStack>> ingredients, List<List<ItemStack>> ingredientsToAdd) {
        for (List<ItemStack> stack : ingredientsToAdd) {
            addItemStack(ingredients, stack);
        }
    }

    private static void addItemStack(List<List<ItemStack>> stacks, List<ItemStack> stackToAdd) {
        for (List<ItemStack> stack : stacks) {
            if (areItemStacksEqualIgnoreSize(stack.get(0), stackToAdd.get(0))) {
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

    private static boolean areItemStacksEqualIgnoreSize(ItemStack stackA, ItemStack stackB) {
        if (stackA.isEmpty() && stackB.isEmpty()) {
            return true;
        } else {
            if (!stackA.isEmpty() && !stackB.isEmpty()){
                if (stackA.getItem() != stackB.getItem()) {
                    return false;
                } else if (stackA.getItemDamage() != stackB.getItemDamage()) {
                    return false;
                } else {
                    return ItemStack.areItemStackTagsEqual(stackA, stackB) && stackA.areCapsCompatible(stackB);
                }
            }
            else {
                return false;
            }
        }
    }
}
