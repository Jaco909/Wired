package engineer.packets;

import engineer.objects.FakeWallEntity;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;

public class FakeWallIDPacket extends Packet {
    public final int wallID;
    public final int tileX;
    public final int tileY;
    public final Packet content;

    public FakeWallIDPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.wallID = reader.getNextInt();
        this.tileX = reader.getNextInt();
        this.tileY = reader.getNextInt();
        this.content = reader.getNextContentPacket();
    }

    public FakeWallIDPacket(int wallID, int tileX, int tileY, ObjectEntity objectEntity) {
        this.wallID = wallID;
        this.tileX = tileX;
        this.tileY = tileY;
        this.content = new Packet();
        objectEntity.setupContentPacket(new PacketWriter(this.content));

        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(wallID);
        writer.putNextInt(tileX);
        writer.putNextInt(tileY);
        writer.putNextContentPacket(this.content);
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        Level level = client.getLevel();
        ObjectEntity levelEnt = level.entityManager.getObjectEntity(this.tileX, this.tileY);
        if (levelEnt != null) {
            if (levelEnt instanceof FakeWallEntity) {
                FakeWallEntity wallEnt = (FakeWallEntity) levelEnt;
                wallEnt.wallID = wallID;
            }
        }
        server.network.sendToAllClients(this);
    }
    public void processClient(NetworkPacket packet, Client client) {
        Level level = client.getLevel();
        ObjectEntity levelEnt = level.entityManager.getObjectEntity(this.tileX, this.tileY);
        if (levelEnt != null) {
            if (levelEnt instanceof FakeWallEntity) {
                FakeWallEntity wallEnt = (FakeWallEntity) levelEnt;
                wallEnt.wallID = wallID;
            }
        }
    }
}
