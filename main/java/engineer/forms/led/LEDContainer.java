package engineer.forms.led;

import engineer.objects.LEDObjectEntity;
import engineer.packets.LEDColorPacket;
import engineer.packets.LEDInputPacket;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameMath;
import necesse.entity.objectEntity.SignObjectEntity;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.BooleanCustomAction;
import necesse.inventory.container.customAction.IntCustomAction;
import necesse.inventory.container.customAction.StringCustomAction;
import necesse.inventory.container.logicGate.WireSelectCustomAction;
import necesse.level.maps.Level;

import java.awt.*;

public class LEDContainer extends Container {
    public LEDObjectEntity objectEntity;
    public final BooleanCustomAction setRedInput;
    public final BooleanCustomAction setGreenInput;
    public final BooleanCustomAction setBlueInput;
    public final BooleanCustomAction setYellowInput;
    public final IntCustomAction setColor1;
    public final IntCustomAction setColor2;
    public final IntCustomAction setColor3;
    public final IntCustomAction setColor4;

    public LEDContainer(NetworkClient client, int uniqueSeed, final LEDObjectEntity objectEntity) {
        super(client, uniqueSeed);
        this.objectEntity = objectEntity;
        this.setRedInput = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                objectEntity.wireInputs[0] = value;
                if (objectEntity.getLevel().isClient()) {
                    objectEntity.getLevel().getClient().network.sendPacket(new LEDInputPacket(Math.round(objectEntity.x),Math.round(objectEntity.y),objectEntity,value,0));
                }
            }
        });
        this.setGreenInput = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                objectEntity.wireInputs[1] = value;
                if (objectEntity.getLevel().isClient()) {
                    objectEntity.getLevel().getClient().network.sendPacket(new LEDInputPacket(Math.round(objectEntity.x),Math.round(objectEntity.y),objectEntity,value,1));
                }
            }
        });
        this.setBlueInput = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                objectEntity.wireInputs[2] = value;
                if (objectEntity.getLevel().isClient()) {
                    objectEntity.getLevel().getClient().network.sendPacket(new LEDInputPacket(Math.round(objectEntity.x),Math.round(objectEntity.y),objectEntity,value,2));
                }
            }
        });
        this.setYellowInput = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                objectEntity.wireInputs[3] = value;
                if (objectEntity.getLevel().isClient()) {
                    objectEntity.getLevel().getClient().network.sendPacket(new LEDInputPacket(Math.round(objectEntity.x),Math.round(objectEntity.y),objectEntity,value,3));
                }
            }
        });
        this.setColor1 = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
            objectEntity.color1 = new Color(value);
            if (objectEntity.getLevel().isClient()) {
                objectEntity.getLevel().getClient().network.sendPacket(new LEDColorPacket(Math.round(objectEntity.x),Math.round(objectEntity.y),objectEntity,value,0));
            }
            }
        });
        this.setColor2 = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
            objectEntity.color2 = new Color(value);
            if (objectEntity.getLevel().isClient()) {
                objectEntity.getLevel().getClient().network.sendPacket(new LEDColorPacket(Math.round(objectEntity.x),Math.round(objectEntity.y),objectEntity,value,1));
            }
            }
        });
        this.setColor3 = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
            objectEntity.color3 = new Color(value);
            if (objectEntity.getLevel().isClient()) {
                objectEntity.getLevel().getClient().network.sendPacket(new LEDColorPacket(Math.round(objectEntity.x),Math.round(objectEntity.y),objectEntity,value,2));
            }
            }
        });
        this.setColor4 = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
            objectEntity.color4 = new Color(value);
            if (objectEntity.getLevel().isClient()) {
                objectEntity.getLevel().getClient().network.sendPacket(new LEDColorPacket(Math.round(objectEntity.x),Math.round(objectEntity.y),objectEntity,value,3));
            }
            }
        });
    }

    public boolean isValid(ServerClient client) {
        if (!super.isValid(client)) {
            return false;
        } else {
            Level level = client.getLevel();
            return !this.objectEntity.removed() && level.getObject(this.objectEntity.getX(), this.objectEntity.getY()).inInteractRange(level, this.objectEntity.getX(), this.objectEntity.getY(), client.playerMob);
        }
    }
}