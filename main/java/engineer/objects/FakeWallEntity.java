package engineer.objects;

import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;

import java.awt.*;

public class FakeWallEntity extends ObjectEntity {
    public int wallID = 0;

    public FakeWallEntity(Level level, int x, int y) {
        super(level, "wall", x, y);
    }

    public void setID(int wallID){
        this.wallID = wallID;
    }

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addInt("wallID", this.wallID);
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.wallID = save.getInt("wallID",0);
    }
    public void setupContentPacket(PacketWriter writer) {
        super.setupContentPacket(writer);
        writer.putNextInt(this.wallID);
    }

    public void applyContentPacket(PacketReader reader) {
        super.applyContentPacket(reader);
        this.wallID = reader.getNextInt();
    }
}