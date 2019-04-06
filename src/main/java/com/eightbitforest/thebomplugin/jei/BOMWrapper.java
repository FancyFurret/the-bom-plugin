package com.eightbitforest.thebomplugin.jei;

import com.eightbitforest.thebomplugin.TheBOMPlugin;
import com.eightbitforest.thebomplugin.gui.ItemListGui;
import com.eightbitforest.thebomplugin.gui.button.GuiIconButton;
import com.eightbitforest.thebomplugin.config.Constants;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class BOMWrapper implements ICraftingRecipeWrapper {
    private BOMRecipe recipe;

    private GuiButton trackButton;
    private GuiButton increaseOutputButton;
    private GuiButton decreaseOutputButton;

    // TODO: Use item list manager
    private ItemListGui itemListGui;

    BOMWrapper(BOMRecipe recipe) {
        this.recipe = recipe;

        this.trackButton = new GuiButton(0, 109, 90, 55, 20, "");
        this.increaseOutputButton = new GuiIconButton(1, 95, 90, 10, 20,
                BOMJeiPlugin.getInstance().getGuiDrawables().getArrowNext());
        this.decreaseOutputButton = new GuiIconButton(2, 57, 90, 10, 20,
                BOMJeiPlugin.getInstance().getGuiDrawables().getArrowPrevious());

        itemListGui = TheBOMPlugin.getInstance().getItemListGui();

        updateDecreaseButton();
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        recipe.getIngredients(ingredients);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        trackButton.displayString =
                itemListGui.isGuiOpen() ? Constants.LANG_BUTTON_UNTRACK : Constants.LANG_BUTTON_TRACK;

        trackButton.drawButton(minecraft, mouseX, mouseY, 1f);
        increaseOutputButton.drawButton(minecraft, mouseX, mouseY, 1f);
        decreaseOutputButton.drawButton(minecraft, mouseX, mouseY, 1f);
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (trackButton.mousePressed(minecraft, mouseX, mouseY)) {
            trackButton.playPressSound(minecraft.getSoundHandler());
            if (itemListGui.isGuiOpen())
                itemListGui.dismissItems();
            else
                itemListGui.showItems(BOMJeiPlugin.getInstance().getCategory().getBaseIngredients());

            return true;
        }

        if (increaseOutputButton.mousePressed(minecraft, mouseX, mouseY)) {
            increaseOutputButton.playPressSound(minecraft.getSoundHandler());
            BOMJeiPlugin.getInstance().getCategory().increaseOutput(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
            if (itemListGui.isGuiOpen())
                itemListGui.showItems(BOMJeiPlugin.getInstance().getCategory().getBaseIngredients());

            updateDecreaseButton();
            return true;
        }

        if (decreaseOutputButton.mousePressed(minecraft, mouseX, mouseY)) {
            decreaseOutputButton.playPressSound(minecraft.getSoundHandler());
            BOMJeiPlugin.getInstance().getCategory().decreaseOutput(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
            if (itemListGui.isGuiOpen())
                itemListGui.showItems(BOMJeiPlugin.getInstance().getCategory().getBaseIngredients());

            updateDecreaseButton();
            return true;
        }

        return false;
    }

    void updateDecreaseButton() {
        decreaseOutputButton.enabled = BOMJeiPlugin.getInstance().getCategory().getOutputAmount() > 1;
    }
}
