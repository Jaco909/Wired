package engineer.forms.fakewall;

import engineer.forms.vanilla.FormContentIconButtonShaded;
import necesse.engine.GameEventListener;
import necesse.engine.Settings;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.client.Client;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.localComponents.FormLocalCheckBox;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.presets.containerComponent.logicGate.LogicGateContainerForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;
import necesse.level.gameLogicGate.entities.LogicGateEntity;

import java.awt.*;

public class FakeWallLogicGateForm<T extends FakeWallLogicGateContainer> extends LogicGateContainerForm<T> {
    protected final GameEventListener<LogicGateEntity.ApplyPacketEvent> applyListener;

    public FakeWallLogicGateForm(Client client, final T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        //this.addComponent(new FormLocalLabel("ui", "null", (new FontOptions(12)).color(Settings.UI.errorTextColor), -1, this.getWidth()-10, 8));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "fakewalllogictip")}));
        this.addWireCheckboxes(10, 40, new LocalMessage("ui", "wireinputs"), container.entity, (e) -> e.wireInputs, container.setInputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 150, 45, 100, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "wallSensorLabel"), new FontOptions(16), -1, 160, 40));

        final FormCheckBox inverted = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "timeinverted", 160, 65, container.entity.inverted))).onClicked((e) -> container.setinverted.runAndSend(((FormCheckBox)e.from).checked));
        this.applyListener = (GameEventListener)container.entity.applyPacketEvents.addListener(new GameEventListener<LogicGateEntity.ApplyPacketEvent>() {
            public void onEvent(LogicGateEntity.ApplyPacketEvent event) {
                inverted.checked = container.entity.inverted;
            }
        });
    }

    protected void init() {
        super.init();
    }

    public void draw(TickManager tickManager, PlayerMob perspective, Rectangle renderBox) {
        super.draw(tickManager, perspective, renderBox);
    }

    public void dispose() {
        super.dispose();
        this.applyListener.dispose();
    }
}