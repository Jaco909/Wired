package engineer.forms.vanilla;

import necesse.engine.GameEventListener;
import necesse.engine.GameTileRange;
import necesse.engine.Settings;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.client.Client;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.Renderer;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.SharedTextureDrawOptions;
import necesse.gfx.drawables.SortedDrawable;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.localComponents.FormLocalCheckBox;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.presets.containerComponent.logicGate.LogicGateContainerForm;
import necesse.gfx.forms.presets.containerComponent.logicGate.SensorLogicGateContainerForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;
import necesse.inventory.container.logicGate.SRLatchLogicGateContainer;
import necesse.inventory.container.logicGate.SensorLogicGateContainer;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.gameLogicGate.entities.SensorLogicGateEntity;
import necesse.level.maps.hudManager.HudDrawElement;

import java.awt.*;
import java.util.List;

public class NewSensorLogicGateContainerForm<T extends SensorLogicGateContainer> extends LogicGateContainerForm<T> {
    protected HudDrawElement rangeElement;
    protected FormHorizontalScroll<Integer> rangeSelector;
    protected final GameEventListener<LogicGateEntity.ApplyPacketEvent> applyListener;

    public NewSensorLogicGateContainerForm(Client client, final T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "sensorgatelogictip")}));
        this.addWireCheckboxes(10, 40, new LocalMessage("ui", "wireoutputs"), container.entity, (e) -> e.wireOutputs, container.setOutputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 150, 45, 100, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "sensorlabel"), new FontOptions(20), -1, 160, 40));
        final FormCheckBox players = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "sensorplayers", 160, 65, container.entity.players))).onClicked((e) -> container.setPlayers.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox hostileMobs = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "sensorhostile", 160, 85, container.entity.hostileMobs))).onClicked((e) -> container.setHostileMobs.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox passiveMobs = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "sensorpassive", 160, 105, container.entity.passiveMobs))).onClicked((e) -> container.setPassiveMobs.runAndSend(((FormCheckBox)e.from).checked));
        this.rangeSelector = ((FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(160, 125, 100, FormHorizontalScroll.DrawOption.valueOnHover, new LocalMessage("ui", "sensorrange"), container.entity.range, 1, SensorLogicGateEntity.MAX_RANGE))).onChanged((e) -> container.setRange.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue()));
        this.applyListener = (GameEventListener)container.entity.applyPacketEvents.addListener(new GameEventListener<LogicGateEntity.ApplyPacketEvent>() {
            public void onEvent(LogicGateEntity.ApplyPacketEvent event) {
                players.checked = container.entity.players;
                hostileMobs.checked = container.entity.hostileMobs;
                passiveMobs.checked = container.entity.passiveMobs;
                NewSensorLogicGateContainerForm.this.rangeSelector.setValue(container.entity.range);
            }
        });
    }

    protected void init() {
        super.init();
        if (this.rangeElement != null) {
            this.rangeElement.remove();
        }

        this.rangeElement = new HudDrawElement() {
            public void addDrawables(List<SortedDrawable> list, GameCamera camera, PlayerMob perspective) {
                if (NewSensorLogicGateContainerForm.this.rangeSelector.isHovering()) {
                    GameTileRange tileRange = SensorLogicGateEntity.getTileRange(((SensorLogicGateContainer)NewSensorLogicGateContainerForm.this.container).entity.range);
                    final SharedTextureDrawOptions options = tileRange.getDrawOptions(new Color(255, 255, 255, 200), new Color(255, 255, 255, 75), ((SensorLogicGateContainer)NewSensorLogicGateContainerForm.this.container).entity.tileX, ((SensorLogicGateContainer)NewSensorLogicGateContainerForm.this.container).entity.tileY, camera);
                    if (options != null) {
                        list.add(new SortedDrawable() {
                            public int getPriority() {
                                return -1000000;
                            }

                            public void draw(TickManager tickManager) {
                                options.draw();
                            }
                        });
                    }

                }
            }
        };
        this.client.getLevel().hudManager.addElement(this.rangeElement);
    }

    public void draw(TickManager tickManager, PlayerMob perspective, Rectangle renderBox) {
        if (this.rangeSelector.isHovering()) {
            Renderer.hudManager.fadeHUD();
        }

        super.draw(tickManager, perspective, renderBox);
    }

    public void dispose() {
        super.dispose();
        this.applyListener.dispose();
        if (this.rangeElement != null) {
            this.rangeElement.remove();
        }

    }
}