package engineer.items;

import necesse.engine.network.PacketReader;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.inventory.InventorySlot;
import necesse.inventory.PlayerInventoryManager;
import necesse.inventory.item.toolItem.ToolDamageItem;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;

import java.util.function.BiFunction;

public class CustomGate extends GameLogicGate {
    public BiFunction<GameLogicGate, TilePosition, LogicGateEntity> provider;

    public CustomGate(BiFunction<GameLogicGate, TilePosition, LogicGateEntity> provider) {
        this.provider = provider;
    }

    public LogicGateEntity getNewEntity(Level level, int tileX, int tileY) {
        return (LogicGateEntity)this.provider.apply(this, new TilePosition(level, tileX, tileY));
    }

    public void placeGate(Level level, int tileX, int tileY) {
        if (canPlace(level,tileX,tileY) == null) {
            System.out.println("wack");
            level.logicLayer.setLogicGate(tileX, tileY, this.getID(), (PacketReader) null);
        }
    }

    public String canPlace(Level level, int tileX, int tileY) {
        if (level.isClient()) {
            int tier = level.getClient().getPlayer().getInv().streamInventorySlots(false, false, false, false).map(InventorySlot::getItem).filter((i) -> i != null && i.item instanceof ToolDamageItem).mapToInt((i) -> ((ToolDamageItem)i.item).getToolTier(i)).max().orElse(-1);
            if (level.getObject(tileX,tileY).toolTier <= tier) {
                return level.logicLayer.hasGate(tileX, tileY) ? "occupied" : null;
            } else {
                return "occupied";
            }
        } else {
            return level.logicLayer.hasGate(tileX, tileY) ? "occupied" : null;
        }
    }

    public void attemptPlace(Level level, int tileX, int tileY, PlayerMob player, String error) {
    }
}