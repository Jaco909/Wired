package engineer.forms.vanilla;

import necesse.engine.GameEventListener;
import necesse.engine.Settings;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.client.Client;
import necesse.gfx.GameResources;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.lists.FormStringSelectList;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.presets.containerComponent.logicGate.LogicGateContainerForm;
import necesse.gfx.forms.presets.containerComponent.logicGate.SoundLogicGateContainerForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;
import necesse.inventory.container.logicGate.CounterLogicGateContainer;
import necesse.inventory.container.logicGate.SoundLogicGateContainer;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.gameLogicGate.entities.SoundLogicGateEntity;

public class NewSoundLogicGateContainerForm<T extends SoundLogicGateContainer> extends LogicGateContainerForm<T> {
    private final GameEventListener<LogicGateEntity.ApplyPacketEvent> applyListener;
    public FormStringSelectList sounds;
    public FormHorizontalIntScroll semitone;

    public NewSoundLogicGateContainerForm(Client client, final T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "soundgatelogictip")}));
        this.sounds = (FormStringSelectList)this.addComponent(new FormStringSelectList(0, 40, 160, this.getHeight() - 40, SoundLogicGateEntity.getSoundNames()));
        this.sounds.setSelected(container.entity.sound);
        this.sounds.onSelect((e) -> {
            this.playTest();
            container.setSound.runAndSend(e.index);
        });
        this.addComponent(new FormLocalLabel("ui", "soundsemitone", new FontOptions(16), 0, 250, 50));
        this.semitone = (FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(220, 75, 60, FormHorizontalScroll.DrawOption.value, new LocalMessage("ui", "soundsemitone"), container.entity.semitone, -12, 12));
        this.semitone.onChanged((e) -> {
            this.playTest();
            container.setSemitone.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue());
        });
        this.applyListener = (GameEventListener)container.entity.applyPacketEvents.addListener(new GameEventListener<LogicGateEntity.ApplyPacketEvent>() {
            public void onEvent(LogicGateEntity.ApplyPacketEvent event) {
                NewSoundLogicGateContainerForm.this.sounds.setSelected(container.entity.sound);
                NewSoundLogicGateContainerForm.this.semitone.setValue(container.entity.semitone);
            }
        });
    }

    public void playTest() {
        int sound = this.sounds.getSelectedIndex();
        SoundLogicGateEntity.playSound(sound, (Integer)this.semitone.getValue(), ((SoundLogicGateContainer)this.container).entity.tileX * 32 + 16, ((SoundLogicGateContainer)this.container).entity.tileY * 32 + 16);
    }

    public void dispose() {
        super.dispose();
        this.applyListener.dispose();
    }
}