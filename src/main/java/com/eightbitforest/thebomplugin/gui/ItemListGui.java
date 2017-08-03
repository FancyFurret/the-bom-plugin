package com.eightbitforest.thebomplugin.gui;

import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemListGui extends Gui {

    private static ItemListGui instance;
    private Minecraft minecraft;
    private int width;
    private int height;
    private float textScale;

    private int pixelPerLine = 12;

    private String[] lines;

    public ItemListGui(Minecraft minecraft) {
        instance = this;

        ScaledResolution scaled = new ScaledResolution(minecraft);
        this.minecraft = minecraft;
        width = scaled.getScaledWidth();
        height = scaled.getScaledHeight();
        textScale = TheBOMPluginMod.getInstance().getConfig().textScale;

        pixelPerLine *= textScale;
    }

    public void render() {
        if (lines == null)
            return;

        for (int i = 0; i < lines.length; i++) {

            GuiHelpers.drawSmallString(minecraft.fontRenderer, 10, ((height / 2) - 4 - (lines.length * pixelPerLine / 2)) + i * pixelPerLine, lines[i], textScale, false);
        }
    }

    public static void dismissItems() {
        instance.lines = null;
    }

    public static void showItems(List<List<ItemStack>> items) {
        instance.lines = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            instance.lines[i] = items.get(i).get(0).getCount() + " " + items.get(i).get(0).getDisplayName();
        }
    }
}
