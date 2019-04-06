package com.eightbitforest.thebomplugin;

import com.eightbitforest.thebomplugin.config.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod(modid = Constants.MOD_ID, version = Constants.VERSION, dependencies = "required-after:jei", clientSideOnly = true)
public class TheBOMPluginMod {
    private TheBOMPlugin theBOMPlugin;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        theBOMPlugin = new TheBOMPlugin();
        theBOMPlugin.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        theBOMPlugin.postInit(e);
    }
}
