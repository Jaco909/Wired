package engineer.forms.light;

import engineer.objects.logic.LightLogicGateEntity;
import engineer.objects.logic.ProjectileLogicGateEntity;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.server.ServerClient;
import necesse.engine.util.GameMath;
import necesse.inventory.PlaceableItemInterface;
import necesse.inventory.container.Container;
import necesse.inventory.container.customAction.BooleanCustomAction;
import necesse.inventory.container.customAction.IntCustomAction;
import necesse.inventory.container.logicGate.WireSelectCustomAction;
import necesse.level.gameLogicGate.entities.SensorLogicGateEntity;

import java.awt.*;

public class ProjectileLogicGateContainer extends Container {
    public ProjectileLogicGateEntity entity;
    public final WireSelectCustomAction setOutputs;
    public final IntCustomAction setRange;
    public final BooleanCustomAction setPlayer;
    public final BooleanCustomAction setMob;
    public final BooleanCustomAction setDamage;
    public final BooleanCustomAction setTrap;
    public final BooleanCustomAction setOther;
    public final BooleanCustomAction setinverted;

    public ProjectileLogicGateContainer(final NetworkClient client, int uniqueSeed, final ProjectileLogicGateEntity entity) {
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
        this.setRange = (IntCustomAction)this.registerAction(new IntCustomAction() {
            protected void run(int value) {
                entity.range = GameMath.limit(value, 1, 5);
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setPlayer = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.fromPlayer = value;
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setMob = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.fromMob = value;
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setDamage = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.fromDamaging = value;
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setTrap = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.fromTrap = value;
                if (client.isServer()) {
                    entity.sendUpdatePacket();
                }

            }
        });
        this.setOther = (BooleanCustomAction)this.registerAction(new BooleanCustomAction() {
            protected void run(boolean value) {
                entity.fromOther = value;
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