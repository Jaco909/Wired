package engineer.objects.logic;

import engineer.Engineer;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;


import java.awt.*;

public class TimeLogicGateEntity extends LogicGateEntity {
    public boolean[] wireOutputs;
    private boolean active;
    public int hour;
    public int minute;
    public boolean inverted;

    public TimeLogicGateEntity(GameLogicGate logicGate, Level level, int tileX, int tileY) {
        super(logicGate, level, tileX, tileY);
        this.wireOutputs = new boolean[4];
        this.active = false;
        this.hour = 0;
        this.minute = 0;
        this.inverted = false;
    }

    public TimeLogicGateEntity(GameLogicGate logicGate, TilePosition pos) {
        this(logicGate, pos.level, pos.tileX, pos.tileY);
    }

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addSmallBooleanArray("wireOutputs", this.wireOutputs);
        save.addInt("hour", this.hour);
        save.addInt("minute", this.minute);
        save.addBoolean("inverted", this.inverted);
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.wireOutputs = save.getSmallBooleanArray("wireOutputs", this.wireOutputs);
        this.hour = save.getInt("hour", 0);
        this.minute = save.getInt("minute", 0);
        this.inverted = save.getBoolean("inverted", false);
        this.updateOutputs(true);
    }

    public void writePacket(PacketWriter writer) {
        super.writePacket(writer);

        for(int i = 0; i < 4; ++i) {
            writer.putNextBoolean(this.wireOutputs[i]);
        }
        writer.putNextInt(this.hour);
        writer.putNextInt(this.minute);
        writer.putNextBoolean(this.inverted);
    }

    public void applyPacket(PacketReader reader) {
        super.applyPacket(reader);

        for(int i = 0; i < 4; ++i) {
            this.wireOutputs[i] = reader.getNextBoolean();
        }
        this.hour = reader.getNextInt();
        this.minute = reader.getNextInt();
        this.inverted = reader.getNextBoolean();
    }

    public void tick() {
        super.tick();
        if (this.isServer()) {
            this.checkTime();
        }
    }
    private void checkTime() {
        if (!this.inverted) {
            if (this.getWorldEntity().getDayTimeHour() == this.hour) {
                if (this.getWorldEntity().getDayTimeMinute() >= this.minute) {
                    this.active = true;
                    this.updateOutputs(true);
                } else {
                    this.active = false;
                    this.updateOutputs(true);
                }
            } else if (this.getWorldEntity().getDayTimeHour() > this.hour) {
                this.active = true;
                this.updateOutputs(true);
            } else {
                this.active = false;
                this.updateOutputs(true);
            }
        } else {
            if (this.getWorldEntity().getDayTimeHour() == this.hour) {
                if (this.getWorldEntity().getDayTimeMinute() < this.minute) {
                    this.active = true;
                    this.updateOutputs(true);
                } else {
                    this.active = false;
                    this.updateOutputs(true);
                }
            } else if (this.getWorldEntity().getDayTimeHour() < this.hour) {
                this.active = true;
                this.updateOutputs(true);
            } else {
                this.active = false;
                this.updateOutputs(true);
            }
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

    public ListGameTooltips getTooltips(PlayerMob perspective, boolean debug) {
        ListGameTooltips tooltips = super.getTooltips(perspective, debug);
        tooltips.add(Localization.translate("logictooltips", "timesensortime","time",Integer.toString(this.hour) + ":" + addAzero(this.minute)));

        return tooltips;
    }
    public String addAzero(Integer time) {
        String out;
        if (time < 10) {
            out = "0" + Integer.toString(time);
        } else {
            out = Integer.toString(time);
        }
        return  out;
    }

    public void openContainer(ServerClient client) {
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.TIME_LOGIC_GATE_CONTAINER, this.tileX, this.tileY));
    }
}