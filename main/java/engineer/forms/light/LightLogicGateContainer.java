package engineer.forms.light;

import engineer.objects.logic.LightLogicGateEntity;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameMath;
import necesse.inventory.PlaceableItemInterface;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.BooleanCustomAction;
import necesse.inventory.container.customAction.IntCustomAction;
import necesse.inventory.container.logicGate.WireSelectCustomAction;

import java.awt.*;

public class LightLogicGateContainer extends Container {
    public LightLogicGateEntity entity;
    public final WireSelectCustomAction setOutputs;
    public final IntCustomAction setLight;
    public final BooleanCustomAction setinverted;

    public LightLogicGateContainer(final NetworkClient client, int uniqueSeed, final LightLogicGateEntity entity) {
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
        this.setLight = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
                entity.light = GameMath.limit(value, 0, 15);
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