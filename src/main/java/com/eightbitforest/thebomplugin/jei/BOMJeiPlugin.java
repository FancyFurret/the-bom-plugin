package com.eightbitforest.thebomplugin.jei;

import com.eightbitforest.thebomplugin.gui.util.GuiDrawables;
import com.eightbitforest.thebomplugin.config.Constants;
import com.eightbitforest.thebomplugin.util.RecipeUtil;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class BOMJeiPlugin implements IModPlugin
{
    static final String UID = Constants.MOD_ID;

    private static BOMJeiPlugin instance;

    private BOMCategory category;

    private IJeiRuntime runtime;
    private IIngredientRegistry ingredientRegistry;
    private GuiDrawables guiDrawables;

    public static BOMJeiPlugin getInstance() {
        return instance;
    }

    BOMCategory getCategory() {
        return category;
    }

    public IJeiRuntime getRuntime() {
        return runtime;
    }

    public IIngredientRegistry getIngredientRegistry() {
        return ingredientRegistry;
    }

    GuiDrawables getGuiDrawables() {
        return guiDrawables;
    }

    public BOMJeiPlugin(){
        instance = this;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        guiDrawables = new GuiDrawables(registry.getJeiHelpers());
        registry.addRecipeCategories(category = new BOMCategory());
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(RecipeUtil.getValidRecipes(registry.getJeiHelpers()), UID);
        registry.handleRecipes(BOMRecipe.class, BOMWrapper::new, UID);
        ingredientRegistry = registry.getIngredientRegistry();
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }
}
