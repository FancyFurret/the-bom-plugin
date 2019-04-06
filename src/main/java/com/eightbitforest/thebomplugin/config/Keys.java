package com.eightbitforest.thebomplugin.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class Keys {
    private static KeyBinding scrollModifier;

    private Keys() {

    }

    public static void register() {
        scrollModifier = new KeyBinding(Constants.BOM_SCROLL_KEYBIND_NAME, Keyboard.KEY_LMENU, Constants.BOM_KEYBIND_CATEGORY);
        ClientRegistry.registerKeyBinding(scrollModifier);
    }

    public static boolean getScrollModifierDown() {
        return scrollModifier.isKeyDown();
    }
}
