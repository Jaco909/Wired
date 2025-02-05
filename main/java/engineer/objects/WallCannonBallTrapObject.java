package engineer.objects;

import necesse.entity.objectEntity.FlameTrapObjectEntity;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.WallObject;
import necesse.level.gameObject.WallTrapObject;
import necesse.level.maps.Level;

public class WallCannonBallTrapObject extends WallTrapObject {
    public WallCannonBallTrapObject(WallObject wallObject) {
        super(wallObject, "cannonballtrap");
    }

    public WallCannonBallTrapObject(WallObject wallObject, int toolTier, ToolType toolType) {
        super(wallObject, "cannonballtrap", toolTier, toolType);
    }

    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new CannonBallTrapObjectEntity(level, x, y);
    }
}