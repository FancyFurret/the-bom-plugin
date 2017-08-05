package com.eightbitforest.thebomplugin.util;

import com.eightbitforest.thebomplugin.TheBOMPlugin;
import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.util.ResourceLocation;

public class Constants {
    public static final String BOM_BACKGROUND_TEXTURE = "textures/gui/bombackground.png";
    public static final ResourceLocation BOM_BACKGROUND = new ResourceLocation(TheBOMPluginMod.MODID, BOM_BACKGROUND_TEXTURE);
    public static final String BOM_TAB_ICON_TEXTURE = "textures/gui/tabicon.png";
    public static final ResourceLocation BOM_ICON = new ResourceLocation(TheBOMPluginMod.MODID, BOM_TAB_ICON_TEXTURE);

    public static final String BOM_INGAME_GUI_TEXTURE = "textures/gui/ingamegui.png";
    public static final ResourceLocation BOM_INGAME_GUI = new ResourceLocation(TheBOMPluginMod.MODID, BOM_INGAME_GUI_TEXTURE);

    public static final String BOM_KEYBIND_CATEGORY = "key.categories.thebomplugin";
    public static final String BOM_SCROLL_KEYBIND_NAME = "key.thebomplugin.scrollmodifier";

    public static final String JEI_RECIPE_BACKGROUND_TEXTURE = "textures/gui/recipe_background.png";
    public static final ResourceLocation JEI_RECIPE_BACKGROUND = new ResourceLocation("jei", JEI_RECIPE_BACKGROUND_TEXTURE);
}
