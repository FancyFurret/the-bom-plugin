package com.eightbitforest.thebomplugin;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.omg.PortableServer.THREAD_POLICY_ID;

@JEIPlugin
public class TheBOMPlugin implements IModPlugin
{
    protected static String uid = "bomplugin";
    private IJeiRuntime runtime;
    private IIngredientRegistry ingredientRegistry;
    private static TheBOMPlugin instance;

    public static TheBOMPlugin getInstance() {
        return instance;
    }

    public IJeiRuntime getRuntime() {
        return runtime;
    }

    public IIngredientRegistry getIngredientRegistry() {
        return ingredientRegistry;
    }

    public TheBOMPlugin(){
        instance = this;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new BOMCategory());
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(CraftingRecipeChecker.getValidRecipes(registry.getJeiHelpers()), uid);
        registry.handleRecipes(BOMRecipe.class, recipe -> {
//            System.out.println("Found " + recipe.output.getDisplayName());
            return new BOMWrapper(recipe, registry.getJeiHelpers());
        }, uid);
        ingredientRegistry = registry.getIngredientRegistry();
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
//        jeiRuntime.getRecipeRegistry().getRecipeWrappers(jeiRuntime.getRecipeRegistry().getre)
    }
}

//public class TheBOMPlugin
//{
//    public static final String MODID = "thebomplugin";
//    public static final String VERSION = "1.0";
//
//    @EventHandler
//    public void init(FMLInitializationEvent event)
//    {
//        // some example code
//        System.out.println("DIRT BLOCK >> "+Blocks.DIRT.getUnlocalizedName());
//    }
//}

