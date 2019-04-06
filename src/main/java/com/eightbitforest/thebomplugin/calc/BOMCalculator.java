package com.eightbitforest.thebomplugin.calc;

import com.eightbitforest.thebomplugin.config.BOMConfig;
import com.eightbitforest.thebomplugin.util.ItemStackUtil;
import com.eightbitforest.thebomplugin.util.RecipeUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.eightbitforest.thebomplugin.util.ItemStackUtil.areItemStackListsEqualIgnoreSize;
import static com.eightbitforest.thebomplugin.util.ItemStackUtil.areItemStacksEqualIgnoreSize;

public class BOMCalculator {
    private List<CachedRecipe> cachedRecipes = new ArrayList<>();
    private List<CachedRecipe> finalCachedRecipes = new ArrayList<>();
    private final ItemStackUtil.ItemStackComparator itemStackComparator = new ItemStackUtil.ItemStackComparator();

    public List<List<ItemStack>> getBaseIngredients(IIngredients recipe) {
        return getBaseIngredients(recipe, 1);
    }

    public List<List<ItemStack>> getBaseIngredients(IIngredients recipe, int outputAmount) {
        try {
            List<ItemStack> output = recipe.getOutputs(VanillaTypes.ITEM).get(0);

            List<List<ItemStack>> baseIngredients = new ArrayList<>();
            List<List<ItemStack>> extraOutputs = new ArrayList<>();

            if (outputAmount == 1) {
                // See if we've already cached the final recipe
                CachedRecipe finalCachedRecipe = findFinalCachedRecipe(recipe);
                if (finalCachedRecipe != null) {
                    baseIngredients = finalCachedRecipe.getBaseIngredients();
                    return baseIngredients;
                }
            }

            // Calculate base materials for this recipe
            for (List<ItemStack> recipeItem : recipe.getInputs(VanillaTypes.ITEM)) {
                for (int i = 0; i < outputAmount; i++) {
                    List<List<ItemStack>> seenItems = new ArrayList<>(Collections.singletonList(output));
                    addToIngredients(baseIngredients, getBaseIngredientsForItem(recipeItem, seenItems, extraOutputs));
                }
            }

            cachedRecipes.add(new CachedRecipe(baseIngredients, output, extraOutputs));
            utilizeExtraOutputs(baseIngredients, extraOutputs);

            // Sort by number of items in each stack
            baseIngredients.sort(itemStackComparator);

            finalCachedRecipes.add(new CachedRecipe(recipe, baseIngredients, output, extraOutputs));

            return baseIngredients;
        } catch (StackOverflowError e) {
            System.out.println("BOMJeiPlugin: ERROR. StackOverFlow while processing: " + recipe.getOutputs(VanillaTypes.ITEM).get(0).get(0).getUnlocalizedName());
            System.out.println("Recipe:");
            for (List<ItemStack> ingredient : recipe.getInputs(VanillaTypes.ITEM)) {
                ingredient.get(0).getUnlocalizedName();
            }
            e.printStackTrace();
        }

        // Cache the broken recipe
        finalCachedRecipes.add(new CachedRecipe(recipe, recipe.getInputs(VanillaTypes.ITEM), recipe.getOutputs(VanillaTypes.ITEM).get(0), new ArrayList<>()));
        return recipe.getInputs(VanillaTypes.ITEM);
    }

