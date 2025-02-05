package engineer.patches.wrench;

import necesse.engine.localization.Localization;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.util.GameBlackboard;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.placeableItem.WrenchPlaceableItem;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = WrenchPlaceableItem.class, name = "getTooltips", arguments = {InventoryItem.class,PlayerMob.class, GameBlackboard.class})
public class WrenchTooltipsPatch {
    @Advice.OnMethodExit()
    static ListGameTooltips onExit(@Advice.This WrenchPlaceableItem wrench, @Advice.Argument(0) InventoryItem item, @Advice.Argument(1) PlayerMob perspective, @Advice.Argument(2) GameBlackboard blackboard, @Advice.Return(readOnly = false) ListGameTooltips tooltips){
        tooltips.add(Localization.translate("itemtooltip", "wrenchgrid"));
        return tooltips;
    }
}
