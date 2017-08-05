package com.eightbitforest.thebomplugin.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<List<ItemStack>> copyItemStackList(List<List<ItemStack>> list) {
        List<List<ItemStack>> itemStackListCopy = new ArrayList<>();
        for (List<ItemStack> stackList : list) {
            itemStackListCopy.add(copyItemStack(stackList));
        }
        return itemStackListCopy;
    }

    public static List<ItemStack> copyItemStack(List<ItemStack> stack) {
        List<ItemStack> stackListCopy = new ArrayList<>();
        for (ItemStack stackCopy : stack) {
            stackListCopy.add(stackCopy.copy());
        }
        return stackListCopy;
    }

    public static boolean areItemStacksEqualIgnoreSize(ItemStack stackA, ItemStack stackB) {
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

    public static int clamp(int n, int min, int max) {
        return Math.max(min, Math.min(max, n));
    }
}
