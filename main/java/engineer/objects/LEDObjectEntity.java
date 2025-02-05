package engineer.objects;

import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.level.maps.Level;

import java.awt.*;

public class LEDObjectEntity extends ObjectEntity {
    public Color color1;
    public Color color2;
    public Color color3;
    public Color color4;
    public boolean[] wireInputs;


    public LEDObjectEntity(Level level, int x, int y) {
        super(level, "ledpanel", x, y);
        this.wireInputs = new boolean[4];
        this.color1 = new Color(200,200,200);
        this.color2 = new Color(200,200,200);
        this.color3 = new Color(200,200,200);
        this.color4 = new Color(200,200,200);
    }

    /*public void setColors(Color color){
        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }*/

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addInt("color1",this.color1.getRGB());
        save.addInt("color2",this.color2.getRGB());
        save.addInt("color3",this.color3.getRGB());
        save.addInt("color4",this.color4.getRGB());
        save.addSmallBooleanArray("wireInputs", this.wireInputs);
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.color1 = new Color(save.getInt("color1"));
        this.color2 = new Color(save.getInt("color2"));
        this.color3 = new Color(save.getInt("color3"));
        this.color4 = new Color(save.getInt("color4"));
        this.wireInputs = save.getSmallBooleanArray("wireInputs");
    }
    public void setupContentPacket(PacketWriter writer) {
        super.setupContentPacket(writer);
        writer.putNextInt(this.color1.getRGB());
        writer.putNextInt(this.color2.getRGB());
        writer.putNextInt(this.color3.getRGB());
        writer.putNextInt(this.color4.getRGB());
        for(int i = 0; i < 4; ++i) {
            writer.putNextBoolean(this.wireInputs[i]);
        }
    }

    public void applyContentPacket(PacketReader reader) {
        super.applyContentPacket(reader);
        this.color1 = new Color(reader.getNextInt());
        this.color2 = new Color(reader.getNextInt());
        this.color3 = new Color(reader.getNextInt());
        this.color4 = new Color(reader.getNextInt());
        for(int i = 0; i < 4; ++i) {
            this.wireInputs[i] = reader.getNextBoolean();
        }
    }
}