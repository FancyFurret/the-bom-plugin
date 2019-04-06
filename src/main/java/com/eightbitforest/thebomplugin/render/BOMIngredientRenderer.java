package com.eightbitforest.thebomplugin.render;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public class BOMIngredientRenderer implements IIngredientRenderer<ItemStack> {

    private static final String style = TextFormatting.DARK_AQUA.toString();
    private static final String reset = TextFormatting.RESET.toString();

    @Override
    public void render(@Nonnull Minecraft minecraft, int x, int y, @Nullable ItemStack ingredient) {
        RenderUtils.renderItemStackWithSmallFont(minecraft, x, y, ingredient);
    }

    @Nonnull
    @Override
    public List<String> getTooltip(Minecraft minecraft, ItemStack stack, ITooltipFlag tooltipFlag) {
        List<String> toolTip = stack.getTooltip(minecraft.player, tooltipFlag);

        int maxStackSize = stack.getMaxStackSize();
        int stackSize = stack.getCount();
        int stacks = stackSize / maxStackSize;
        int remainder = stackSize % maxStackSize;

        toolTip.add(TextFormatting.GRAY + "You need:");
        String tip = style;

        if (stacks == 0)
            tip += Integer.toString(remainder);
        else if (remainder == 0)
            tip += stacks + reset + " stack(s) of " + maxStackSize;
        else
            tip += stacks + reset + " stack(s) of " + maxStackSize + " + " + style + remainder;

        tip += reset;

        toolTip.add(tip);

        return toolTip;
    }
}
