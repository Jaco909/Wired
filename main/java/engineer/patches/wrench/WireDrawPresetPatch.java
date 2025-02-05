package engineer.patches.wrench;

import engineer.Engineer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.forms.presets.sidebar.WireEditSidebarForm;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.maps.wireManager.WireManager;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = WireManager.class, name = "drawWirePreset", arguments = {int.class,int.class, GameCamera.class,int.class})
public class WireDrawPresetPatch {
    @Advice.OnMethodExit()
    static void onExit(@Advice.This WireManager wire, @Advice.Argument(0) int tileX, @Advice.Argument(1) int tileY, @Advice.Argument(2) GameCamera camera, @Advice.Argument(3) int wireID){
        if (Engineer.settings.wrenchGrid) {
            int editingFirst = 0;
            for (int i = 0; i < 4; i++) {
                if (WireEditSidebarForm.isEditing(i)) {
                    editingFirst = i;
                    break;
                }
            }

            int drawX = camera.getTileDrawX(tileX);
            int drawY = camera.getTileDrawY(tileY);
            GameTexture texture = GameTexture.fromFile("other/tile_grid");
            GameTexture texture_fade = GameTexture.fromFile("other/tile_grid_transp");
            texture.initDraw().sprite(1, 1, 32).draw(drawX, drawY);

            if (wire.hasWire(tileX + 1, tileY, wireID)) {
                texture.initDraw().sprite(2, 1, 32).draw(drawX + 32, drawY);
                if (wireID == 0) {
                    texture.initDraw().sprite(0, 2, 32).mirrorX().draw(drawX + 32, drawY);
                } else if (wireID == 1) {
                    texture.initDraw().sprite(2, 2, 32).mirrorX().draw(drawX + 32, drawY);
                } else if (wireID == 2) {
                    texture.initDraw().sprite(3, 2, 32).mirrorX().draw(drawX + 32, drawY);
                } else {
                    texture.initDraw().sprite(4, 2, 32).mirrorX().draw(drawX + 32, drawY);
                }
            }
            if (wire.hasWire(tileX - 1, tileY, wireID)) {
                texture.initDraw().sprite(0, 1, 32).draw(drawX - 32, drawY);
                if (wireID == 0) {
                    texture.initDraw().sprite(0, 2, 32).draw(drawX - 32, drawY);
                } else if (wireID == 1) {
                    texture.initDraw().sprite(2, 2, 32).draw(drawX - 32, drawY);
                } else if (wireID == 2) {
                    texture.initDraw().sprite(3, 2, 32).draw(drawX - 32, drawY);
                } else {
                    texture.initDraw().sprite(4, 2, 32).draw(drawX - 32, drawY);
                }
            }
            if (wire.hasWire(tileX, tileY + 1, wireID)) {
                texture.initDraw().sprite(1, 2, 32).draw(drawX, drawY + 32);
                if (wireID == 0) {
                    texture.initDraw().sprite(0, 0, 32).mirrorY().draw(drawX, drawY + 32);
                } else if (wireID == 1) {
                    texture.initDraw().sprite(2, 0, 32).mirrorY().draw(drawX, drawY + 32);
                } else if (wireID == 2) {
                    texture.initDraw().sprite(3, 0, 32).mirrorY().draw(drawX, drawY + 32);
                } else {
                    texture.initDraw().sprite(4, 0, 32).mirrorY().draw(drawX, drawY + 32);
                }
            }
            if (wire.hasWire(tileX, tileY - 1, wireID)) {
                texture.initDraw().sprite(1, 0, 32).draw(drawX, drawY - 32);
                if (wireID == 0) {
                    texture.initDraw().sprite(0, 0, 32).draw(drawX, drawY - 32);
                } else if (wireID == 1) {
                    texture.initDraw().sprite(2, 0, 32).draw(drawX, drawY - 32);
                } else if (wireID == 2) {
                    texture.initDraw().sprite(3, 0, 32).draw(drawX, drawY - 32);
                } else {
                    texture.initDraw().sprite(4, 0, 32).draw(drawX, drawY - 32);
                }
            }
            if (editingFirst == wireID) {
                texture_fade.initDraw().sprite(1, 2, 32).draw(drawX, drawY + 32);
                texture_fade.initDraw().sprite(0, 1, 32).draw(drawX - 32, drawY);
                texture_fade.initDraw().sprite(2, 1, 32).draw(drawX + 32, drawY);
                texture_fade.initDraw().sprite(1, 0, 32).draw(drawX, drawY - 32);
            } else if (wire.hasWire(tileX,tileY,editingFirst)) {
                texture_fade.initDraw().sprite(1, 2, 32).draw(drawX, drawY + 32);
                texture_fade.initDraw().sprite(0, 1, 32).draw(drawX - 32, drawY);
                texture_fade.initDraw().sprite(2, 1, 32).draw(drawX + 32, drawY);
                texture_fade.initDraw().sprite(1, 0, 32).draw(drawX, drawY - 32);
            }
        }
    }
}
/*if (wireID == 0) {
            if (Engineer.settings.colorblind) {
                out = new Color(255, 0, 93); //FF005D
            } else {
                out =  new Color(220, 50, 50);
            }
        } else if (wireID == 1) {
            if (Engineer.settings.colorblind) {
                out =  new Color(0, 77, 64); //004D40
            } else {
                out =  new Color(50, 220, 50);
            }
        } else if (wireID == 2) {
            if (Engineer.settings.colorblind) {
                out =  new Color(30, 136, 229); //1E88E5
            } else {
                out =  new Color(50, 50, 220);
            }
        } else {
            if (Engineer.settings.colorblind) {
                out =  wireID == 3 ? new Color(255, 193, 7) : new Color(255, 255, 255); //FFC107
            } else {
                out =  wireID == 3 ? new Color(220, 220, 50) : new Color(255, 255, 255);
            }
        }
        return out;*/
