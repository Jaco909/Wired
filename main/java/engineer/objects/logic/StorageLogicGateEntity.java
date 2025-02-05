package engineer.objects.logic;

import engineer.Engineer;
import engineer.packets.UpdateSotrageSizePacket;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.InventoryObjectEntity;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;

public class StorageLogicGateEntity extends LogicGateEntity {
    public boolean[] wireOutputs;
    private boolean redActive;
    private boolean greenActive;
    private boolean blueActive;
    private boolean yellowActive;
    public int foundSlots;
    public int redTrigger;
    public int greenTrigger;
    public int blueTrigger;
    public int yellowTrigger;
    public boolean redInverted;
    public boolean greenInverted;
    public boolean blueInverted;
    public boolean yellowInverted;
    private boolean[] outputs;

    public StorageLogicGateEntity(GameLogicGate logicGate, Level level, int tileX, int tileY) {
        super(logicGate, level, tileX, tileY);
        this.wireOutputs = new boolean[4];
        this.redActive = false;
        this.greenActive = false;
        this.blueActive = false;
        this.yellowActive = false;
        this.foundSlots = this.sizeCheck();
        this.redTrigger = 0;
        this.greenTrigger = 0;
        this.blueTrigger = 0;
        this.yellowTrigger = 0;
        this.redInverted = false;
        this.greenInverted = false;
        this.blueInverted = false;
        this.yellowInverted = false;

    }

    public StorageLogicGateEntity(GameLogicGate logicGate, TilePosition pos) {
        this(logicGate, pos.level, pos.tileX, pos.tileY);
    }

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addSmallBooleanArray("wireOutputs", this.wireOutputs);
        save.addInt("foundSlots", this.foundSlots);
        save.addInt("redTrigger", this.redTrigger);
        save.addInt("greenTrigger", this.greenTrigger);
        save.addInt("blueTrigger", this.blueTrigger);
        save.addInt("yellowTrigger", this.yellowTrigger);
        save.addBoolean("redInverted", this.redInverted);
        save.addBoolean("greenInverted", this.greenInverted);
        save.addBoolean("blueInverted", this.blueInverted);
        save.addBoolean("yellowInverted", this.yellowInverted);
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.wireOutputs = save.getSmallBooleanArray("wireOutputs", this.wireOutputs);
        this.foundSlots = save.getInt("foundSlots", 0);
        this.redTrigger = save.getInt("redTrigger", 0);
        this.greenTrigger = save.getInt("greenTrigger", 0);
        this.blueTrigger = save.getInt("blueTrigger", 0);
        this.yellowTrigger = save.getInt("yellowTrigger", 0);
        this.redInverted = save.getBoolean("redInverted", false);
        this.greenInverted = save.getBoolean("greenInverted", false);
        this.blueInverted = save.getBoolean("blueInverted", false);
        this.yellowInverted = save.getBoolean("yellowInverted", false);
        this.updateOutputs(true);
    }

    public void writePacket(PacketWriter writer) {
        super.writePacket(writer);

        for(int i = 0; i < 4; ++i) {
            writer.putNextBoolean(this.wireOutputs[i]);
        }
        writer.putNextInt(this.foundSlots);
        writer.putNextInt(this.redTrigger);
        writer.putNextInt(this.greenTrigger);
        writer.putNextInt(this.blueTrigger);
        writer.putNextInt(this.yellowTrigger);
        writer.putNextBoolean(this.redInverted);
        writer.putNextBoolean(this.greenInverted);
        writer.putNextBoolean(this.blueInverted);
        writer.putNextBoolean(this.yellowInverted);
    }

    public void applyPacket(PacketReader reader) {
        super.applyPacket(reader);

        for(int i = 0; i < 4; ++i) {
            this.wireOutputs[i] = reader.getNextBoolean();
        }
        this.foundSlots = reader.getNextInt();
        this.redTrigger = reader.getNextInt();
        this.greenTrigger = reader.getNextInt();
        this.blueTrigger = reader.getNextInt();
        this.yellowTrigger = reader.getNextInt();
        this.redInverted = reader.getNextBoolean();
        this.greenInverted = reader.getNextBoolean();
        this.blueInverted = reader.getNextBoolean();
        this.yellowInverted = reader.getNextBoolean();
    }

    public void tick() {
        super.tick();
        if (this.isServer()) {
            this.fillCheck();
            if (this.sizeCheck() != this.foundSlots) {
                this.getServer().network.sendToAllClients(new UpdateSotrageSizePacket(this.foundSlots,this.sizeCheck(),this.tileX,this.tileY));
                this.foundSlots = sizeCheck();
            }
        }
    }

    private int sizeCheck() {
        InventoryObjectEntity invObjectEntity;
        try {
            invObjectEntity = (InventoryObjectEntity) level.entityManager.getObjectEntity(this.tileX, this.tileY);
        } catch (Exception e) {
            invObjectEntity = null;
        }
        if (invObjectEntity != null) {
            return invObjectEntity.inventory.getSize();
        } else {
            return 0;
        }
    }

    private void fillCheck() {
        InventoryObjectEntity invObjectEntity;
        try {
            invObjectEntity = (InventoryObjectEntity) level.entityManager.getObjectEntity(this.tileX, this.tileY);
        } catch (Exception e) {
            invObjectEntity = null;
        }
        if (invObjectEntity != null) {
            int slots = invObjectEntity.inventory.getSize();
            int filled = 0;
            for(int i = 0; i <= slots; ++i) {
                if (!invObjectEntity.inventory.isSlotClear(i)) {
                    filled++;
                }
            }
            if ((filled >= this.redTrigger && !this.redInverted) || (filled < this.redTrigger && this.redInverted)) {
                this.redActive = true;
            } else {
                this.redActive = false;
            }
            if ((filled >= this.greenTrigger && !this.greenInverted) || (filled < this.greenTrigger && this.greenInverted)) {
                this.greenActive = true;
            } else {
                this.greenActive = false;
            }
            if ((filled >= this.blueTrigger && !this.blueInverted) || (filled < this.blueTrigger && this.blueInverted)) {
                this.blueActive = true;
            } else {
                this.blueActive = false;
            }
            if ((filled >= this.yellowTrigger && !this.yellowInverted) || (filled < this.yellowTrigger && this.yellowInverted)) {
                this.yellowActive = true;
            } else {
                this.yellowActive = false;
            }
            this.updateOutputs(true);
        }
    }

    public void updateOutputs(boolean forceUpdate) {
        for(int i = 0; i < 4; ++i) {
            if (i == 0) {
                this.setOutput(i, this.redActive, forceUpdate);
            } else if (i == 1) {
                this.setOutput(i, this.greenActive, forceUpdate);
            } else if (i == 2) {
                this.setOutput(i, this.blueActive, forceUpdate);
            } else if (i == 3) {
                this.setOutput(i, this.yellowActive, forceUpdate);
            }
        }

    }

    public ListGameTooltips getTooltips(PlayerMob perspective, boolean debug) {
        ListGameTooltips tooltips = super.getTooltips(perspective, debug);
        //tooltips.add(Localization.translate("logictooltips", "timesensortime","time",Integer.toString(this.hour) + ":" + addAzero(this.minute)));

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
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.STORAGE_LOGIC_GATE_CONTAINER, this.tileX, this.tileY));
    }
}