package com.eightbitforest.thebomplugin.gui.util;

import com.eightbitforest.thebomplugin.util.Constants;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.IDrawable;

public class GuiDrawables {

    private final IDrawable categoryBackground;
    private final IDrawable categoryIcon;

    private final IDrawable arrowNext;
    private final IDrawable arrowPrevious;

    public GuiDrawables(IJeiHelpers jeiHelpers) {
        categoryBackground = jeiHelpers.getGuiHelper().createDrawable(Constants.BOM_BACKGROUND, 0, 0, 163, 119);
        categoryIcon = jeiHelpers.getGuiHelper().createDrawable(Constants.BOM_ICON, 0, 0, 16, 16, 16, 16);

        arrowNext = jeiHelpers.getGuiHelper().createDrawable(Constants.JEI_RECIPE_BACKGROUND, 204, 55, 5, 8, 1, 0, 1, 0);
        arrowPrevious = jeiHelpers.getGuiHelper().createDrawable(Constants.JEI_RECIPE_BACKGROUND, 196, 55, 5, 8, 1, 0, 1, 0);
    }

    public IDrawable getCategoryBackground() {
        return categoryBackground;
    }

    public IDrawable getCategoryIcon() {
        return categoryIcon;
    }

    public IDrawable getArrowNext() {
        return arrowNext;
    }

    public IDrawable getArrowPrevious() {
        return arrowPrevious;
    }
}
