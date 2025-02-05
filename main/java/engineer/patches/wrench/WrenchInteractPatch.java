package engineer.patches.wrench;

import engineer.Engineer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.PacketReader;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.placeableItem.WrenchPlaceableItem;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = WrenchPlaceableItem.class, name = "onLevelInteract", arguments = {Level.class,int.class,int.class,PlayerMob.class,int.class,InventoryItem.class,PlayerInventorySlot.class,int.class,PacketReader.class})
public class WrenchInteractPatch {
    @Advice.OnMethodExit()
    static InventoryItem onExit(@Advice.This WrenchPlaceableItem wire, @Advice.Argument(0) Level level, @Advice.Argument(1) int x, @Advice.Argument(2) int y, @Advice.Argument(3) PlayerMob player, @Advice.Argument(4) int attackHeight, @Advice.Argument(5) InventoryItem item, @Advice.Argument(6) PlayerInventorySlot slot, @Advice.Argument(7) int seed, @Advice.Argument(8) PacketReader contentReader, @Advice.Return(readOnly = false) InventoryItem returnItem){
        if (level.isClient()) {
            int tileX = x / 32;
            int tileY = y / 32;
            if (level.objectLayer.getObject(tileX,tileY) != null) {
                LogicGateEntity entity = level.logicLayer.getEntity(tileX, tileY);
                if (entity == null && !level.objectLayer.getObject(tileX,tileY).canInteract(level,tileX,tileY,player)) {
                    Engineer.settings.wrenchGrid = !Engineer.settings.wrenchGrid;
                }
            } else {
                Engineer.settings.wrenchGrid = !Engineer.settings.wrenchGrid;
            }
        }
        return returnItem;
    }
}
