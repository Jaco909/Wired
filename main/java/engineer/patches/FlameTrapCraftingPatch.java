package engineer.patches;

import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.level.gameObject.WallFlameTrapObject;
import necesse.level.gameObject.WallObject;
import net.bytebuddy.asm.Advice;

@ModConstructorPatch(target = WallFlameTrapObject.class, arguments = {WallObject.class})
public class FlameTrapCraftingPatch {
    @Advice.OnMethodExit
    static void onExit(@Advice.This WallFlameTrapObject object, @Advice.Argument(0) WallObject wallObject) {
        object.addGlobalIngredient(new String[]{"anyflametrap"});
    }
}
