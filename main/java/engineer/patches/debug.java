package engineer.patches;

import necesse.engine.input.controller.ControllerInput;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.PlayerMob;
import necesse.engine.input.Input;

import necesse.gfx.gameTexture.GameTexture;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = PlayerMob.class, name = "clientTick", arguments = {})
public class debug {
    @Advice.OnMethodExit()
    static void onExit(@Advice.This PlayerMob player) {
        if (Input.lastInputIsController) {
            float x = ControllerInput.getAimX();
            float y = ControllerInput.getAimY();
            System.out.println(x);
            System.out.println(y);
            GameTexture texture = GameTexture.fromFile("other/tile_grid");
            texture.initDraw().sprite(1, 1, 32).draw(Math.round(x), Math.round(y));
        }
    }
}
