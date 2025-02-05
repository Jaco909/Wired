package engineer.packets;

import engineer.objects.FakeWallEntity;
import engineer.objects.vanilla.NewArrowObjectEntity;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.inventory.InventoryItem;
import necesse.level.maps.Level;

public class ArrowTrapInventoryPacket extends Packet {
    public final int itemID;
    public final int itemAmount;
    public final int tileX;
    public final int tileY;
    public final Packet content;

    public ArrowTrapInventoryPacket(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.itemID = reader.getNextInt();
        this.itemAmount = reader.getNextInt();
        this.tileX = reader.getNextInt();
        this.tileY = reader.getNextInt();
        this.content = reader.getNextContentPacket();
    }

    public ArrowTrapInventoryPacket(int itemID, int itemAmount, ObjectEntity objectEntity) {
        this.itemID = itemID;
        this.itemAmount = itemAmount;
        this.tileX = objectEntity.getTileX();
        this.tileY = objectEntity.getTileY();
        this.content = new Packet();
        objectEntity.setupContentPacket(new PacketWriter(this.content));

        PacketWriter writer = new PacketWriter(this);
        writer.putNextInt(itemID);
        writer.putNextInt(itemAmount);
        writer.putNextInt(tileX);
        writer.putNextInt(tileY);
        writer.putNextContentPacket(this.content);
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        Level level = client.getLevel();
        ObjectEntity levelEnt = level.entityManager.getObjectEntity(this.tileX, this.tileY);
        if (levelEnt != null) {
            if (levelEnt instanceof NewArrowObjectEntity) {
                NewArrowObjectEntity trapEnt = (NewArrowObjectEntity) levelEnt;
                trapEnt.itemID = this.itemID;
                trapEnt.itemAmount = this.itemAmount;
            }
        }
    }
    public void processClient(NetworkPacket packet, Client client) {
        Level level = client.getLevel();
        ObjectEntity levelEnt = level.entityManager.getObjectEntity(this.tileX, this.tileY);
        if (levelEnt != null) {
            if (levelEnt instanceof NewArrowObjectEntity) {
                NewArrowObjectEntity trapEnt = (NewArrowObjectEntity) levelEnt;
                trapEnt.itemID = this.itemID;
                trapEnt.itemAmount = this.itemAmount;
                trapEnt.inventory.setItem(0,new InventoryItem(ItemRegistry.getItem(this.itemID),this.itemAmount),true);
            }
        }
    }
}
