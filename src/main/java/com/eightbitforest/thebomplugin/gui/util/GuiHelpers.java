package com.eightbitforest.thebomplugin.gui.util;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiHelpers {
    public static void drawSmallString(FontRenderer fontRenderer, int x, int y, String text, float fontScale, boolean rightAlign) {
        drawSmallString(fontRenderer, x, y, text, fontScale, rightAlign, 16777215);
    }

    public static void drawSmallString(FontRenderer fontRenderer, int x, int y, String text, float fontScale, boolean rightAlign, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 1);
        GL11.glScalef(fontScale, fontScale, 1);

        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.disableBlend();

        fontRenderer.drawStringWithShadow(text, rightAlign ? 17 - fontRenderer.getStringWidth(text) : 0, 9, color);

        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        GlStateManager.enableBlend();

        GlStateManager.popMatrix();
    }
}
