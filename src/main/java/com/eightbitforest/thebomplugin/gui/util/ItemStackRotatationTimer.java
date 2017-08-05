package com.eightbitforest.thebomplugin.gui.util;

import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemStackRotatationTimer {

    private static int delay = 1000;
    private List<ItemStack> stack;
    private long startTime;

    public ItemStackRotatationTimer(List<ItemStack> stack) {
        this.stack = stack;
        startTime = System.currentTimeMillis();
    }

    public ItemStack getCurrentStack() {
        return stack.get((int)(((System.currentTimeMillis() - startTime) / delay) % stack.size()));
    }

    public List<ItemStack> getAllItems() {
        return stack;
    }
}