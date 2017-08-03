package com.eightbitforest.thebomplugin.util;

import com.eightbitforest.thebomplugin.TheBOMPlugin;
import com.eightbitforest.thebomplugin.TheBOMPluginMod;
import com.eightbitforest.thebomplugin.gui.GuiHelpers;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BOMIngredientRenderer implements IIngredientRenderer<ItemStack> {

    private static final String style =
            TextFormatting.DARK_AQUA.toString();
    private static final String reset =
            TextFormatting.RESET.toString();
    private static float fontScale = -1f;

    public BOMIngredientRenderer() {
    }

    @Override
    public void render(Minecraft minecraft, int x, int y, @Nullable ItemStack itemStack) {
        if (itemStack != null) {

            if (fontScale == -1f) {
                fontScale = TheBOMPluginMod.getInstance().getConfig().textScale;
            }

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
        x = (int)(x + (16 * (1 - fontScale)));
        y = (int)(y + (16 * (1 - fontScale)));
        GuiHelpers.drawSmallString(fontRenderer, x, y, amount, fontScale, true);
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
            tip += stacks + reset + " stack(s) + " + style + remainder;
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
