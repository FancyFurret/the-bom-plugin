package com.eightbitforest.thebomplugin.event;

import net.minecraft.entity.player.InventoryPlayer;

public interface IInventoryChangedEventListener {
    void onInventoryChanged(InventoryPlayer inventory);
}
