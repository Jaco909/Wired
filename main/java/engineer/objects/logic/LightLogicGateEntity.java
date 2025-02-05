package engineer.objects.logic;

import engineer.Engineer;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;

public class LightLogicGateEntity extends LogicGateEntity {
    public boolean[] wireOutputs;
    private boolean active;
    public int light;
    public boolean inverted;

    public LightLogicGateEntity(GameLogicGate logicGate, Level level, int tileX, int tileY) {
        super(logicGate, level, tileX, tileY);
        this.wireOutputs = new boolean[4];
        this.active = false;
        this.light = 0;
        this.inverted = false;
    }

    public LightLogicGateEntity(GameLogicGate logicGate, TilePosition pos) {
        this(logicGate, pos.level, pos.tileX, pos.tileY);
    }

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addSmallBooleanArray("wireOutputs", this.wireOutputs);
        save.addInt("light", this.light);
        save.addBoolean("inverted", this.inverted);
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.wireOutputs = save.getSmallBooleanArray("wireOutputs", this.wireOutputs);
        this.light = save.getInt("light", 0);
        this.inverted = save.getBoolean("inverted", false);
        this.updateOutputs(true);
    }

    public void writePacket(PacketWriter writer) {
        super.writePacket(writer);

        for(int i = 0; i < 4; ++i) {
            writer.putNextBoolean(this.wireOutputs[i]);
        }
        writer.putNextInt(this.light);
        writer.putNextBoolean(this.inverted);
    }

    public void applyPacket(PacketReader reader) {
        super.applyPacket(reader);

        for(int i = 0; i < 4; ++i) {
            this.wireOutputs[i] = reader.getNextBoolean();
        }
        this.light = reader.getNextInt();
        this.inverted = reader.getNextBoolean();
    }

    public void tick() {
        super.tick();
        if (this.isServer()) {
            this.checkState();
        }
    }

    public void checkState() {
        if (this.level.getLightLevel(this.tileX,this.tileY).getLevel() >= this.light*10 && !this.inverted) {
            this.active = true;
            this.updateOutputs(true);
        } else if (this.level.getLightLevel(this.tileX,this.tileY).getLevel() < this.light*10 && this.inverted) {
            this.active = true;
            this.updateOutputs(true);
        } else {
            this.active = false;
            this.updateOutputs(true);
        }
    }

    public boolean isActive() {
        return this.active;
    }

    public void updateOutputs(boolean forceUpdate) {
        for(int i = 0; i < 4; ++i) {
            boolean desired = this.wireOutputs[i] && this.active;
            this.setOutput(i, desired, forceUpdate);
        }

    }

    public void openContainer(ServerClient client) {
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.LIGHT_LOGIC_GATE_CONTAINER, this.tileX, this.tileY));
    }
}