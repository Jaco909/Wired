package engineer.patches.wrench;

import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.window.GameWindow;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.Renderer;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.gameTooltips.GameTooltipManager;
import necesse.gfx.gameTooltips.InputTooltip;
import necesse.gfx.gameTooltips.TooltipLocation;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.placeableItem.WrenchPlaceableItem;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = WrenchPlaceableItem.class, name = "onMouseHoverTile", arguments = {InventoryItem.class,GameCamera.class,PlayerMob.class,int.class,int.class,TilePosition.class,boolean.class})
public class WrenchMouseHoverPatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static void onExit(@Advice.This WrenchPlaceableItem wrench, @Advice.Argument(0) InventoryItem item, @Advice.Argument(1) GameCamera camera, @Advice.Argument(2) PlayerMob perspective, @Advice.Argument(3) int mouseX, @Advice.Argument(4) int mouseY, @Advice.Argument(5) TilePosition pos, @Advice.Argument(6) boolean inDebug){
        String interactError = wrench.canInteractError(pos.level, mouseX, mouseY, perspective, item);
        if (interactError == null) {
            Renderer.setCursor(GameWindow.CURSOR.INTERACT);
            GameTooltipManager.addTooltip(new InputTooltip(Control.MOUSE2, Localization.translate("controls", "edittip")), TooltipLocation.INTERACT_FOCUS);
        } else if (interactError.equals("outofrange")) {
            Renderer.setCursor(GameWindow.CURSOR.INTERACT);
            GameTooltipManager.addTooltip(new InputTooltip(Control.MOUSE2, Localization.translate("controls", "edittip"), 0.5F), TooltipLocation.INTERACT_FOCUS);
        }
    }
}
