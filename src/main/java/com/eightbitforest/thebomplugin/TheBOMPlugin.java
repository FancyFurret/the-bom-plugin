package com.eightbitforest.thebomplugin;

import com.eightbitforest.thebomplugin.event.BOMEventHandler;
import com.eightbitforest.thebomplugin.event.InventoryChangedEvent;
import com.eightbitforest.thebomplugin.gui.ItemListGui;
import com.eightbitforest.thebomplugin.config.BOMConfig;
import com.eightbitforest.thebomplugin.config.Keys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TheBOMPlugin {
    private static TheBOMPlugin instance;

    // TODO: Use item list manager
    private ItemListGui itemListGui;

    public TheBOMPlugin() {
        instance = this;
    }

    public static TheBOMPlugin getInstance() {
        return instance;
    }

    public ItemListGui getItemListGui() {
        return itemListGui;
    }

    public void preInit(FMLPreInitializationEvent e) {
        Keys.register();
        BOMConfig.load(e.getSuggestedConfigurationFile());
    }

    public void postInit(FMLPostInitializationEvent e) {
        itemListGui = new ItemListGui(Minecraft.getMinecraft());

        BOMEventHandler guiEventHandler = new BOMEventHandler(itemListGui);
        MinecraftForge.EVENT_BUS.register(guiEventHandler);

        InventoryChangedEvent inventoryChangedEvent = new InventoryChangedEvent();
        MinecraftForge.EVENT_BUS.register(inventoryChangedEvent);
        inventoryChangedEvent.registerInventoryChangedEventListener(itemListGui);
    }
}
