package engineer.objects.logic;

import engineer.Engineer;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.gameLogicGate.entities.SimpleLogicGateEntity;
import necesse.level.gameLogicGate.entities.XOrLogicGateEntity;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;

public class XNORLogicGateEntity extends XOrLogicGateEntity {
    public XNORLogicGateEntity(GameLogicGate logicGate, TilePosition pos) {
        super(logicGate, pos);
    }

    public boolean condition() {
        return !super.condition();
    }
}