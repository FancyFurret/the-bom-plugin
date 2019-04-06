package com.eightbitforest.thebomplugin.gui.util;

import com.eightbitforest.thebomplugin.util.ItemStackUtil;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemListGuiItemStack {
    private ItemStackRotationTimer stack;
    private int inventoryAmount = 0;

    public ItemListGuiItemStack(List<ItemStack> items) {
        this.stack = new ItemStackRotationTimer(items);
    }

    public ItemStack getCurrentStack() {
        return stack.getCurrentStack();
    }

    public boolean doesInventoryHaveEnough() {
        return inventoryAmount >= stack.getCurrentStack().getCount();
    }

    public int getRemainingNeeded() {
        return stack.getCurrentStack().getCount() - inventoryAmount;
    }

    public void setInventoryAmount(IInventory inventory) {
        inventoryAmount = 0;
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack inventoryStack = inventory.getStackInSlot(i);
            for (ItemStack itemStack : stack.getAllItems()) {
                if (ItemStackUtil.areItemStacksEqualIgnoreSize(itemStack, inventoryStack)) {
                    inventoryAmount += inventoryStack.getCount();
                    break;
                }
            }
        }
    }
}
