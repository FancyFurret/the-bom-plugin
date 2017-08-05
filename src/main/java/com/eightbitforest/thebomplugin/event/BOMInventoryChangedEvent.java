package com.eightbitforest.thebomplugin.event;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BOMInventoryChangedEvent {
    private List<IInventoryChangedEventListener> inventoryChangedEventListeners;

    public BOMInventoryChangedEvent() {
        inventoryChangedEventListeners = new ArrayList<>();

        InventoryChangeTrigger delegate = CriteriaTriggers.INVENTORY_CHANGED;
        InventoryChangeTrigger forwarding = new InventoryChangeTrigger() {
            @Override
            public ResourceLocation getId() {
                return delegate.getId();
            }

            @Override
            public void addListener(PlayerAdvancements advancements, ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> listener) {
                delegate.addListener(advancements, listener);
            }

            @Override
            public void removeListener(PlayerAdvancements advancements, ICriterionTrigger.Listener<InventoryChangeTrigger.Instance> listener) {
                delegate.removeListener(advancements, listener);
            }

            @Override
            public void removeAllListeners(PlayerAdvancements advancements) {
                delegate.removeAllListeners(advancements);
            }

            @Override
            public InventoryChangeTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
                return delegate.deserializeInstance(json, context);
            }

            @Override
            public void trigger(EntityPlayerMP player, InventoryPlayer inventory) {
                delegate.trigger(player, inventory);
                for (IInventoryChangedEventListener listener : inventoryChangedEventListeners) {
                    listener.onInventoryChanged(inventory);
                }
            }
        };
        Field invChangedField = ReflectionHelper.findField(CriteriaTriggers.class, "field_192125_e", "INVENTORY_CHANGED");
        try {
            EnumHelper.setFailsafeFieldValue(invChangedField, null, forwarding);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void registerInventoryChangedEventListener(IInventoryChangedEventListener inventoryChangedListener) {
        inventoryChangedEventListeners.add(inventoryChangedListener);
    }
}
