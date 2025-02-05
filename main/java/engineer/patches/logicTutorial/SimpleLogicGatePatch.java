package engineer.patches.logicTutorial;

import engineer.Engineer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.level.gameLogicGate.entities.SimpleLogicGateEntity;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = SimpleLogicGateEntity.class, name = "openContainer", arguments = {ServerClient.class})
public class SimpleLogicGatePatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static void onExit(@Advice.This SimpleLogicGateEntity gate, @Advice.Argument(0) ServerClient client){
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.NEW_SIMPLE_LOGIC_GATE_CONTAINER, gate.tileX, gate.tileY));
    }
}
