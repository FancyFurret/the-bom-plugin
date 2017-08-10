package com.eightbitforest.thebomplugin.event;

import com.eightbitforest.thebomplugin.gui.ItemListGui;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BOMInventoryChangedEvent {
    private List<IInventoryChangedEventListener> inventoryChangedEventListeners;

    private boolean enable = false;
    private int timesChanged = -1;

    public BOMInventoryChangedEvent() {
        inventoryChangedEventListeners = new ArrayList<>();
    }

    public void enable() {
        enable = true;
    }

    public void disable() {
        enable = false;
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event) {
        if (enable && event.player.isUser()) {
            InventoryPlayer inventory = event.player.inventory;
            int newTimesChanged = inventory.getTimesChanged();
            if (timesChanged != newTimesChanged) {
                timesChanged = newTimesChanged;
                System.out.println("Inventory changed");
                inventoryChanged(inventory);
            }
        }
    }

    private void inventoryChanged(InventoryPlayer inventory) {
        for (IInventoryChangedEventListener listener : inventoryChangedEventListeners) {
            listener.onInventoryChanged(inventory);
        }
    }

    public void registerInventoryChangedEventListener(IInventoryChangedEventListener inventoryChangedListener) {
        inventoryChangedEventListeners.add(inventoryChangedListener);
    }
}
