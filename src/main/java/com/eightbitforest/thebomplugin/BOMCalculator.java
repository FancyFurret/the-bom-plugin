package com.eightbitforest.thebomplugin;

import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BOMCalculator {
    private BOMCalculator() {}

    private static ArrayList<String> baseItems = new ArrayList<>(Arrays.asList(
            "^minecraft:grass$",
            "^minecraft:dirt$",
            "^minecraft:cobblestone$",
            "^minecraft:sapling$",
            "^minecraft:bedrock$",
            "^minecraft:sand$",
            "^minecraft:gravel$",
            "^minecraft:.*_ore$",
            "^minecraft:log$",
            "^minecraft:log2$",
            "^minecraft:leaves$",
            "^minecraft:leaves2$",
            "^minecraft:sponge$",
            "^minecraft:web$",
            "^minecraft:tallgrass$",
            "^minecraft:deadbush$",
            "^minecraft:.*_flower$",
            "^minecraft:.*_mushroom$",
            "^minecraft:mossy_cobblestone$",
            "^minecraft:obsidian$",
            "^minecraft:fire$",
            "^minecraft:mob_spawner$",
            "^minecraft:ice$",
            "^minecraft:snow$",
            "^minecraft:cactus$",
            "^minecraft:clay$",
            "^minecraft:pumpkin$",
            "^minecraft:netherrack$",
            "^minecraft:soul_sand$",
            "^minecraft:glowstone$",
            "^minecraft:vine$",
            "^minecraft:mycelium$",
            "^minecraft:waterlily$",
            "^minecraft:end_stone$",
            "^minecraft:dragon_egg$",
            "^minecraft:cocoa$",
            "^minecraft:apple$",
            "^minecraft:coal$",
            "^minecraft:string$",
            "^minecraft:feather$",
            "^minecraft:gunpowder$",
            "^minecraft:wheat$",
            "^minecraft:flint$",
            "^minecraft:porkchop$",
            "^minecraft:redstone$",
            "^minecraft:leather$",
            "^minecraft:clay_ball$",
            "^minecraft:reeds$",
            "^minecraft:egg$",
            "^minecraft:fish$",
            "^minecraft:dye$",
            "^minecraft:bone$",
            "^minecraft:melon$",
            "^minecraft:melon_seeds$",
            "^minecraft:beef$",
            "^minecraft:chicken$",
            "^minecraft:rotten_flesh$",
            "^minecraft:ender_pearl$",
            "^minecraft:blaze_rod$",
            "^minecraft:ghast_tear$",
            "^minecraft:nether_wart$",
            "^minecraft:spider_eye$",
            "^minecraft:blaze_powder$",
            "^minecraft:carrot$",
            "^minecraft:potato$",
            "^minecraft:map$",
            "^minecraft:skull$",
            "^minecraft:nether_star$",
            "^minecraft:record_.*$"
    ));

    public static List<List<ItemStack>> getBaseIngredients(List<List<ItemStack>> recipe, List<ItemStack> stack) {
        List<List<ItemStack>> baseIngredients = new ArrayList<>();
        for (List<ItemStack> recipeItem : recipe) {
            addToIngredients(baseIngredients, getBaseIngredientsForItem(recipeItem, new ArrayList<>(Arrays.asList(stack))));
        }
        return baseIngredients;
    }

    private static List<List<ItemStack>> getBaseIngredientsForItem(List<ItemStack> stack, List<List<ItemStack>> seenItems) {
        List<List<ItemStack>> baseIngredients = new ArrayList<>();

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

        List<IRecipe> recipes = CraftingRecipeChecker.getRecipesForItemStack(stack.get(0));

        // Make sure recipe doesn't need something we already saw
        for (List<ItemStack> item : seenItems) {
            for (Ingredient ingredient : recipes.get(0).getIngredients()) {
                if (ingredient.getMatchingStacks()[0].isItemEqual(item.get(0))) {
                    addItemStack(baseIngredients, item);
                    return baseIngredients;
                }
            }

        }

        for (Ingredient recipeItem : recipes.get(0).getIngredients()) {
            List<List<ItemStack>> newSeenItems = new ArrayList<>(seenItems);
            newSeenItems.add(stack);

            addToIngredients(baseIngredients, getBaseIngredientsForItem(Arrays.asList(recipeItem.getMatchingStacks()), newSeenItems));
        }

        return baseIngredients;
    }

    private static boolean isBaseItem(List<ItemStack> item) {
        if (item.size() > 1) // Is an ore dictionary item
            return true;
        else if (item.get(0).getUnlocalizedName().toLowerCase().contains("ingot")) // Is an ingot
            return true;
        else if (item.get(0).getUnlocalizedName().toLowerCase().contains("ore")) // Is an ore
            return true;

        for (String regex : baseItems)
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
