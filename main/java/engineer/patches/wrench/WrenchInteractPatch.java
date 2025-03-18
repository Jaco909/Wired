package engineer.patches.wrench;

import engineer.Engineer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.PacketReader;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.placeableItem.WrenchPlaceableItem;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = WrenchPlaceableItem.class, name = "onLevelInteract", arguments = {Level.class,int.class,int.class, ItemAttackerMob.class,int.class,InventoryItem.class, ItemAttackSlot.class,int.class, GNDItemMap.class})
public class WrenchInteractPatch {
    @Advice.OnMethodExit()
    static InventoryItem onExit(@Advice.This WrenchPlaceableItem wire, @Advice.Argument(0) Level level, @Advice.Argument(1) int x, @Advice.Argument(2) int y, @Advice.Argument(3) ItemAttackerMob attackerMob, @Advice.Argument(4) int attackHeight, @Advice.Argument(5) InventoryItem item, @Advice.Argument(6) ItemAttackSlot slot, @Advice.Argument(7) int seed, @Advice.Argument(8) GNDItemMap mapContent, @Advice.Return(readOnly = false) InventoryItem returnItem){
        if (level.isClient()) {
            int tileX = x / 32;
            int tileY = y / 32;
            if (level.objectLayer.getObject(tileX,tileY) != null) {
                LogicGateEntity entity = level.logicLayer.getEntity(tileX, tileY);
                if (entity == null && !level.objectLayer.getObject(tileX,tileY).canInteract(level,tileX,tileY,attackerMob.getFirstPlayerOwner())) {
                    Engineer.settings.wrenchGrid = !Engineer.settings.wrenchGrid;
                }
            } else {
                Engineer.settings.wrenchGrid = !Engineer.settings.wrenchGrid;
            }
        }
        return returnItem;
    }
}
