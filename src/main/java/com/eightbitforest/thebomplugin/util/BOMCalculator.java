package com.eightbitforest.thebomplugin.util;

import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BOMCalculator {

    private static List<CachedRecipe> cachedRecipes = new ArrayList<>();

    private BOMCalculator() {}

    public static List<List<ItemStack>> getBaseIngredients(IIngredients recipe) {
        List<ItemStack> output = recipe.getOutputs(ItemStack.class).get(0);

        List<List<ItemStack>> baseIngredients = new ArrayList<>();
        List<List<ItemStack>> extraOutputs = new ArrayList<>();

        CachedRecipe cachedRecipe = findCachedRecipe(output);
        if (cachedRecipe != null) {
            baseIngredients = cachedRecipe.getBaseIngredients();
            utilizeExtraOutputs(baseIngredients, cachedRecipe.getExtraOutputs());
            return baseIngredients;
        }

        List<List<ItemStack>> extraOutputsForCache = new ArrayList<>();

        for (List<ItemStack> recipeItem : recipe.getInputs(ItemStack.class)) {
            List<List<ItemStack>> seenItems = new ArrayList<>(Collections.singletonList(output));
            addToIngredients(baseIngredients, getBaseIngredientsForItem(recipeItem, seenItems, extraOutputs, extraOutputsForCache));
        }

        cachedRecipes.add(new CachedRecipe(baseIngredients, output, extraOutputs));
        utilizeExtraOutputs(baseIngredients, extraOutputs);

        return baseIngredients;
    }

    private static List<List<ItemStack>> getBaseIngredientsForItem(List<ItemStack> stack, List<List<ItemStack>> seenItems, List<List<ItemStack>> extraOutputs, List<List<ItemStack>> extraOutputsForCache) {
        List<List<ItemStack>> baseIngredients = new ArrayList<>();

        // Make sure stack isn't empty
        if (stack.size() == 0) {
            return baseIngredients;
        }

        // See if we've cached this recipe
        CachedRecipe cachedRecipe = findCachedRecipe(stack);
        if (cachedRecipe != null) {
            addToIngredients(extraOutputs, cachedRecipe.getExtraOutputs());
            addToIngredients(baseIngredients, cachedRecipe.getBaseIngredients());
            return baseIngredients;
        }

        // See if we have an extra output for recipeItem
//        if (checkForExtraItem(extraOutputs, stack)) {
//            return baseIngredients;
//        }

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

        // Pick recipe that doesn't include blacklisted items TODO: Pick best recipe
        IIngredients chosenRecipe = pickBestRecipe(recipes);

        // Make sure that there is a good recipe
        if (chosenRecipe == null) {
            addItemStack(baseIngredients, stack);
            return baseIngredients;
        }

        // Make sure recipe doesn't need something we already saw
        for (List<ItemStack> item : seenItems) {
            for (List<ItemStack> recipeItem : chosenRecipe.getInputs(ItemStack.class)) {
                if (recipeItem.size() != 0 && areItemStacksEqualIgnoreSize(recipeItem.get(0), item.get(0))) {
                    addItemStack(baseIngredients, item);
                    return baseIngredients;
                }
            }
        }

        //////////////////////
        // Passed all tests //
        //////////////////////

        List<List<ItemStack>> newExtraOutputs = new ArrayList<>();
//        extraOutputs.clear();

        // See if we have extra outputs
        List<ItemStack> recipeOutput = chosenRecipe.getOutputs(ItemStack.class).get(0);
        if (recipeOutput.get(0).getCount() > 1) {
            List<ItemStack> extraStack = new ArrayList<>(Collections.singletonList(recipeOutput.get(0).copy()));
            decreaseStackCount(extraStack);
            addToIngredients(newExtraOutputs, new ArrayList<>(Collections.singletonList(extraStack)));
//            addToIngredients(extraOutputsForCache, new ArrayList<>(Collections.singletonList(extraStack)));
        }

        List<List<ItemStack>> newExtraOutputsForCache = new ArrayList<>();

        // Add recipe components to ingredients
        for (List<ItemStack> recipeItem : chosenRecipe.getInputs(ItemStack.class)) {
            if (recipeItem.size() != 0) {
                List<List<ItemStack>> newSeenItems = new ArrayList<>(seenItems);

                newSeenItems.add(stack);
                addToIngredients(baseIngredients, getBaseIngredientsForItem(recipeItem, newSeenItems, newExtraOutputs, extraOutputsForCache));
            }
        }

//        utilizeExtraOutputs(baseIngredients, extraOutputs);
//        if (recipeOutput.get(0).getCount() > 1) {
//            List<ItemStack> copy = new ArrayList<>(recipeOutput.size());
//            for (ItemStack originalStack : recipeOutput) {
//                copy.add(originalStack.copy());
//            }
//            decreaseStackCount(copy);
//            addToIngredients(newExtraOutputsForCache, Collections.singletonList(copy));
//        }

        cachedRecipes.add(new CachedRecipe(baseIngredients, recipeOutput, newExtraOutputs));

        addToIngredients(extraOutputs, newExtraOutputs);

        return baseIngredients;
    }

    private static void utilizeExtraOutputs(List<List<ItemStack>> baseIngredients, List<List<ItemStack>> extraOutputs) {
        for (List<ItemStack> extraOutput : extraOutputs) {
            if (extraOutput.get(0).getCount() > 0) {

//                CachedRecipe recipe = findCachedRecipe(extraOutput);
//                if (recipe == null) {
////                    System.out.println("ERROR: couldn't find error for extra output");
//                    List<ItemStack> ingredient = findIngredientToDecrease(baseIngredients, extraOutput);
//                    decreaseStackCount(ingredient);
//                }
//                else {
//                    int numberOfExtras = extraOutput.get(0).getCount() / recipe.getOutput().get(0).getCount();
//                    for (List<ItemStack> extraRecipeItem : recipe.getBaseIngredients()) {
//                        utilizeExtraOutputs(baseIngredients, Collections.singletonList(extraRecipeItem));
//                    }
//                }
                if (isBaseItem(extraOutput)) {
                    for (int i = 0; i < extraOutput.get(0).getCount(); i++) {
                        List<ItemStack> ingredient = findIngredientToDecrease(baseIngredients, extraOutput);
                        decreaseStackCount(ingredient);
                    }
                    continue;
                }

                List<IIngredients> recipes = Recipes.getRecipesForItemStack(extraOutput.get(0));
                IIngredients extraOutputRecipe = pickBestRecipe(recipes);
                if (extraOutputRecipe != null) {
                    int numberOfExtras = extraOutput.get(0).getCount() / extraOutputRecipe.getOutputs(ItemStack.class).get(0).get(0).getCount();
                    if (numberOfExtras > 0) {
                        for (List<ItemStack> extraOutputRecipeItem : extraOutputRecipe.getInputs(ItemStack.class)) {
                            if (extraOutputRecipeItem.size() > 0) {
//                                if (!ignoreNumberOfExtraOutputs) {
//                                    for (int i = 0; i < numberOfExtras; i++) {
                                List<ItemStack> extraOutputRecipeItemCopy = Utils.copyItemStack(extraOutputRecipeItem);
                                extraOutputRecipeItemCopy.forEach(stack -> stack.setCount(numberOfExtras));
                                    utilizeExtraOutputs(baseIngredients, Collections.singletonList(extraOutputRecipeItemCopy));
//                                    }
//                                } else {
//                                    utilizeExtraOutputs(baseIngredients, Collections.singletonList(extraOutputRecipeItem), false);
//                                }
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < extraOutput.get(0).getCount(); i++) {
                        List<ItemStack> ingredient = findIngredientToDecrease(baseIngredients, extraOutput);
                        decreaseStackCount(ingredient);
                    }
                }
            }
        }
    }

    private static List<ItemStack> findIngredientToDecrease(List<List<ItemStack>> ingredients, List<ItemStack> toFind) {
        for (List<ItemStack> ingredient : ingredients) {
            if (areItemStacksEqualIgnoreSize(ingredient.get(0), toFind.get(0)) && ingredient.get(0).getCount() > 1) {
                return ingredient;
            }
        }
        return null;
    }

    private static CachedRecipe findCachedRecipe(List<ItemStack> output) {
        for (CachedRecipe cachedRecipe : cachedRecipes) {
            if (areItemStacksEqualIgnoreSize(cachedRecipe.getOutput().get(0), output.get(0))) {
                return cachedRecipe;
            }
        }
        return null;
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
        for (ItemStack item : stack)
            item.setCount(item.getCount() - 1);
    }

    private static IIngredients pickBestRecipe(List<IIngredients> recipes) {
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
