package engineer.patches.logicTutorial;

import engineer.Engineer;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.level.gameLogicGate.entities.TimerLogicGateEntity;
import net.bytebuddy.asm.Advice;

@ModMethodPatch(target = TimerLogicGateEntity.class, name = "openContainer", arguments = {ServerClient.class})
public class TimerLogicGatePatch {
    @Advice.OnMethodEnter(
            skipOn = Advice.OnNonDefaultValue.class
    )
    static boolean onEnter() {
        return true;
    }
    @Advice.OnMethodExit()
    static void onExit(@Advice.This TimerLogicGateEntity gate, @Advice.Argument(0) ServerClient client){
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.NEW_TIMER_LOGIC_GATE_CONTAINER, gate.tileX, gate.tileY));
    }
}
