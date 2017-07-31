package com.eightbitforest.thebomplugin.util;

import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.tools.nsc.backend.icode.Primitives;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BOMConfig {
    private Configuration config;

    private static final String GUI = "gui";
    public float textScale;

    private static final String GENERAL = "general";
    public String[] baseItems;
    public String[] recipeItemBlacklist;

    public BOMConfig(File configFile) {
        config = new Configuration(configFile);
        MinecraftForge.EVENT_BUS.register(this);
        loadConfig();
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equalsIgnoreCase(TheBOMPluginMod.MODID)) {
            loadConfig();
        }
    }

    private void loadConfig() {
        textScale = config.getFloat("textScale", GUI, .5f, 0f, 1f, "A float between 0 and 1 that scales the font size used to display item amounts");

        baseItems = config.getStringList("baseItems", GENERAL, baseItemsDefault, "(Regex) Items that will not be broken down into their components");
        recipeItemBlacklist = config.getStringList("recipeItemBlacklist", GENERAL, recipeItemBlacklistDefault, "(Regex) Recipes containing these items will not be used");

        if (config.hasChanged()) {
            config.save();
        }
    }

    // Basic items that don't need to be broken down
    private static String[] baseItemsDefault = new String[]{
            "^minecraft:stick$",
            "^minecraft:torch$",
            "^minecraft:leather$",
            "^minecraft:paper$",
            "^minecraft:wool$"
    };

    // Items that should not be in a recipe
    private static String[] recipeItemBlacklistDefault = new String[]{
            "^thermalfoundation:material$", // TE dusts/ingots
            "^mysticalagriculture:.*_essence$", // MA essences
            "^techreborn:uumatter$"
    };
}
