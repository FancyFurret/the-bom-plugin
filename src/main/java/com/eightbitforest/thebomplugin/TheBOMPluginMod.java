package com.eightbitforest.thebomplugin;

import com.eightbitforest.thebomplugin.util.BOMConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = TheBOMPluginMod.MODID, version = TheBOMPluginMod.VERSION, dependencies = "required-after:jei")
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
}
