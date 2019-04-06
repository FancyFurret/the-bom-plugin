package com.eightbitforest.thebomplugin.event;

import com.eightbitforest.thebomplugin.gui.ItemListGui;
import com.eightbitforest.thebomplugin.config.Keys;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BOMEventHandler {
    private ItemListGui gui;

    public BOMEventHandler(ItemListGui gui) {
        this.gui = gui;
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE)
            return;

        gui.render();
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        if (Keys.getScrollModifierDown() && event.getDwheel() != 0) {
            gui.onScroll(event.getDwheel() > 0 ? 1 : -1);
            event.setCanceled(true);
        }
    }
}
