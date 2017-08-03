package com.eightbitforest.thebomplugin;

import com.eightbitforest.thebomplugin.util.BOMConfig;
import com.eightbitforest.thebomplugin.gui.BOMGuiEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        instance = this;
        config = new BOMConfig(e.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new BOMGuiEventHandler(Minecraft.getMinecraft()));
    }
}
