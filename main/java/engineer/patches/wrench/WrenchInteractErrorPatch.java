package engineer.patches.wrench;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.placeableItem.WrenchPlaceableItem;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = WrenchPlaceableItem.class, name = "canInteractError", arguments = {Level.class,int.class,int.class,PlayerMob.class,InventoryItem.class})
public class WrenchInteractErrorPatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static String onExit(@Advice.This WrenchPlaceableItem wrench, @Advice.Argument(0) Level level, @Advice.Argument(1) int x, @Advice.Argument(2) int y, @Advice.Argument(3) PlayerMob player, @Advice.Argument(4) InventoryItem item, @Advice.Return(readOnly = false) String error){
        int tileX = x / 32;
        int tileY = y / 32;
        if (level.isProtected(tileX, tileY)) {
            error = "protected";
            return error;
        } else if (level.objectLayer.getObject(tileX,tileY) == null) {
            error = "blank";
            return error;
        } else if (!level.logicLayer.hasGate(tileX, tileY) && !level.objectLayer.getObject(tileX,tileY).canInteract(level,tileX,tileY,player)) {
            error = "blank";
            return error;
        } else if (level.getObject(tileX,tileY).canInteract(level,tileX,tileY,player) && !level.logicLayer.hasGate(tileX,tileY)) {
            //if (!level.logicLayer.getEntity(tileX,tileY).getLogicGate().getStringID().equalsIgnoreCase("storageGate")) {
                error = "interactable";
                return error;
            //}
        } else {
            error = player.getPositionPoint().distance((double)(tileX * 32 + 16), (double)(tileY * 32 + 16)) > (double)wrench.getPlaceRange(item, player) ? "outofrange" : null;
            return error;
        }
    }
}
