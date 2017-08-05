package com.eightbitforest.thebomplugin.render;

import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import com.eightbitforest.thebomplugin.gui.util.GuiHelpers;
import com.eightbitforest.thebomplugin.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {
    private static float fontScale = -1f;
    public static void renderItemStackWithSmallFont(Minecraft minecraft, int x, int y, ItemStack itemStack) {
        if (itemStack != null) {
            if (fontScale == -1f) {
                fontScale = TheBOMPluginMod.getInstance().getConfig().textScale;
            }
            renderItemStackWithSmallFont(minecraft, x, y, itemStack, Integer.toString(itemStack.getCount()), 16777215, fontScale);
        }
    }

    public static void renderItemStackWithSmallFont(Minecraft minecraft, int x, int y, ItemStack itemStack, String amount, int color, float fontScale) {
        if (itemStack != null) {
            RenderHelper.enableGUIStandardItemLighting();
            FontRenderer fontRenderer = minecraft.fontRenderer;

            minecraft.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
            minecraft.getRenderItem().renderItemOverlayIntoGUI(fontRenderer, itemStack, x, y, "");
            drawItemAmount(fontRenderer, amount, x, y, color, fontScale);

            GlStateManager.disableBlend();
            RenderHelper.disableStandardItemLighting();
        }
    }


    private static void drawItemAmount(FontRenderer fontRenderer, String amount, int x, int y) {
        drawItemAmount(fontRenderer, amount, x, y, 16777215, fontScale);
    }

    private static void drawItemAmount(FontRenderer fontRenderer, String amount, int x, int y, int color, float fontScale) {
        try {
            int amountNumber = Integer.parseInt(amount);
            amount = shortenAmount(amount, amountNumber);
        } catch (NumberFormatException ignored) {}

        x = (int)(x + (16 * (1 - fontScale)));
        y = (int)(y + (16 * (1 - fontScale)));
        GuiHelpers.drawSmallString(fontRenderer, x, y, amount, fontScale, true, color);
    }

    private static String shortenAmount(String amount, int amountNumber) {
        if (amount.length() <= 5) {
            return amount;
        }
        return Utils.formatLong(amountNumber);
    }
}
