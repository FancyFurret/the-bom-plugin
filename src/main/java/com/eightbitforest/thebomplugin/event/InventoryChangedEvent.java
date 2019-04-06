package com.eightbitforest.thebomplugin.event;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class InventoryChangedEvent {
    private List<IInventoryChangedEventListener> inventoryChangedEventListeners;

    private boolean enable = false;
    private int timesChanged = -1;

    public InventoryChangedEvent() {
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
