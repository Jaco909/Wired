package engineer.forms.storage;

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

public class StorageLogicGateForm<T extends StorageLogicGateContainer> extends LogicGateContainerForm<T> {
    protected FormHorizontalScroll<Integer> redTriggerSelector;
    protected FormHorizontalScroll<Integer> greenTriggerSelector;
    protected FormHorizontalScroll<Integer> blueTriggerSelector;
    protected FormHorizontalScroll<Integer> yellowTriggerSelector;
    protected final GameEventListener<LogicGateEntity.ApplyPacketEvent> applyListener;

    public StorageLogicGateForm(Client client, final T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "storagelogictip")}));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "storageSensorLabel"), new FontOptions(16), -1, 4, 40));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "redslots"), new FontOptions(12), -1, 4, 65));
        this.redTriggerSelector = (FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(100, 65, 100, FormHorizontalScroll.DrawOption.value, new LocalMessage("ui", "redslotsvalue"), container.entity.redTrigger, 0, container.entity.foundSlots));
        this.redTriggerSelector.onChanged((e) -> {
            container.setRedTriggerSlots.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue());
        });
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "greenslots"), new FontOptions(12), -1, 4, 85));
        this.greenTriggerSelector = (FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(100, 85, 100, FormHorizontalScroll.DrawOption.value, new LocalMessage("ui", "greenslotsvalue"), container.entity.greenTrigger, 0, container.entity.foundSlots));
        this.greenTriggerSelector.onChanged((e) -> {
            container.setGreenTriggerSlots.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue());
        });
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "blueslots"), new FontOptions(12), -1, 4, 105));
        this.blueTriggerSelector = (FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(100, 105, 100, FormHorizontalScroll.DrawOption.value, new LocalMessage("ui", "blueslotsvalue"), container.entity.blueTrigger, 0, container.entity.foundSlots));
        this.blueTriggerSelector.onChanged((e) -> {
            container.setBlueTriggerSlots.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue());
        });
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "yellowslots"), new FontOptions(12), -1, 4, 125));
        this.yellowTriggerSelector = (FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(100, 125, 100, FormHorizontalScroll.DrawOption.value, new LocalMessage("ui", "yellowslotsvalue"), container.entity.yellowTrigger, 0, container.entity.foundSlots));
        this.yellowTriggerSelector.onChanged((e) -> {
            container.setYellowTriggerSlots.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue());
        });
        final FormCheckBox redInverted = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "timeinverted", 210, 65, container.entity.redInverted))).onClicked((e) -> container.setRedInverted.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox greenInverted = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "timeinverted", 210, 85, container.entity.greenInverted))).onClicked((e) -> container.setGreenInverted.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox blueInverted = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "timeinverted", 210, 105, container.entity.blueInverted))).onClicked((e) -> container.setBlueInverted.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox yellowInverted = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "timeinverted", 210, 125, container.entity.yellowInverted))).onClicked((e) -> container.setYellowInverted.runAndSend(((FormCheckBox)e.from).checked));

        this.applyListener = (GameEventListener)container.entity.applyPacketEvents.addListener(new GameEventListener<LogicGateEntity.ApplyPacketEvent>() {
            public void onEvent(LogicGateEntity.ApplyPacketEvent event) {
                StorageLogicGateForm.this.redTriggerSelector.setValue(container.entity.redTrigger);
                StorageLogicGateForm.this.greenTriggerSelector.setValue(container.entity.greenTrigger);
                StorageLogicGateForm.this.blueTriggerSelector.setValue(container.entity.blueTrigger);
                StorageLogicGateForm.this.yellowTriggerSelector.setValue(container.entity.yellowTrigger);
                redInverted.checked = container.entity.redInverted;
                greenInverted.checked = container.entity.greenInverted;
                blueInverted.checked = container.entity.blueInverted;
                yellowInverted.checked = container.entity.yellowInverted;
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