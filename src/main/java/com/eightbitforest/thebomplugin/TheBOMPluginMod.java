package com.eightbitforest.thebomplugin;

import com.eightbitforest.thebomplugin.util.BOMEventHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = TheBOMPluginMod.MODID, version = TheBOMPluginMod.VERSION)
public class TheBOMPluginMod {
    public static final String MODID = "thebomplugin";
    public static final String VERSION = "0.1";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new BOMEventHandler());
    }
}
