package engineer.patches.logicTutorial;

import engineer.Engineer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.level.gameLogicGate.entities.SRLatchLogicGateEntity;
import necesse.level.gameLogicGate.entities.SensorLogicGateEntity;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = SensorLogicGateEntity.class, name = "openContainer", arguments = {ServerClient.class})
public class SensorLogicGatePatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static void onExit(@Advice.This SensorLogicGateEntity gate, @Advice.Argument(0) ServerClient client){
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.NEW_SENSOR_LOGIC_GATE_CONTAINER, gate.tileX, gate.tileY));
    }
}
