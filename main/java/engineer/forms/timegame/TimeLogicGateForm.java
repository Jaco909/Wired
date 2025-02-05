package engineer.forms.timegame;

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

public class TimeLogicGateForm<T extends TimeLogicGateContainer> extends LogicGateContainerForm<T> {
    protected FormHorizontalScroll<Integer> hourSelector;
    protected FormHorizontalScroll<Integer> minuteSelector;
    protected final GameEventListener<LogicGateEntity.ApplyPacketEvent> applyListener;

    public TimeLogicGateForm(Client client, final T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "timelogictip")}));
        this.addWireCheckboxes(10, 40, new LocalMessage("ui", "wireoutputs"), container.entity, (e) -> e.wireOutputs, container.setOutputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 150, 45, 100, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "timeSensorLabel"), new FontOptions(16), -1, 160, 40));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "hourrange"), new FontOptions(12), -1, 160, 65));
        this.hourSelector = (FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(215, 65, 100, FormHorizontalScroll.DrawOption.value, new LocalMessage("ui", "hourvalue"), container.entity.hour, 0, 23));
        this.hourSelector.onChanged((e) -> {
            container.setHour.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue());
        });
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "minuterange"), new FontOptions(12), -1, 160, 85));
        this.minuteSelector = (FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(215, 85, 100, FormHorizontalScroll.DrawOption.value, new LocalMessage("ui", "minutevalue"), container.entity.minute, 0, 59));
        this.minuteSelector.onChanged((e) -> {
            container.setMinute.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue());
        });
        final FormCheckBox inverted = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "timeinverted", 160, 105, container.entity.inverted))).onClicked((e) -> container.setinverted.runAndSend(((FormCheckBox)e.from).checked));
        this.applyListener = (GameEventListener)container.entity.applyPacketEvents.addListener(new GameEventListener<LogicGateEntity.ApplyPacketEvent>() {
            public void onEvent(LogicGateEntity.ApplyPacketEvent event) {
                TimeLogicGateForm.this.hourSelector.setValue(container.entity.hour);
                TimeLogicGateForm.this.minuteSelector.setValue(container.entity.minute);
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