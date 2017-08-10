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
    public boolean showRemainingItemsInTracker;

    private static final String GENERAL = "general";

    public List<String> baseItems;
    public List<String> recipeItemBlacklist;

    public BOMConfig(File configFile) {
        config = new Configuration(configFile, "0.2.1");
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
        showRemainingItemsInTracker = config.getBoolean("showRemainingItemsInTracker", GUI, true, "True will make the GUI tracker show how many more items you need. False will show the total amount needed");

        baseItems = new ArrayList<>(Arrays.asList(config.getStringList("baseItems", GENERAL, baseItemsDefault, "(Regex) Items that will not be broken down into their components. You may add an @ symbol at the end followed by a damage value to target an item with that specific damage value")));
        recipeItemBlacklist = new ArrayList<>(Arrays.asList(config.getStringList("recipeItemBlacklist", GENERAL, recipeItemBlacklistDefault, "(Regex) Recipes containing these items will not be used. You may add an @ symbol at the end followed by a damage value to target an item with that specific damage value")));

        // Update with missing default // TODO: Don't re-add things users have removed
        String loadedVersion = config.getLoadedConfigVersion();
        if (loadedVersion == null || !loadedVersion.equals(config.getDefinedConfigVersion())) {
            for (String defaultItem : baseItemsDefault) {
                if (!baseItems.contains(defaultItem)) {
                    baseItems.add(defaultItem);
                }
            }
            for (String defaultItem : recipeItemBlacklistDefault) {
                if (!recipeItemBlacklist.contains(defaultItem)) {
                    recipeItemBlacklist.add(defaultItem);
                }
            }
            // Save new config items
            String[] newBaseItems = baseItems.toArray(new String[baseItems.size()]);
            config.get(GENERAL, "baseItems", newBaseItems).set(newBaseItems);
            String[] newRecipeItemBlacklist = recipeItemBlacklist.toArray(new String[recipeItemBlacklist.size()]);
            config.get(GENERAL, "recipeItemBlacklist", newRecipeItemBlacklist).set(newRecipeItemBlacklist);
        }

        if (config.hasChanged() || loadedVersion == null || !loadedVersion.equals(config.getDefinedConfigVersion())) {
            config.save();
        }
    }

    // TODO: Changes
    // Machine casing block that gives 8 iron
    // Basic items that don't need to be broken down
    private static String[] baseItemsDefault = new String[]{
            "^minecraft:stick$",
            "^minecraft:torch$",
            "^minecraft:leather$",
            "^minecraft:paper$",
            "^minecraft:wool$",
            "^minecraft:planks$",
            "^minecraft:netherrack$",
            "^actuallyadditions:item_crystal$",
            "^actuallyadditions:item_crystal_empowered$",

            // Added in 0.2.0
            "^minecraft:dye$",
            "^minecraft:flint$",
            "^minecraft:obsidian$",
            "^minecraft:.*_bucket$",

            // 0.2.1
            "^minecraft:wool$",
	        "^minecraft:glowstone_dust$",
            "^minecraft:.*_bottle$",
            "^minecraft:elytra$",
            "^minecraft:string$",
            "^minecraft:string$",
            "^harvestcraft:cuttingboarditem$",
            "^harvestcraft:potitem",
            "^harvestcraft:skilletitem",
            "^harvestcraft:saucepanitem",
            "^harvestcraft:bakewareitem",
            "^harvestcraft:mortarandpestleitem",
            "^harvestcraft:mixingbowlitem",
            "^harvestcraft:juiceritem",
    };

    // Items that should not be in a recipe
    private static String[] recipeItemBlacklistDefault = new String[]{
            "^thermalfoundation:material$@864", // TE dusts
            "^thermalfoundation:material$@1024", // TE dusts
            "^thermalfoundation:material$@1025", // TE dusts
            "^thermalfoundation:material$@1026", // TE dusts
            "^thermalfoundation:material$@1027", // TE dusts
            "^thermalfoundation:material$@1028", // TE dusts
            "^mysticalagriculture:.*_essence$", // MA essences
            "^techreborn:uumatter$",

            // Added in 0.2.0
            "^mysticalagradditions:stuff$",
            "^ic2:crafting$@38" // Industrial credits
    };
}
