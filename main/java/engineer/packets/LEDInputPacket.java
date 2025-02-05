package engineer.packets;

import engineer.objects.LEDObjectEntity;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;

import java.awt.*;

public class LEDInputPacket extends Packet {

    public final boolean active;
    public final int wireID;
    public final int x;
    public final int y;
    public final Packet content;

    public LEDInputPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.active = reader.getNextBoolean();
        this.wireID = reader.getNextInt();
        this.x = reader.getNextInt();
        this.y = reader.getNextInt();
        this.content = reader.getNextContentPacket();
    }

    public LEDInputPacket(int x, int y, ObjectEntity objectEntity, boolean active, int wireID) {
        this.active = active;
        this.wireID = wireID;
        this.x = x;
        this.y = y;
        this.content = new Packet();
        objectEntity.setupContentPacket(new PacketWriter(this.content));

        PacketWriter writer = new PacketWriter(this);
        writer.putNextBoolean(active);
        writer.putNextInt(wireID);
        writer.putNextInt(x);
        writer.putNextInt(y);
        writer.putNextContentPacket(this.content);
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        System.out.println("packet recieved server");
        Level level = server.world.getLevel(client);
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(this.x, this.y);
        if (objectEntity instanceof LEDObjectEntity) {
            LEDObjectEntity ledEnt = (LEDObjectEntity) objectEntity;
            if (wireID == 0) {
                ledEnt.wireInputs[0] = true;
            } else if (wireID == 1) {
                ledEnt.wireInputs[1] = true;
            } else if (wireID == 2) {
                ledEnt.wireInputs[2] = true;
            } else {
                ledEnt.wireInputs[3] = true;
            }
        }
        server.network.sendToAllClients(this);
    }
    public void processClient(NetworkPacket packet, Client client) {
        System.out.println("packet recieved client");
        Level level = client.getLevel();
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(this.x, this.y);
        if (objectEntity instanceof LEDObjectEntity) {
            LEDObjectEntity ledEnt = (LEDObjectEntity) objectEntity;
            if (wireID == 0) {
                ledEnt.wireInputs[0] = true;
            } else if (wireID == 1) {
                ledEnt.wireInputs[1] = true;
            } else if (wireID == 2) {
                ledEnt.wireInputs[2] = true;
            } else {
                ledEnt.wireInputs[3] = true;
            }
        }
    }
}
