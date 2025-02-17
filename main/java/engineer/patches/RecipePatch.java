package engineer.patches;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.state.MainMenu;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;
import net.bytebuddy.asm.Advice;

import java.util.Iterator;

//Unload vanilla recipes on Main Menu load
@ModMethodPatch(target = MainMenu.class, name = "init", arguments = {})
public class RecipePatch {
    @Advice.OnMethodExit()
    static void onExit() {

        Iterator recipes = Recipes.getRecipes().iterator();
        while(recipes.hasNext()) {
            Recipe recipe = (Recipe)recipes.next();
            if (!recipe.tech.getStringID().equalsIgnoreCase("LOGICBENCH")) {
                if (recipe.resultStringID.equalsIgnoreCase("woodpressureplate") || recipe.resultStringID.equalsIgnoreCase("stonepressureplate") || recipe.resultStringID.equalsIgnoreCase("snowstonepressureplate") || recipe.resultStringID.equalsIgnoreCase("swampstonepressureplate") || recipe.resultStringID.equalsIgnoreCase("sandstonepressureplate") || recipe.resultStringID.equalsIgnoreCase("deepstonepressureplate") || recipe.resultStringID.equalsIgnoreCase("deepsnowstonepressureplate") || recipe.resultStringID.equalsIgnoreCase("deepswampstonepressureplate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("wrench")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("cutter")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("wire")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("rocklever")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("ledpanel")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("fireworkdispenser")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("tnt")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("andgate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("nandgate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("orgate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("norgate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("xorgate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("srlatchgate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("tflipflopgate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("countergate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("timergate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("delaygate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("buffergate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("sensorgate")) {
                    recipes.remove();
                }
                if (recipe.resultStringID.equalsIgnoreCase("soundgate")) {
                    recipes.remove();
                }

            }
        }
    }
}
