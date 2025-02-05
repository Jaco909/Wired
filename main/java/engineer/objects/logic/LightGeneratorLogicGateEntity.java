package engineer.objects.logic;

import engineer.Engineer;
import engineer.objects.FakeWallEntity;
import engineer.packets.FakeWallIDPacket;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.registries.ObjectRegistry;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.util.GameRandom;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.particle.Particle;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;

import java.awt.*;

public class LightGeneratorLogicGateEntity extends LogicGateEntity {
    public boolean[] wireInputs;
    private boolean active;
    private boolean isRunning;
    private boolean firstTick;
    public boolean inverted;
    private boolean isRemoved;

    public LightGeneratorLogicGateEntity(GameLogicGate logicGate, Level level, int tileX, int tileY) {
        super(logicGate, level, tileX, tileY);
        this.wireInputs = new boolean[4];
        this.active = false;
        this.inverted = false;
        this.firstTick = true;
    }

    public LightGeneratorLogicGateEntity(GameLogicGate logicGate, TilePosition pos) {
        this(logicGate, pos.level, pos.tileX, pos.tileY);
    }

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addSmallBooleanArray("wireInputs", this.wireInputs);
        save.addBoolean("inverted", this.inverted);
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.wireInputs = save.getSmallBooleanArray("wireInputs", this.wireInputs);
        this.inverted = save.getBoolean("inverted", false);
        this.updateOutputs(true);
    }

    public void writePacket(PacketWriter writer) {
        super.writePacket(writer);
        for(int i = 0; i < 4; ++i) {
            writer.putNextBoolean(this.wireInputs[i]);
        }
        writer.putNextBoolean(this.inverted);
    }

    public void applyPacket(PacketReader reader) {
        super.applyPacket(reader);
        for(int i = 0; i < 4; ++i) {
            this.wireInputs[i] = reader.getNextBoolean();
        }
        this.inverted = reader.getNextBoolean();
        this.updateRunning();
        if (this.isServer()) {
            this.updateOutputs(true);
        }
    }

    public void tick() {
        super.tick();
        if (this.isServer()) {
            this.checkState();
        }
        if (this.firstTick) {
            this.updateRunning();
        }
    }
    protected void onUpdate(int wireID, boolean active) {
        this.updateRunning();
    }

    public void updateRunning() {
        this.isRunning = false;

        for(int i = 0; i < 4; ++i) {
            if (this.wireInputs[i] && this.isWireActive(i)) {
                this.isRunning = true;
                break;
            }
        }

        if (!this.isRunning) {
            if (this.isServer()) {
                this.checkState();
            }
            this.active = false;
        }

    }

    public boolean conditions() {
        if (this.wireInputs[0] && this.isWireActive(0)) {
            return true;
        } else if (this.wireInputs[1] && this.isWireActive(1)) {
            return true;
        } else if (this.wireInputs[2] && this.isWireActive(2)) {
            return true;
        } else if (this.wireInputs[3] && this.isWireActive(3)) {
            return true;
        } else {
            return false;
        }
    }

    public void checkState() {
        if (!this.inverted) {
            if (this.conditions()) {
                if (level.tickManager().getTotalTicks() % 5L == 0L) {
                    level.entityManager.addParticle(tileX, tileY, Particle.GType.COSMETIC).color(new Color(249, 226, 117)).sizeFades(6, 10).givesLight(50.0F, 0.4F).height(16.0F);
                }
                level.lightManager.refreshParticleLightFloat(tileX, tileY, 50.0F, 0.4F, 135);
            }
        }
    }

    public void remove() {
        this.isRemoved = true;
    }

    public boolean isRemoved() {
        return this.isRemoved;
    }

    public boolean isActive() {
        return this.active;
    }

    public void updateOutputs(boolean forceUpdate) {
    }

    public void openContainer(ServerClient client) {
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.LIGHT_GENERATOR_LOGIC_GATE_CONTAINER, this.tileX, this.tileY));
    }
}