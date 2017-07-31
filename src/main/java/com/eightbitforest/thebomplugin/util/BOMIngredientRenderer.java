package com.eightbitforest.thebomplugin.util;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

public class BOMIngredientRenderer implements IIngredientRenderer<ItemStack> {

    private static final String style =
            TextFormatting.DARK_AQUA.toString();
    private static final String reset =
            TextFormatting.RESET.toString();
    private static final float fontScale = .5f;

    @Override
    public void render(Minecraft minecraft, int x, int y, @Nullable ItemStack itemStack) {
        if (itemStack != null) {
            RenderHelper.enableGUIStandardItemLighting();
            FontRenderer fontRenderer = getFontRenderer(minecraft, itemStack);

            minecraft.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
            minecraft.getRenderItem().renderItemOverlayIntoGUI(fontRenderer, itemStack, x, y, "");
            drawItemAmount(fontRenderer, Integer.toString(itemStack.getCount()), x, y);

            GlStateManager.disableBlend();
            RenderHelper.disableStandardItemLighting();
        }
    }

    private void drawItemAmount(FontRenderer fontRenderer, String amount, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 1);
        GL11.glScalef(fontScale, fontScale, 1);

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();

        fontRenderer.drawStringWithShadow(amount, 30 - fontRenderer.getStringWidth(amount), 22, 16777215);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();

        GlStateManager.popMatrix();
    }

    @Override
    public List<String> getTooltip(Minecraft minecraft, ItemStack stack, ITooltipFlag tooltipFlag) {
        List<String> toolTip = stack.getTooltip(minecraft.player, tooltipFlag);

        int maxStackSize = stack.getMaxStackSize();
        int stackSize = stack.getCount();
        int stacks = stackSize / maxStackSize;
        int remainder = stackSize % maxStackSize;

        toolTip.add("You need:");
        String tip = style;

        if (stacks == 0) {
            tip += Integer.toString(remainder);
        } else if (remainder == 0) {
            tip += stacks + reset + " stack(s)";
        } else {
            tip += stacks + reset + " stacks(s) + " + style + remainder;
        }

        tip += reset;

        toolTip.add(tip);

        for (int k = 0; k < toolTip.size(); ++k) {
            if (k == 0) {
                toolTip.set(k, stack.getRarity().rarityColor + toolTip.get(k));
            } else {
                toolTip.set(k, TextFormatting.GRAY + toolTip.get(k));
            }
        }

        return toolTip;
    }
}
