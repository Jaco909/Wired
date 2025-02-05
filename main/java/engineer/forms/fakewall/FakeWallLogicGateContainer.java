package engineer.forms.fakewall;

import engineer.objects.logic.WallLogicGateEntity;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.server.ServerClient;
import necesse.inventory.PlaceableItemInterface;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.BooleanCustomAction;
import necesse.inventory.container.logicGate.WireSelectCustomAction;

import java.awt.*;

public class FakeWallLogicGateContainer extends Container {
    public WallLogicGateEntity entity;
    public final WireSelectCustomAction setInputs;
    public final BooleanCustomAction setinverted;

    public FakeWallLogicGateContainer(final NetworkClient client, int uniqueSeed, final WallLogicGateEntity entity) {
        super(client, uniqueSeed);
        this.entity = entity;
        this.setInputs = (WireSelectCustomAction)this.registerAction(new WireSelectCustomAction() {
            protected void run(boolean[] wires) {
                entity.wireInputs = wires;
                if (client.isServer()) {
                    entity.updateOutputs(false);
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