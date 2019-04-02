package com.eightbitforest.thebomplugin.plugin;

import com.eightbitforest.thebomplugin.TheBOMPlugin;
import com.eightbitforest.thebomplugin.gui.button.GuiIconButton;
import com.eightbitforest.thebomplugin.jei.ingredients.Ingredients;
import com.eightbitforest.thebomplugin.gui.ItemListGui;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class BOMWrapper implements ICraftingRecipeWrapper {

    private BOMRecipe recipe;
    private IJeiHelpers jeiHelpers;

    private GuiButton hudListButton;
    private GuiButton increaseOutputButton;
    private GuiButton decreaseOutputButton;

    private String trackString;
    private String untrackString;

    public BOMWrapper(BOMRecipe recipe, IJeiHelpers helpers) {
        this.recipe = recipe;
        this.jeiHelpers = helpers;
        this.hudListButton = new GuiButton(0, 109, 90, 55, 20, "");
        this.increaseOutputButton = new GuiIconButton(1, 95, 90, 10, 20, TheBOMPlugin.getInstance().getGuiDrawables().getArrowNext());
        this.decreaseOutputButton = new GuiIconButton(2, 57, 90, 10, 20, TheBOMPlugin.getInstance().getGuiDrawables().getArrowPrevious());
        updateDecreaseButton();

        trackString = I18n.format("button.track.name");
        untrackString = I18n.format("button.untrack.name");
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.inputs);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        if (ItemListGui.isGuiOpen()) {
            hudListButton.displayString = untrackString;
        } else {
            hudListButton.displayString = trackString;
        }
        hudListButton.drawButton(minecraft, mouseX, mouseY, 1f);
        increaseOutputButton.drawButton(minecraft, mouseX, mouseY, 1f);
        decreaseOutputButton.drawButton(minecraft, mouseX, mouseY, 1f);
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (hudListButton.mousePressed(minecraft, mouseX, mouseY)) {
            hudListButton.playPressSound(minecraft.getSoundHandler());
            if (ItemListGui.isGuiOpen()) {
                ItemListGui.dismissItems();
            } else {
//                Ingredients i = new Ingredients();
//                getIngredients(i);
                ItemListGui.showItems(TheBOMPlugin.getInstance().getCategory().getBaseIngredients());
            }
            return true;
        }
        if (increaseOutputButton.mousePressed(minecraft, mouseX, mouseY)) {
            increaseOutputButton.playPressSound(minecraft.getSoundHandler());
            TheBOMPlugin.getInstance().getCategory().increaseOutput(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
            if (ItemListGui.isGuiOpen()) {
                ItemListGui.showItems(TheBOMPlugin.getInstance().getCategory().getBaseIngredients());
            }
            updateDecreaseButton();
            return true;
        }
        if (decreaseOutputButton.mousePressed(minecraft, mouseX, mouseY)) {
            decreaseOutputButton.playPressSound(minecraft.getSoundHandler());
            TheBOMPlugin.getInstance().getCategory().decreaseOutput(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
            if (ItemListGui.isGuiOpen()) {
                ItemListGui.showItems(TheBOMPlugin.getInstance().getCategory().getBaseIngredients());
            }
            updateDecreaseButton();
            return true;
        }

        return false;
    }

    public void updateDecreaseButton() {
        decreaseOutputButton.enabled = TheBOMPlugin.getInstance().getCategory().getOutputAmount() > 1;
    }
}
