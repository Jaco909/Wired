package engineer.forms.vanilla;

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

import java.awt.*;

public class NewCounterLogicGateContainerForm<T extends CounterLogicGateContainer> extends LogicGateContainerForm<T> {
    public NewCounterLogicGateContainerForm(Client client, T container) {
        super(client, 660, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "countergatelogictip")}));
        this.addWireCheckboxes(10, 40, new LocalMessage("ui", "counterinc"), container.entity, (e) -> e.incInputs, container.setIncInputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 130, 45, 100, false));
        this.addWireCheckboxes(140, 40, new LocalMessage("ui", "counterdec"), container.entity, (e) -> e.decInputs, container.setDecInputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 260, 45, 100, false));
        this.addWireCheckboxes(270, 40, new LocalMessage("ui", "rsreset"), container.entity, (e) -> e.resetInputs, container.setResetInputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 390, 45, 100, false));
        this.addWireCheckboxes(400, 40, new LocalMessage("ui", "wireoutputs"), container.entity, (e) -> e.wireOutputs, container.setOutputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 520, 45, 100, false));
        this.addComponent(new FormLocalLabel("ui", "countermax", new FontOptions(20), -1, 530, 40));
        FormSlider delay = ((FormSlider)this.addComponent(new FormSlider("", 530, 65, container.entity.getMaxValue(), 1, 256, 100))).onGrab((e) -> {
            if (!e.grabbed) {
                container.setMaxValue.runAndSend(((FormSlider)e.from).getValue());
            }

        });
        delay.onScroll((e) -> container.setMaxValue.runAndSend(((FormSlider)e.from).getValue()));
        delay.setValue(container.entity.getMaxValue());
        delay.drawValueInPercent = false;
        this.addComponent(new FormLocalLabel("ui", "countercurrent", new FontOptions(16), -1, 530, 110));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "countercurrentvalue","value", container.entity.currentValue), new FontOptions(16), -1, 620, 130));
    }
}