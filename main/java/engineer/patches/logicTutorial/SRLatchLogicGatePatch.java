package engineer.patches.logicTutorial;

import engineer.Engineer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.level.gameLogicGate.entities.SRLatchLogicGateEntity;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = SRLatchLogicGateEntity.class, name = "openContainer", arguments = {ServerClient.class})
public class SRLatchLogicGatePatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static void onExit(@Advice.This SRLatchLogicGateEntity gate, @Advice.Argument(0) ServerClient client){
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.NEW_SRLATCH_LOGIC_GATE_CONTAINER, gate.tileX, gate.tileY));
    }
}
