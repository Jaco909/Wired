package engineer.patches.wrench;

import necesse.engine.input.Control;
import necesse.engine.localization.Localization;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.window.GameWindow;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
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

@ModMethodPatch(target = WrenchPlaceableItem.class, name = "canLevelInteract", arguments = {Level.class,int.class,int.class, ItemAttackerMob.class,InventoryItem.class})
public class WrenchCanInteractPatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static boolean onExit(@Advice.This WrenchPlaceableItem wrench, @Advice.Argument(0) Level level, @Advice.Argument(1) int x, @Advice.Argument(2) int y, @Advice.Argument(3) ItemAttackerMob attackerMob, @Advice.Argument(4) InventoryItem item, @Advice.Return(readOnly = false) boolean out){
        boolean set = false;
        if (wrench.canInteractError(level, x, y, attackerMob.getFirstPlayerOwner(), item) == null) {
            set = true;
            out = set;
            return out;
        } else if (wrench.canInteractError(level, x, y, attackerMob.getFirstPlayerOwner(), item).equalsIgnoreCase("blank")) {
            set = true;
            out = set;
            return out;
        } else {
            out = set;
            return out;
        }
    }
}
