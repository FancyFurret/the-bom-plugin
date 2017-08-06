package com.eightbitforest.thebomplugin.gui.util;

import com.eightbitforest.thebomplugin.util.Utils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemListGuiItemStack {

    private ItemStackRotatationTimer stack;
    private int inventoryAmount = 0;

    public ItemListGuiItemStack(List<ItemStack> items) {
        this.stack = new ItemStackRotatationTimer(items);
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
                if (Utils.areItemStacksEqualIgnoreSize(itemStack, inventoryStack)) {
                    inventoryAmount += inventoryStack.getCount();
                    break;
                }
            }
        }
    }

}
