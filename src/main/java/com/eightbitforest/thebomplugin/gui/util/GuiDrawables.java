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
        categoryIcon = jeiHelpers.getGuiHelper().drawableBuilder(Constants.BOM_ICON, 0, 0, 16, 16)
                .setTextureSize(16, 16).build();

        arrowNext = jeiHelpers.getGuiHelper().drawableBuilder(Constants.BOM_ARROW_NEXT, 0, 0, 9, 9)
                .setTextureSize(9, 9).trim(0, 0, 1, 1).build();
        arrowPrevious = jeiHelpers.getGuiHelper().drawableBuilder(Constants.BOM_ARROW_PREV, 0, 0, 9, 9)
                .setTextureSize(9, 9).trim(0, 0, 1, 1).build();
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
