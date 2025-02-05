package engineer.packets;

import engineer.objects.FakeWallEntity;
import engineer.objects.logic.StorageLogicGateEntity;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.maps.Level;

public class UpdateSotrageSizePacket  extends Packet {
    public final int prevSize;
    public final int size;
    public final int tileX;
    public final int tileY;

    public UpdateSotrageSizePacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.prevSize = reader.getNextInt();
        this.size = reader.getNextInt();
        this.tileX = reader.getNextInt();
        this.tileY = reader.getNextInt();
    }

    public UpdateSotrageSizePacket(int prevSize, int size, int tileX, int tileY) {
        this.prevSize = prevSize;
        this.size = size;
        this.tileX = tileX;
        this.tileY = tileY;

        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(prevSize);
        writer.putNextInt(size);
        writer.putNextInt(tileX);
        writer.putNextInt(tileY);
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        Level level = client.getLevel();
        LogicGateEntity levelEnt = level.logicLayer.getEntity(this.tileX,this.tileY);
        if (levelEnt != null) {
            if (levelEnt instanceof StorageLogicGateEntity) {
                StorageLogicGateEntity storeEnt = (StorageLogicGateEntity) levelEnt;
                System.out.println(this.size);
                storeEnt.foundSlots = this.size;
                if (this.prevSize > this.size) {
                    storeEnt.redTrigger = 0;
                    storeEnt.greenTrigger = 0;
                    storeEnt.blueTrigger = 0;
                    storeEnt.yellowTrigger = 0;
                }
            }
        }
        server.network.sendToAllClients(this);
    }
    public void processClient(NetworkPacket packet, Client client) {
        Level level = client.getLevel();
        LogicGateEntity levelEnt = level.logicLayer.getEntity(this.tileX,this.tileY);
        if (levelEnt != null) {
            if (levelEnt instanceof StorageLogicGateEntity) {
                StorageLogicGateEntity storeEnt = (StorageLogicGateEntity) levelEnt;
                System.out.println(this.size);
                storeEnt.foundSlots = this.size;
                if (this.prevSize > this.size) {
                    storeEnt.redTrigger = 0;
                    storeEnt.greenTrigger = 0;
                    storeEnt.blueTrigger = 0;
                    storeEnt.yellowTrigger = 0;
                }
            }
        }
    }
}
