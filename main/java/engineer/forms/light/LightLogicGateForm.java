package engineer.forms.light;

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

public class LightLogicGateForm<T extends LightLogicGateContainer> extends LogicGateContainerForm<T> {
    protected FormHorizontalScroll<Integer> lightSelector;
    protected final GameEventListener<LogicGateEntity.ApplyPacketEvent> applyListener;

    public LightLogicGateForm(Client client, final T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        //this.addComponent(new FormLocalLabel("ui", "null", (new FontOptions(12)).color(Settings.UI.errorTextColor), -1, this.getWidth()-10, 8));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "lightlogictip")}));
        this.addWireCheckboxes(10, 40, new LocalMessage("ui", "wireoutputs"), container.entity, (e) -> e.wireOutputs, container.setOutputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 150, 45, 100, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "lightSensorLabel"), new FontOptions(16), -1, 160, 40));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "lightrange"), new FontOptions(12), -1, 160, 65));
        this.lightSelector = (FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(245, 65, 100, FormHorizontalScroll.DrawOption.value, new LocalMessage("ui", "lightvalue"), container.entity.light, 0, 15));
        this.lightSelector.onChanged((e) -> {
            container.setLight.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue());
        });
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "lightrangecurrent", "lightLevel",Math.round(container.entity.level.getLightLevel(container.entity.tileX,container.entity.tileY).getLevel()/10)), new FontOptions(12), -1, 160, 85));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "lightrangenote"), new FontOptions(12), -1, 160, 125));

        final FormCheckBox inverted = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "timeinverted", 160, 105, container.entity.inverted))).onClicked((e) -> container.setinverted.runAndSend(((FormCheckBox)e.from).checked));
        this.applyListener = (GameEventListener)container.entity.applyPacketEvents.addListener(new GameEventListener<LogicGateEntity.ApplyPacketEvent>() {
            public void onEvent(LogicGateEntity.ApplyPacketEvent event) {
                LightLogicGateForm.this.lightSelector.setValue(container.entity.light);
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