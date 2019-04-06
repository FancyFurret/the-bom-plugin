package com.eightbitforest.thebomplugin.config;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class Constants {
    private Constants() {

    }

    public static final String MOD_ID = "thebomplugin";
    public static final String VERSION = "@VERSION";

    public static final ResourceLocation BOM_BACKGROUND = getResourceLocation("textures/gui/bombackground.png");
    public static final ResourceLocation BOM_ICON = getResourceLocation("textures/gui/tabicon.png");
    public static final ResourceLocation BOM_INGAME_GUI = getResourceLocation("textures/gui/ingamegui.png");
    public static final ResourceLocation BOM_ARROW_NEXT = getResourceLocation("textures/gui/icons/arrow_next.png");
    public static final ResourceLocation BOM_ARROW_PREV = getResourceLocation("textures/gui/icons/arrow_previous.png");

    public static final String BOM_KEYBIND_CATEGORY = "key.categories.thebomplugin";
    public static final String BOM_SCROLL_KEYBIND_NAME = "key.thebomplugin.scrollmodifier";

    public static final String LANG_BUTTON_TRACK = I18n.format("button.track.name");
    public static final String LANG_BUTTON_UNTRACK = I18n.format("button.untrack.name");

    private static ResourceLocation getResourceLocation(String location) {
        return new ResourceLocation(MOD_ID, location);
    }
}