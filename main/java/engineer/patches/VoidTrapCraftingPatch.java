package engineer.patches;

import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.level.gameObject.WallFlameTrapObject;
import necesse.level.gameObject.WallObject;
import necesse.level.gameObject.WallVoidTrapObject;
import net.bytebuddy.asm.Advice;

@ModConstructorPatch(target = WallVoidTrapObject.class, arguments = {WallObject.class})
public class VoidTrapCraftingPatch {
    @Advice.OnMethodExit
    static void onExit(@Advice.This WallVoidTrapObject object, @Advice.Argument(0) WallObject wallObject) {
        object.addGlobalIngredient(new String[]{"anyvoidtrap"});
    }
}
