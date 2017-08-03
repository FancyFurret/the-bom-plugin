package com.eightbitforest.thebomplugin.gui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
}
