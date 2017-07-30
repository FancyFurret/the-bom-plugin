package com.eightbitforest.thebomplugin.util;

import net.minecraft.item.ItemStack;

import java.util.Comparator;
import java.util.List;

public class ItemStackComparator implements Comparator<List<ItemStack>>{
    @Override
    public int compare(List<ItemStack> o1, List<ItemStack> o2) {
        return o2.get(0).getCount() - o1.get(0).getCount();
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

}
