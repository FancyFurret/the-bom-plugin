package com.eightbitforest.thebomplugin.util;

import net.minecraft.item.ItemStack;

import java.util.*;

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

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatLong(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatLong(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatLong(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static <T> boolean arrayContains(T[] array, T item) {
        for (T arrayItem : array) {
            if (arrayItem.equals(item)) {
                return true;
            }
        }
        return false;
    }
}
