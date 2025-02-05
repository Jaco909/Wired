package engineer.objects.vanilla;

import engineer.Engineer;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.inventory.container.object.OEInventoryContainer;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.WallObject;
import necesse.level.gameObject.WallTrapObject;
import necesse.level.maps.Level;

public class WallArrowTrapObject extends WallTrapObject {
    public WallArrowTrapObject(WallObject wallObject) {
        super(wallObject, "arrowtrap");
        this.addGlobalIngredient(new String[]{"anyarrowtrap"});
    }

    public WallArrowTrapObject(WallObject wallObject, int toolTier, ToolType toolType) {
        super(wallObject, "arrowtrap", toolTier, toolType);
    }
    /*public GameMessage getNewLocalization() {
        //return (GameMessage)new LocalMessage("object", this.wallObject.textureName+"arrowtrap");
        //return new LocalMessage("object", this.textureName + "trap", "trap",this.);
        //return this.wallObject.getNewTrapLocalization(new LocalMessage("object", this.textureName));
    }*/
    /*public GameMessage getNewLocalization() {
        return getNewTrapLocalization(new LocalMessage("object", this.textureName));
    }
    public GameMessage getNewTrapLocalization(GameMessage trap) {
        System.out.println(this.wallObject.getStringID());
        return new LocalMessage("object", "<wall>" + " <trap>", "trap", trap ,"wall", new LocalMessage("object", this.wallObject.getStringID()));
    }*/
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new NewArrowObjectEntity(level, x, y);
    }
    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        ObjectEntity ent = level.entityManager.getObjectEntity(x, y);
        if (ent != null && !level.isTrialRoom) {
            return !((NewArrowObjectEntity)ent).isInUse();
        } else {
            return false;
        }
    }
    public void interact(Level level, int x, int y, PlayerMob player) {
        if (level.isServer()) {
            OEInventoryContainer.openAndSendContainer(Engineer.ARROW_CONTAINER, player.getServerClient(), level, x, y);
        } if (level.isClient()) {
            ObjectEntity ent = level.entityManager.getObjectEntity(x, y);
            if (ent != null) {
                ((NewArrowObjectEntity)ent).firstSaveSetup();
            }
        }
    }
    public void onWireUpdate(Level level, int layerID, int tileX, int tileY, int wireID, boolean active) {
        if (active) {
            ObjectEntity ent = level.entityManager.getObjectEntity(tileX, tileY);
            if (ent != null) {
                ((NewArrowObjectEntity)ent).triggerTrap(wireID, level.getObjectRotation(tileX, tileY));
            }
        }

    }
}