package engineer.packets;

import engineer.objects.LEDObjectEntity;
import necesse.engine.GameLog;
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

public class LEDColorPacket extends Packet {

    public final int color;
    public final int wireID;
    public final int x;
    public final int y;
    public final Packet content;

    public LEDColorPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.color = reader.getNextInt();
        this.wireID = reader.getNextInt();
        this.x = reader.getNextInt();
        this.y = reader.getNextInt();
        this.content = reader.getNextContentPacket();
    }

    public LEDColorPacket(int x, int y, ObjectEntity objectEntity, int color, int wireID) {
        this.color = color;
        this.wireID = wireID;
        this.x = x;
        this.y = y;
        this.content = new Packet();
        objectEntity.setupContentPacket(new PacketWriter(this.content));

        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(color);
        writer.putNextInt(wireID);
        writer.putNextInt(x);
        writer.putNextInt(y);
        writer.putNextContentPacket(this.content);
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        Level level = server.world.getLevel(client);
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(this.x, this.y);
        if (objectEntity instanceof LEDObjectEntity) {
            LEDObjectEntity ledEnt = (LEDObjectEntity) objectEntity;
            if (wireID == 0) {
                ledEnt.color1 = new Color(this.color);
            } else if (wireID == 1) {
                ledEnt.color2 = new Color(this.color);
            } else if (wireID == 2) {
                ledEnt.color3 = new Color(this.color);
            } else {
                ledEnt.color4 = new Color(this.color);
            }
        }
        server.network.sendToAllClients(this);
    }
    public void processClient(NetworkPacket packet, Client client) {
        Level level = client.getLevel();
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(this.x, this.y);
        if (objectEntity instanceof LEDObjectEntity) {
            LEDObjectEntity ledEnt = (LEDObjectEntity) objectEntity;
            if (wireID == 0) {
                ledEnt.color1 = new Color(this.color);
            } else if (wireID == 1) {
                ledEnt.color2 = new Color(this.color);
            } else if (wireID == 2) {
                ledEnt.color3 = new Color(this.color);
            } else {
                ledEnt.color4 = new Color(this.color);
            }
        }
    }
}
