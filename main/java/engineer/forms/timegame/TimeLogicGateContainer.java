package engineer.forms.timegame;

import engineer.objects.logic.TimeLogicGateEntity;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameMath;
import necesse.inventory.PlaceableItemInterface;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.BooleanCustomAction;
import necesse.inventory.container.customAction.IntCustomAction;
import necesse.inventory.container.logicGate.WireSelectCustomAction;

import java.awt.*;

public class TimeLogicGateContainer extends Container {
    public TimeLogicGateEntity entity;
    public final WireSelectCustomAction setOutputs;
    public final IntCustomAction setHour;
    public final IntCustomAction setMinute;
    public final BooleanCustomAction setinverted;

    public TimeLogicGateContainer(final NetworkClient client, int uniqueSeed, final TimeLogicGateEntity entity) {
        super(client, uniqueSeed);
        this.entity = entity;
        this.setOutputs = (WireSelectCustomAction)this.registerAction(new WireSelectCustomAction() {
            protected void run(boolean[] wires) {
                entity.wireOutputs = wires;
                if (client.isServer()) {
                    entity.updateOutputs(false);
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setHour = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
                entity.hour = GameMath.limit(value, 0, 23);
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setMinute = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
                entity.minute = GameMath.limit(value, 0, 59);
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setinverted = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.inverted = value;
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
    }

    public boolean isValid(ServerClient client) {
        if (!super.isValid(client)) {
            return false;
        } else {
            return !this.entity.isRemoved() && (new Point(this.entity.tileX * 32 + 16, this.entity.tileY * 32 + 16)).distance((double)client.playerMob.getX(), (double)client.playerMob.getY()) <= (double) PlaceableItemInterface.getPlaceRange(client.playerMob);
        }
    }
}