package com.eightbitforest.thebomplugin;

import com.eightbitforest.thebomplugin.gui.util.GuiDrawables;
import com.eightbitforest.thebomplugin.plugin.BOMCategory;
import com.eightbitforest.thebomplugin.plugin.BOMRecipe;
import com.eightbitforest.thebomplugin.plugin.BOMWrapper;
import com.eightbitforest.thebomplugin.util.BOMCalculator;
import com.eightbitforest.thebomplugin.util.Recipes;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

@JEIPlugin
public class TheBOMPlugin implements IModPlugin
{
    public static String uid = "thebomplugin";

    private static TheBOMPlugin instance;

    private BOMCategory category;

    private IJeiRuntime runtime;
    private IIngredientRegistry ingredientRegistry;
    private GuiDrawables guiDrawables;

    public static TheBOMPlugin getInstance() {
        return instance;
    }

    public BOMCategory getCategory() {
        return category;
    }

    public IJeiRuntime getRuntime() {
        return runtime;
    }

    public IIngredientRegistry getIngredientRegistry() {
        return ingredientRegistry;
    }

    public GuiDrawables getGuiDrawables() {
        return guiDrawables;
    }

    public TheBOMPlugin(){
        instance = this;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        guiDrawables = new GuiDrawables(registry.getJeiHelpers());
        registry.addRecipeCategories(category = new BOMCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(Recipes.getValidRecipes(registry.getJeiHelpers()), uid);
        registry.handleRecipes(BOMRecipe.class, recipe -> new BOMWrapper(recipe, registry.getJeiHelpers()), uid);
        ingredientRegistry = registry.getIngredientRegistry();
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }
}
