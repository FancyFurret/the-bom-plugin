package com.eightbitforest.thebomplugin.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItemStackUtil {
    private ItemStackUtil(){

    }

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

    public static boolean areItemStackListsEqualIgnoreSize(List<List<ItemStack>> stackA, List<List<ItemStack>> stackB) {
        if (stackA.size() != stackB.size()) {
            return false;
        }

        for (int i = 0; i < stackA.size(); i++) {
            if (stackA.get(i).size() == 0 || stackB.size() == 0) {
                return false;
            }
            if (!areItemStacksEqualIgnoreSize(stackA.get(i).get(0), stackB.get(i).get(0))) {
                return false;
            }
        }

        return true;
    }

    public static class ItemStackComparator implements Comparator<List<ItemStack>>{
        @Override
        public int compare(List<ItemStack> o1, List<ItemStack> o2) {
            return o2.get(0).getCount() - o1.get(0).getCount();
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }

    }
}
