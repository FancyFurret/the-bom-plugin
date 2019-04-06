package com.eightbitforest.thebomplugin.gui.util;

import net.minecraft.item.ItemStack;

import java.util.List;

class ItemStackRotationTimer {
    private static final int DELAY = 1000;

    private List<ItemStack> stack;
    private long startTime;

    ItemStackRotationTimer(List<ItemStack> stack) {
        this.stack = stack;
        startTime = System.currentTimeMillis();
    }

    ItemStack getCurrentStack() {
        return stack.get((int)(((System.currentTimeMillis() - startTime) / DELAY) % stack.size()));
    }

    List<ItemStack> getAllItems() {
        return stack;
    }
}