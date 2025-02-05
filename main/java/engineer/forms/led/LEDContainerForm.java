package engineer.forms.led;

import engineer.forms.vanilla.FormContentIconButtonShaded;
import engineer.packets.LEDColorPacket;
import necesse.engine.Settings;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.PacketReader;
import necesse.engine.network.client.Client;
import necesse.gfx.GameBackground;
import necesse.gfx.fairType.FairType;
import necesse.gfx.fairType.TypeParsers;
import necesse.gfx.fairType.parsers.TypeParser;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.localComponents.FormLocalCheckBox;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.presets.containerComponent.ContainerForm;
import necesse.gfx.forms.presets.containerComponent.object.SignContainerForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;
import necesse.inventory.container.object.SignContainer;

import java.awt.*;

public class LEDContainerForm<T extends LEDContainer> extends ContainerForm<T> {

    public static TypeParser[] getParsers(FontOptions fontOptions) {
        return new TypeParser[]{TypeParsers.GAME_COLOR, TypeParsers.REMOVE_URL, TypeParsers.URL_OPEN, TypeParsers.ItemIcon(fontOptions.getSize()), TypeParsers.MobIcon(fontOptions.getSize()), TypeParsers.InputIcon(fontOptions)};
    }

    public LEDContainerForm(Client client, final T container) {
        super(client, 785, 180, container);
        this.addComponent(new FormLocalLabel(container.objectEntity.getObject().getLocalization(), new FontOptions(20), -1, 4, 4));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "ledlogictip")}));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "wireinputs"), new FontOptions(16), -1, 15, 35));
        final FormCheckBox redInput = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "wire0", 5, 65, container.objectEntity.wireInputs[0]))).onClicked((e) -> container.setRedInput.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox greenInput = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "wire1", 5, 85, container.objectEntity.wireInputs[1]))).onClicked((e) -> container.setGreenInput.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox blueInput = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "wire2", 5, 105, container.objectEntity.wireInputs[2]))).onClicked((e) -> container.setBlueInput.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox yellowInput = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "wire3", 5, 125, container.objectEntity.wireInputs[3]))).onClicked((e) -> container.setYellowInput.runAndSend(((FormCheckBox)e.from).checked));
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 105, 30, 175, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "LEDRed"), new FontOptions(16), -1, 120, 35));
        FormColorPicker picker1 = (FormColorPicker) this.addComponent(new FormColorPicker(120, 65, 145, 100));
        picker1.setSelectedColor(container.objectEntity.color1);
        picker1.onChanged((e) -> {
            Color selectedColor = picker1.getSelectedColor();
            container.setColor1.runAndSend(selectedColor.getRGB());
            //container.setColor1.executePacket(new PacketReader(new LEDColorPacket(Math.round(container.objectEntity.x),Math.round(container.objectEntity.y),container.objectEntity,selectedColor.getRGB(),0)));
        });
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 275, 30, 175, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "LEDGreen"), new FontOptions(16), -1, 290, 35));
        FormColorPicker picker2 = (FormColorPicker) this.addComponent(new FormColorPicker(290, 65, 145, 100));
        picker2.setSelectedColor(container.objectEntity.color2);
        picker2.onChanged((e) -> {
            Color selectedColor = picker2.getSelectedColor();
            container.setColor2.runAndSend(selectedColor.getRGB());
        });
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 445, 30, 175, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "LEDBlue"), new FontOptions(16), -1, 460, 35));
        FormColorPicker picker3 = (FormColorPicker) this.addComponent(new FormColorPicker(460, 65, 145, 100));
        picker3.setSelectedColor(container.objectEntity.color3);
        picker3.onChanged((e) -> {
            Color selectedColor = picker3.getSelectedColor();
            container.setColor3.runAndSend(selectedColor.getRGB());
        });
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 615, 30, 175, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "LEDYellow"), new FontOptions(16), -1, 630, 35));
        FormColorPicker picker4 = (FormColorPicker) this.addComponent(new FormColorPicker(630, 65, 145, 100));
        picker4.setSelectedColor(container.objectEntity.color4);
        picker4.onChanged((e) -> {
            Color selectedColor = picker4.getSelectedColor();
            container.setColor4.runAndSend(selectedColor.getRGB());
        });
    }
}