package com.eightbitforest.thebomplugin;

import com.eightbitforest.thebomplugin.event.BOMInventoryChangedEvent;
import com.eightbitforest.thebomplugin.util.BOMConfig;
import com.eightbitforest.thebomplugin.event.BOMGuiEventHandler;
import com.eightbitforest.thebomplugin.util.Constants;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

@SideOnly(Side.CLIENT)
@Mod(modid = TheBOMPluginMod.MODID, version = TheBOMPluginMod.VERSION, dependencies = "required-after:jei", clientSideOnly = true)
public class TheBOMPluginMod {
    public static final String MODID = "thebomplugin";
    public static final String VERSION = "0.1.2";

    private static TheBOMPluginMod instance;

    private BOMConfig config;

    public static TheBOMPluginMod getInstance() {
        return instance;
    }

    public BOMConfig getConfig() {
        return config;
    }

    private BOMInventoryChangedEvent inventoryChangedEvent;
    private BOMGuiEventHandler guiEventHandler;

    private KeyBinding scrollModifierKeybind;

    public KeyBinding getScrollModifierKeybind() {
        return scrollModifierKeybind;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        instance = this;
        config = new BOMConfig(e.getSuggestedConfigurationFile());

        // Register scroll modifier
        scrollModifierKeybind = new KeyBinding(Constants.BOM_SCROLL_KEYBIND_NAME, Keyboard.KEY_LMENU, Constants.BOM_KEYBIND_CATEGORY);
        ClientRegistry.registerKeyBinding(scrollModifierKeybind);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        inventoryChangedEvent = new BOMInventoryChangedEvent();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        guiEventHandler = new BOMGuiEventHandler(Minecraft.getMinecraft());
        MinecraftForge.EVENT_BUS.register(guiEventHandler);
        inventoryChangedEvent.registerInventoryChangedEventListener(guiEventHandler.getItemListGui());
    }
}
