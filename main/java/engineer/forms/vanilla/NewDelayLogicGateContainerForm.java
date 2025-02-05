package engineer.forms.vanilla;

import necesse.engine.GameEventListener;
import necesse.engine.Settings;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.client.Client;
import necesse.gfx.forms.components.FormBreakLine;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.FormSlider;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.presets.containerComponent.logicGate.LogicGateContainerForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;
import necesse.inventory.container.logicGate.CounterLogicGateContainer;
import necesse.inventory.container.logicGate.DelayLogicGateContainer;
import necesse.level.gameLogicGate.entities.LogicGateEntity;

public class NewDelayLogicGateContainerForm<T extends DelayLogicGateContainer> extends LogicGateContainerForm<T> {
    private final GameEventListener<LogicGateEntity.ApplyPacketEvent> applyListener;

    public NewDelayLogicGateContainerForm(Client client, final T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "delaygatelogictip")}));
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 135, 45, 100, false));
        this.addWireCheckboxes(10, 40, new LocalMessage("ui", "wireinputs"), container.entity, (e) -> e.wireInputs, container.setInputs);
        this.addWireCheckboxes(150, 40, new LocalMessage("ui", "wireoutputs"), container.entity, (e) -> e.wireOutputs, container.setOutputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 265, 45, 100, false));
        this.addComponent(new FormLocalLabel("ui", "bufferticks", new FontOptions(20), -1, 280, 40));
        final FormSlider delay = ((FormSlider)this.addComponent(new FormSlider("", 280, 65, container.entity.delayTicks, 1, 200, 100))).onGrab((e) -> {
            if (!e.grabbed) {
                container.setDelay.runAndSend(((FormSlider)e.from).getValue());
            }

        });
        delay.onScroll((e) -> container.setDelay.runAndSend(((FormSlider)e.from).getValue()));
        delay.setValue(container.entity.delayTicks);
        delay.drawValueInPercent = false;
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "buffertip", new Object[]{"ticks", 20}), new FontOptions(12), -1, 280, delay.getY() + delay.getTotalHeight() + 5, 100));
        this.applyListener = (GameEventListener)container.entity.applyPacketEvents.addListener(new GameEventListener<LogicGateEntity.ApplyPacketEvent>() {
            public void onEvent(LogicGateEntity.ApplyPacketEvent event) {
                delay.setValue(container.entity.delayTicks);
            }
        });
    }

    public void dispose() {
        super.dispose();
        this.applyListener.dispose();
    }
}