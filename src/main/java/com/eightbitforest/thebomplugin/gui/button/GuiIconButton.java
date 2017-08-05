package com.eightbitforest.thebomplugin.gui.button;

import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiIconButton extends GuiButton {
    private final IDrawable icon;

    public GuiIconButton(int buttonId, int x, int y, int width, int height, IDrawable icon) {
        super(buttonId, x, y, width, height, "");
        this.icon = icon;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        if (this.visible) {
            int xOffset = x + (width - this.icon.getWidth()) / 2;
            int yOffset = y + (height - this.icon.getHeight()) / 2;
            this.icon.draw(mc, xOffset, yOffset);
        }
    }
}
