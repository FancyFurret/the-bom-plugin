package com.eightbitforest.thebomplugin.event;

import com.eightbitforest.thebomplugin.gui.ItemListGui;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class BOMGuiEventHandler {

    private ItemListGui gui;
    private Minecraft minecraft;

    public BOMGuiEventHandler(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.gui = new ItemListGui(minecraft);
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE)
            return;

        gui.render();
    }

    public ItemListGui getItemListGui() {
        return gui;
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LMENU) && event.getDwheel() != 0) {
            gui.onScroll(event.getDwheel() > 0 ? 1 : -1);
            event.setCanceled(true);
        }

    }
}
