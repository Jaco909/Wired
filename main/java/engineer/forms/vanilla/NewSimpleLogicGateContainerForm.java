package engineer.forms.vanilla;

import necesse.engine.Settings;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.client.Client;
import necesse.gfx.forms.components.FormBreakLine;
import necesse.gfx.forms.components.FormInputSize;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.presets.containerComponent.logicGate.LogicGateContainerForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;
import necesse.inventory.container.logicGate.SimpleLogicGateContainer;

public class NewSimpleLogicGateContainerForm<T extends SimpleLogicGateContainer> extends LogicGateContainerForm<T> {
    public NewSimpleLogicGateContainerForm(Client client, T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        if (this.container.entity.getLogicGate().getStringID().equalsIgnoreCase("andgate")) {
            this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "andgatelogictip")}));
        } else if (this.container.entity.getLogicGate().getStringID().equalsIgnoreCase("orgate")) {
            this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "orgatelogictip")}));
        } else if (this.container.entity.getLogicGate().getStringID().equalsIgnoreCase("norgate")) {
            this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "norgatelogictip")}));
        } else if (this.container.entity.getLogicGate().getStringID().equalsIgnoreCase("xorgate")) {
            this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "xorgatelogictip")}));
        } else if (this.container.entity.getLogicGate().getStringID().equalsIgnoreCase("nandgate")) {
            this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "nandgatelogictip")}));
        } else if (this.container.entity.getLogicGate().getStringID().equalsIgnoreCase("xnorgate")) {
            this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "xnorgatelogictip")}));
        }
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 150, 45, 100, false));
        this.addWireCheckboxes(10, 40, new LocalMessage("ui", "wireinputs"), container.entity, (e) -> e.wireInputs, container.setInputs);
        this.addWireCheckboxes(165, 40, new LocalMessage("ui", "wireoutputs"), container.entity, (e) -> e.wireOutputs, container.setOutputs);
    }
}