    private List<List<ItemStack>> getBaseIngredientsForItem(List<ItemStack> stack, List<List<ItemStack>> seenItems, List<List<ItemStack>> extraOutputs) {
        List<List<ItemStack>> baseIngredients = new ArrayList<>();

        // Make sure stack isn't empty
        if (stack.size() == 0) {
            return baseIngredients;
        }

        // Make sure this item isn't a base item
        if (isBaseItem(stack)) {
            addItemStack(baseIngredients, stack);
            return baseIngredients;
        }

        // See if we've cached this recipe
        CachedRecipe cachedRecipe = findCachedRecipe(stack);
        if (cachedRecipe != null) {
            addToIngredients(extraOutputs, cachedRecipe.getExtraOutputs());
            addToIngredients(baseIngredients, cachedRecipe.getBaseIngredients());
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

        // Get matching recipes
        List<IIngredients> recipes = RecipeUtil.getRecipesForItemStack(stack.get(0));

        // Pick recipe that doesn't include blacklisted items TODO: Pick best recipe
        IIngredients chosenRecipe = pickBestRecipe(recipes);

        // Make sure that there is a good recipe
        if (chosenRecipe == null) {
            addItemStack(baseIngredients, stack);
            return baseIngredients;
        }

        // Make sure recipe doesn't need something we already saw
        for (List<ItemStack> item : seenItems) {
            for (List<ItemStack> recipeItem : chosenRecipe.getInputs(VanillaTypes.ITEM)) {
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

        // See if we have extra outputs
        List<ItemStack> recipeOutput = chosenRecipe.getOutputs(VanillaTypes.ITEM).get(0);
        if (recipeOutput.get(0).getCount() > 1) {
            List<ItemStack> extraStack = new ArrayList<>(Collections.singletonList(recipeOutput.get(0).copy()));
            decreaseStackCount(extraStack);
            addToIngredients(newExtraOutputs, new ArrayList<>(Collections.singletonList(extraStack)));
        }

        // Add recipe components to ingredients
        for (List<ItemStack> recipeItem : chosenRecipe.getInputs(VanillaTypes.ITEM)) {
            if (recipeItem.size() != 0) {
                List<List<ItemStack>> newSeenItems = new ArrayList<>(seenItems);

                newSeenItems.add(stack);
                addToIngredients(baseIngredients, getBaseIngredientsForItem(recipeItem, newSeenItems, newExtraOutputs));
            }
        }


        cachedRecipes.add(new CachedRecipe(baseIngredients, recipeOutput, newExtraOutputs));
        addToIngredients(extraOutputs, newExtraOutputs);

        return baseIngredients;
    }

    private void utilizeExtraOutputs(List<List<ItemStack>> baseIngredients, List<List<ItemStack>> extraOutputs) {
        purgeAirFromExtraOutputs(extraOutputs);
        List<List<ItemStack>> extraOutputsCopy = ItemStackUtil.copyItemStackList(extraOutputs);
        for (List<ItemStack> extraOutputCopy : extraOutputsCopy) {
            List<ItemStack> extraOutput = findIngredientInList(extraOutputs, extraOutputCopy);
            if (extraOutput != null) {
                if (extraOutput.get(0).getCount() > 0) {
                    if (isBaseItem(extraOutput)) {
                        int numToDecrease = extraOutput.get(0).getCount();
                        for (int i = 0; i < numToDecrease; i++) {
                            List<ItemStack> ingredient = findIngredientInList(baseIngredients, extraOutput);
                            decreaseStackCount(ingredient);
                            decreaseStackCount(extraOutput);
                        }
                        continue;
                    }

                    List<IIngredients> recipes = RecipeUtil.getRecipesForItemStack(extraOutput.get(0));
                    IIngredients extraOutputRecipe = pickBestRecipe(recipes);
                    if (extraOutputRecipe != null) {
                        int numberOfExtras = extraOutput.get(0).getCount() / extraOutputRecipe.getOutputs(VanillaTypes.ITEM).get(0).get(0).getCount();
                        if (numberOfExtras > 0) {
                            for (int i = 0; i < numberOfExtras * extraOutputRecipe.getOutputs(VanillaTypes.ITEM).get(0).get(0).getCount(); i++)
                                decreaseStackCount(extraOutput);

                            for (List<ItemStack> extraOutputRecipeItem : extraOutputRecipe.getInputs(VanillaTypes.ITEM)) {
                                if (extraOutputRecipeItem.size() > 0) {
                                    List<ItemStack> extraOutputRecipeItemCopy = ItemStackUtil.copyItemStack(extraOutputRecipeItem);
                                    extraOutputRecipeItemCopy.forEach(stack -> stack.setCount(numberOfExtras));

                                    addToIngredients(extraOutputs, Collections.singletonList(extraOutputRecipeItemCopy));
                                    utilizeExtraOutputs(baseIngredients, extraOutputs);
                                }
                            }
                        }
                    } else {
                        int numToDecrease = extraOutput.get(0).getCount();
                        for (int i = 0; i < numToDecrease; i++) {
                            List<ItemStack> ingredient = findIngredientInList(baseIngredients, extraOutput);
                            decreaseStackCount(ingredient);
                            decreaseStackCount(extraOutput);
                        }
                    }
                }
            }
        }
    }

    private void purgeAirFromExtraOutputs(List<List<ItemStack>> extraOutputs) {
        extraOutputs.removeIf(next -> next.size() == 0 || next.get(0).getCount() <= 0);
    }

    private List<ItemStack> findIngredientInList(List<List<ItemStack>> ingredients, List<ItemStack> toFind) {
        for (List<ItemStack> ingredient : ingredients) {
            if (areItemStacksEqualIgnoreSize(ingredient.get(0), toFind.get(0)) && ingredient.get(0).getCount() > 0) {
                return ingredient;
            }
        }
        return null;
    }

    private CachedRecipe findCachedRecipe(List<ItemStack> output) {
        for (CachedRecipe cachedRecipe : cachedRecipes) {
            if (areItemStacksEqualIgnoreSize(cachedRecipe.getOutput().get(0), output.get(0))) {
                return cachedRecipe;
            }
        }
        return null;
    }

    private CachedRecipe findFinalCachedRecipe(IIngredients recipe) {
        List<ItemStack> output = recipe.getOutputs(VanillaTypes.ITEM).get(0);
        List<List<ItemStack>> inputs = recipe.getInputs(VanillaTypes.ITEM);
        for (CachedRecipe cachedRecipe : finalCachedRecipes) {
            List<List<ItemStack>> cachedInputs = cachedRecipe.getInputs().getInputs(VanillaTypes.ITEM);
            if (areItemStacksEqualIgnoreSize(cachedRecipe.getOutput().get(0), output.get(0))) {
                if (areItemStackListsEqualIgnoreSize(cachedInputs, inputs)) {
                    return cachedRecipe;
                }
            }
        }
        return null;
    }

    private void decreaseStackCount(List<ItemStack> stack) {
        for (ItemStack item : stack)
            item.setCount(item.getCount() - 1);
    }

    private IIngredients pickBestRecipe(List<IIngredients> recipes) {
        for (IIngredients recipe : recipes) {
            boolean validRecipe = true;

            if (isBlockRecipe(recipe) || isNuggetRecipe(recipe)) {
                continue;
            }

            for (List<ItemStack> item : recipe.getInputs(VanillaTypes.ITEM)) {
                if (!validRecipe)
                    break;

                if (item.size() != 0) {
                    if (doesItemMatchConfigList(BOMConfig.getRecipeItemBlacklist(), item.get(0))) {
                        validRecipe = false;
                    }
                }
            }
            if (validRecipe) {
                return recipe;
            }
        }
        return null;
    }

    private boolean doesItemMatchConfigList(List<String> configList, ItemStack stack) {
        for (String configItem : configList) {
            String[] configParts = configItem.split("@");
            if (stack.getItem().getRegistryName().toString().matches(configParts[0])) {
                if (configParts.length == 2) {
                    if (Integer.toString(stack.getItemDamage()).matches(configParts[1])) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isBlockRecipe(IIngredients recipe) {
        List<ItemStack> stack = null;

        // Make sure output gives 9
//        if (recipe.getOutputs(VanillaTypes.ITEM).get(0).get(0).getCount() != 9 &&
//                recipe.getOutputs(VanillaTypes.ITEM).get(0).get(0).getCount() != 6 &&
//                recipe.getOutputs(VanillaTypes.ITEM).get(0).get(0).getCount() != 4) {
//            return false;
//        }

        // Make sure there's only one input
        for (List<ItemStack> recipeItem : recipe.getInputs(VanillaTypes.ITEM)) {
            if (stack == null && recipeItem.size() != 0) {
                stack = recipeItem;
            } else if (recipeItem.size() != 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isNuggetRecipe(IIngredients recipe) {
        List<ItemStack> stack = null;

        // Make sure output gives 1
        if (recipe.getOutputs(VanillaTypes.ITEM).get(0).get(0).getCount() != 1) {
            return false;
        }

        // Make sure there's 9 of the same item
//        if (recipe.getInputs(VanillaTypes.ITEM).size() != 9 &&
//                recipe.getInputs(VanillaTypes.ITEM).size() != 6 &&
//                recipe.getInputs(VanillaTypes.ITEM).size() != 4) {
//            return false;
//        }
        for (List<ItemStack> recipeItem : recipe.getInputs(VanillaTypes.ITEM)) {
            if (recipeItem.size() == 0) {
                return false;
            } else if (stack == null) {
                stack = recipeItem;
            } else if (!areItemStacksEqualIgnoreSize(recipeItem.get(0), stack.get(0))) {
                return false;
            }
        }
        return true;
    }

    private boolean isBaseItem(List<ItemStack> item) {
        if (item.size() > 1) // Is an ore dictionary item
            return true;

        return doesItemMatchConfigList(BOMConfig.getBaseItems(), item.get(0));

    }

    private void addToIngredients(List<List<ItemStack>> ingredients, List<List<ItemStack>> ingredientsToAdd) {
        for (List<ItemStack> stack : ingredientsToAdd) {
            addItemStack(ingredients, stack);
        }
    }

    private void addItemStack(List<List<ItemStack>> stacks, List<ItemStack> stackToAdd) {
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
}
