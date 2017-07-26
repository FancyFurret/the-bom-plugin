package com.eightbitforest.thebomplugin;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;

@JEIPlugin
public class TheBOMPlugin implements IModPlugin
{
    protected static String uid = "bomplugin";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new BOMCategory());
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipes(CraftingRecipeChecker.getValidRecipes(registry.getJeiHelpers()), uid);
        registry.handleRecipes(BOMRecipe.class, recipe -> {
            System.out.println("Found " + recipe.output.getDisplayName());
            return new BOMWrapper(recipe, registry.getJeiHelpers());
        }, uid);

        registry.addRecipeCatalyst(new ItemStack(Blocks.COAL_BLOCK), uid);
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

