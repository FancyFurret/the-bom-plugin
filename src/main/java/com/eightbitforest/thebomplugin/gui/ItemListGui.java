package com.eightbitforest.thebomplugin.gui;

import com.eightbitforest.thebomplugin.event.IInventoryChangedEventListener;
import com.eightbitforest.thebomplugin.gui.util.ItemListGuiItemStack;
import com.eightbitforest.thebomplugin.render.RenderUtils;
import com.eightbitforest.thebomplugin.config.BOMConfig;
import com.eightbitforest.thebomplugin.config.Constants;
import com.eightbitforest.thebomplugin.util.ItemStackUtil;
import com.eightbitforest.thebomplugin.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class ItemListGui extends Gui implements IInventoryChangedEventListener {
    private static final int TEXTURE_OFFSET_X = 1;
    private static final int TEXTURE_OFFSET_Y = 1;

    private static final int SCROLL_BAR_START_X = 18;
    private static final int SCROLL_BAR_START_Y = 1;
    private static final int SCROLL_BAR_X = 25;
    private static final int SCROLL_BAR_Y = 0;
    private static final int SCROLL_BAR_WIDTH = 6;
    private static final int SCROLL_BAR_HEIGHT = 9;

    private Minecraft minecraft;
    private TextureManager textureManager;

    private int height;
    private float textScale;

    private boolean isGuiOpen = false;
    private boolean isScrollbarShown = false;

    private ResourceLocation guiBackground;
    private List<ItemListGuiItemStack> baseItems;
    private int topItem = 0;

    public ItemListGui(Minecraft minecraft) {
        ScaledResolution scaled = new ScaledResolution(minecraft);
        this.minecraft = minecraft;
        textureManager = minecraft.getTextureManager();
        height = scaled.getScaledHeight();
        textScale = BOMConfig.getTextScale();

        guiBackground = Constants.BOM_INGAME_GUI;

        baseItems = new ArrayList<>();
    }

    public boolean isGuiOpen() {
        return isGuiOpen;
    }

    public void render() {
        if (!isGuiOpen)
            return;

        textureManager.bindTexture(guiBackground);
        int baseY = ((height / 2) - (154 / 2));
        int width = isScrollbarShown ? 25 : 18;
        drawTexturedModalRect(0, baseY, 0, 0, width, 154);

        if (isScrollbarShown) {
            int scrollBarOffset = 0;
            if (topItem != 0)
                scrollBarOffset = (int) (((float) topItem / (baseItems.size() - 9)) * (154 - SCROLL_BAR_HEIGHT - 2));

            drawTexturedModalRect(SCROLL_BAR_START_X, baseY + SCROLL_BAR_START_Y + scrollBarOffset,
                    SCROLL_BAR_X, SCROLL_BAR_Y, SCROLL_BAR_WIDTH, SCROLL_BAR_HEIGHT);
        }

        for (int i = 0; i < Math.min(baseItems.size(), 9); i++) {
            ItemListGuiItemStack stack = baseItems.get(i + topItem);

            int x = TEXTURE_OFFSET_X;
            int y = TEXTURE_OFFSET_Y + baseY + i * 16 + i;

            if (stack.doesInventoryHaveEnough()) {
                RenderUtils.renderItemStackWithSmallFont(minecraft, x, y, stack.getCurrentStack(),
                        "\u2714", 0x83f442, textScale);
            } else {
                if (BOMConfig.getShowRemainingItemsInTracker())
                    RenderUtils.renderItemStackWithSmallFont(minecraft, x, y, stack.getCurrentStack(),
                            Integer.toString(stack.getRemainingNeeded()), 16777215, textScale);
                else
                    RenderUtils.renderItemStackWithSmallFont(minecraft, x, y, stack.getCurrentStack());
            }
        }
    }

    public void dismissItems() {
        isGuiOpen = false;
        baseItems.clear();
    }

    public void showItems(List<List<ItemStack>> items) {
        isGuiOpen = true;
        baseItems.clear();
        topItem = 0;

        for (List<ItemStack> item : items)
            baseItems.add(new ItemListGuiItemStack(item));

        onInventoryChanged(minecraft.player.inventory);

        isScrollbarShown = items.size() > 9;
    }

    @Override
    public void onInventoryChanged(InventoryPlayer inventory) {
        if (isGuiOpen)
            for (ItemListGuiItemStack itemListGuiItemStack : baseItems)
                itemListGuiItemStack.setInventoryAmount(inventory);
    }

    public void onScroll(int direction) {
        if (isGuiOpen && isScrollbarShown)
            topItem = MathUtil.clamp(topItem - direction, 0, baseItems.size() - 9);
    }
}
