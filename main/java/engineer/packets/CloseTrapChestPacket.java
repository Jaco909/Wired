package engineer.packets;

import engineer.objects.TrappedContainerObjectEntity;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;

public class CloseTrapChestPacket extends Packet {

    public final int x;
    public final int y;
    public final Packet content;

    public CloseTrapChestPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.x = reader.getNextInt();
        this.y = reader.getNextInt();
        this.content = reader.getNextContentPacket();
    }

    public CloseTrapChestPacket(int x, int y, ObjectEntity objectEntity) {
        this.x = x;
        this.y = y;
        this.content = new Packet();
        objectEntity.setupContentPacket(new PacketWriter(this.content));

        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(x);
        writer.putNextInt(y);
        writer.putNextContentPacket(this.content);
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        server.network.sendToAllClients(this);
        Level level = client.getLevel();
        ObjectEntity levelEnt = level.entityManager.getObjectEntity(this.x, this.y);
        if (levelEnt != null) {
            if (levelEnt instanceof TrappedContainerObjectEntity) {
                TrappedContainerObjectEntity invEnt = (TrappedContainerObjectEntity) levelEnt;
                //invEnt.pleaseClose = true;
                //invEnt.stayOpen = false;
                level.wireManager.updateWire(Math.round(this.x),Math.round(this.y),false);
                level.sendWireUpdatePacket(Math.round(this.x), Math.round(this.y));
            }
        }
    }
    public void processClient(NetworkPacket packet, Client client) {
        Level level = client.getLevel();
        ObjectEntity levelEnt = level.entityManager.getObjectEntity(this.x, this.y);
        if (levelEnt != null) {
            if (levelEnt instanceof TrappedContainerObjectEntity) {
                TrappedContainerObjectEntity invEnt = (TrappedContainerObjectEntity) levelEnt;
                //invEnt.pleaseClose = true;
                //invEnt.stayOpen = false;
                level.wireManager.updateWire(Math.round(this.x),Math.round(this.y),false);
                level.sendWireUpdatePacket(Math.round(this.x), Math.round(this.y));
            }
        }
    }
}
