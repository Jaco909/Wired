package engineer.forms.storage;

import engineer.objects.logic.StorageLogicGateEntity;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameMath;
import necesse.inventory.PlaceableItemInterface;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.BooleanCustomAction;
import necesse.inventory.container.customAction.IntCustomAction;

import java.awt.*;

public class StorageLogicGateContainer extends Container {
    public StorageLogicGateEntity entity;
    //public final WireSelectCustomAction setOutputs;
    public final IntCustomAction setRedTriggerSlots;
    public final IntCustomAction setGreenTriggerSlots;
    public final IntCustomAction setBlueTriggerSlots;
    public final IntCustomAction setYellowTriggerSlots;
    public final BooleanCustomAction setRedInverted;
    public final BooleanCustomAction setGreenInverted;
    public final BooleanCustomAction setBlueInverted;
    public final BooleanCustomAction setYellowInverted;

    public StorageLogicGateContainer(final NetworkClient client, int uniqueSeed, final StorageLogicGateEntity entity) {
        super(client, uniqueSeed);
        this.entity = entity;
        /*this.setOutputs = (WireSelectCustomAction)this.registerAction(new WireSelectCustomAction() {
            protected void run(boolean[] wires) {
                entity.wireOutputs = wires;
                if (client.isServer()) {
                    entity.updateOutputs(false);
                    entity.sendUpdatePacket();
                }

            }
        });*/
        this.setRedTriggerSlots = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
                entity.redTrigger = GameMath.limit(value, 0, entity.foundSlots);
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setGreenTriggerSlots = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
                entity.greenTrigger = GameMath.limit(value, 0, entity.foundSlots);
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setBlueTriggerSlots = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
                entity.blueTrigger = GameMath.limit(value, 0, entity.foundSlots);
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setYellowTriggerSlots = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
                entity.yellowTrigger = GameMath.limit(value, 0, entity.foundSlots);
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setRedInverted = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.redInverted = value;
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setGreenInverted = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.greenInverted = value;
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setBlueInverted = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.blueInverted = value;
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setYellowInverted = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.yellowInverted = value;
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