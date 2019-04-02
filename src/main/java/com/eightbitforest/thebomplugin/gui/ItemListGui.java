package com.eightbitforest.thebomplugin.gui;

import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import com.eightbitforest.thebomplugin.event.IInventoryChangedEventListener;
import com.eightbitforest.thebomplugin.gui.util.ItemListGuiItemStack;
import com.eightbitforest.thebomplugin.render.RenderUtils;
import com.eightbitforest.thebomplugin.util.Constants;
import com.eightbitforest.thebomplugin.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ItemListGui extends Gui implements IInventoryChangedEventListener {

    private static ItemListGui instance;
    private Minecraft minecraft;
    private TextureManager textureManager;
    private int width;
    private int height;
    private float textScale;
    private boolean isGuiOpen = false;
    private boolean isScrollbarShown = false;
    private ResourceLocation guiBackground;
    private List<ItemListGuiItemStack> baseItems;
    private int topItem = 0;

    private final int textureOffsetX = 1;
    private final int textureOffsetY = 1;

    private final int checkmarkOffsetX = 20;
    private final int checkmarkOffsetY = 2;
    private final int checkmarkX = 46;
    private final int checkmarkY = 0;
    private final int checkmarkWidth = 16;
    private final int checkmarkHeight = 16;

    private final int scrollBarStartX = 18;
    private final int scrollBarStartY = 1;
    private final int scrollBarX = 25;
    private final int scrollBarY = 0;
    private final int scrollBarWidth = 6;
    private final int scrollBarHeight = 9;

    public ItemListGui(Minecraft minecraft) {
        instance = this;

        ScaledResolution scaled = new ScaledResolution(minecraft);
        this.minecraft = minecraft;
        textureManager = minecraft.getTextureManager();
        width = scaled.getScaledWidth();
        height = scaled.getScaledHeight();
        textScale = TheBOMPluginMod.getInstance().getConfig().textScale;

        guiBackground = Constants.BOM_INGAME_GUI;

        baseItems = new ArrayList<>();
    }

    public static boolean isGuiOpen() {
        return instance.isGuiOpen;
    }

    public void render() {
        if (!instance.isGuiOpen)
            return;

        textureManager.bindTexture(guiBackground);
        int baseY = ((height / 2) - (154/2));
        int width = isScrollbarShown ? 25 : 18;
        drawTexturedModalRect(0, baseY, 0, 0, width, 154);

        if (isScrollbarShown) {
            int scrollBarOffset = 0;
            if (topItem != 0) {
                scrollBarOffset = (int)(((float)topItem / (baseItems.size() - 9)) * (154 - scrollBarHeight - 2));
            }
            drawTexturedModalRect(scrollBarStartX, baseY + scrollBarStartY + scrollBarOffset, scrollBarX, scrollBarY, scrollBarWidth, scrollBarHeight);
        }

        for (int i = 0; i < Math.min(baseItems.size(), 9); i++) {
            ItemListGuiItemStack stack = baseItems.get(i + topItem);

            int x = textureOffsetX;
            int y = textureOffsetY + baseY + i * 16 + (i * 1);

            if (stack.doesInventoryHaveEnough())
                RenderUtils.renderItemStackWithSmallFont(minecraft, x, y, stack.getCurrentStack(), "\u2714", 0x83f442, textScale);
            else {
                if (TheBOMPluginMod.getInstance().getConfig().showRemainingItemsInTracker) {
                    RenderUtils.renderItemStackWithSmallFont(minecraft, x, y, stack.getCurrentStack(), Integer.toString(stack.getRemainingNeeded()), 16777215, textScale);
                }
                else {
                    RenderUtils.renderItemStackWithSmallFont(minecraft, x, y, stack.getCurrentStack());
                }
            }
        }
    }

    public static void dismissItems() {
        instance.isGuiOpen = false;
        instance.baseItems.clear();
        TheBOMPluginMod.getInstance().getInventoryChangedEvent().disable();
    }

    public static void showItems(List<List<ItemStack>> items) {
        instance.isGuiOpen = true;
        instance.baseItems.clear();
        instance.topItem = 0;

        TheBOMPluginMod.getInstance().getInventoryChangedEvent().enable();

        for (List<ItemStack> item : items)
            instance.baseItems.add(new ItemListGuiItemStack(item));

        instance.onInventoryChanged(instance.minecraft.player.inventory);

        instance.isScrollbarShown = items.size() > 9;
    }

    @Override
    public void onInventoryChanged(InventoryPlayer inventory) {
        if (instance.isGuiOpen) {
            for (ItemListGuiItemStack itemListGuiItemStack : baseItems) {
                itemListGuiItemStack.setInventoryAmount(inventory);
            }
        }
    }

    public void onScroll(int direction) {
        if (isGuiOpen) {
            if (isScrollbarShown) {
                topItem = Utils.clamp(topItem - direction, 0, baseItems.size() - 9);
            }
        }
    }
}
