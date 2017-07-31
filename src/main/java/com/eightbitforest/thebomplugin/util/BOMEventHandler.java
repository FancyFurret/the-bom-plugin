package com.eightbitforest.thebomplugin.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class BOMEventHandler {

    private static final String style =
            TextFormatting.DARK_AQUA.toString();
    private static final String reset =
            TextFormatting.RESET.toString();

    @SubscribeEvent
    public void itemTooltipEvent(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getTagCompound() != null) {
            if (stack.getTagCompound().hasKey(Constants.BOM_ITEMSTACK_NBT_MARK)) {
                List<String> toolTip = event.getToolTip();
                int maxStackSize = stack.getMaxStackSize();
                int stackSize = stack.getCount();
                int stacks = stackSize / maxStackSize;
                int remainder = stackSize % maxStackSize;

                toolTip.add("You need:");
                String tip = style;

                if (stacks == 0) {
                    tip += Integer.toString(remainder);
                }
                else if (remainder == 0){
                    tip += stacks + reset + " stack(s)";
                }
                else {
                    tip += stacks + reset + " stacks(s) + " + style + remainder;
                }

                tip += reset;

                toolTip.add(tip);
            }
        }
    }
}
