package engineer.forms.projectile;

import engineer.forms.light.LightLogicGateContainer;
import engineer.forms.light.ProjectileLogicGateContainer;
import engineer.forms.vanilla.FormContentIconButtonShaded;
import engineer.forms.vanilla.NewSensorLogicGateContainerForm;
import engineer.objects.logic.ProjectileLogicGateEntity;
import necesse.engine.GameEventListener;
import necesse.engine.GameTileRange;
import necesse.engine.Settings;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.client.Client;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.SharedTextureDrawOptions;
import necesse.gfx.drawables.SortedDrawable;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.localComponents.FormLocalCheckBox;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.presets.containerComponent.logicGate.LogicGateContainerForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;
import necesse.inventory.container.logicGate.SensorLogicGateContainer;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.gameLogicGate.entities.SensorLogicGateEntity;
import necesse.level.maps.hudManager.HudDrawElement;

import java.awt.*;
import java.util.List;

public class ProjectileLogicGateForm<T extends ProjectileLogicGateContainer> extends LogicGateContainerForm<T> {
    protected HudDrawElement rangeElement;
    protected FormHorizontalScroll<Integer> rangeSelector;
    protected final GameEventListener<LogicGateEntity.ApplyPacketEvent> applyListener;

    public ProjectileLogicGateForm(Client client, final T container) {
        super(client, 400, 160, container);
        this.addComponent(new FormLocalLabel(container.entity.getLogicGate().getLocalization(), new FontOptions(20), -1, 4, 4));
        this.addComponent(new FormContentIconButtonShaded(this.getWidth()-25, 5, FormInputSize.SIZE_20, ButtonColor.BASE, Settings.UI.button_help_20, new GameMessage[]{new LocalMessage("ui", "projectilelogictip")}));
        this.addWireCheckboxes(10, 40, new LocalMessage("ui", "wireoutputs"), container.entity, (e) -> e.wireOutputs, container.setOutputs);
        this.addComponent(new FormBreakLine(FormBreakLine.ALIGN_BEGINNING, 150, 45, 100, false));
        this.addComponent(new FormLocalLabel(new LocalMessage("ui", "SensorLabel"), new FontOptions(16), -1, 160, 40));
        final FormCheckBox player = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "ProjPlayer", 160, 65, container.entity.fromPlayer))).onClicked((e) -> container.setPlayer.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox mob = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "ProjMob", 160, 85, container.entity.fromMob))).onClicked((e) -> container.setMob.runAndSend(((FormCheckBox)e.from).checked));
        //final FormCheckBox trap = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "ProjTrap", 160, 105, container.entity.fromTrap))).onClicked((e) -> container.setTrap.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox damage = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "ProjDamage", 160, 105, container.entity.fromDamaging))).onClicked((e) -> container.setDamage.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox other = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "ProjOther", 245, 65, container.entity.fromOther))).onClicked((e) -> container.setOther.runAndSend(((FormCheckBox)e.from).checked));
        final FormCheckBox inverted = ((FormLocalCheckBox)this.addComponent(new FormLocalCheckBox("ui", "timeinverted", 245, 85, container.entity.inverted))).onClicked((e) -> container.setinverted.runAndSend(((FormCheckBox)e.from).checked));
        this.rangeSelector = ((FormHorizontalIntScroll)this.addComponent(new FormHorizontalIntScroll(160, 130, 180, FormHorizontalScroll.DrawOption.valueOnHover, new LocalMessage("ui", "sensorrange"), container.entity.range, 1, 5))).onChanged((e) -> container.setRange.runAndSend((Integer)((FormHorizontalScroll)e.from).getValue()));

        this.applyListener = (GameEventListener)container.entity.applyPacketEvents.addListener(new GameEventListener<LogicGateEntity.ApplyPacketEvent>() {
            public void onEvent(LogicGateEntity.ApplyPacketEvent event) {
                player.checked = container.entity.fromPlayer;
                mob.checked = container.entity.fromMob;
                //trap.checked = container.entity.fromTrap;
                damage.checked = container.entity.fromDamaging;
                other.checked = container.entity.fromOther;
                inverted.checked = container.entity.inverted;
                ProjectileLogicGateForm.this.rangeSelector.setValue(container.entity.range);
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
                if (ProjectileLogicGateForm.this.rangeSelector.isHovering()) {
                    GameTileRange tileRange = ProjectileLogicGateEntity.getTileRange(((ProjectileLogicGateContainer)ProjectileLogicGateForm.this.container).entity.range);
                    final SharedTextureDrawOptions options = tileRange.getDrawOptions(new Color(255, 255, 255, 200), new Color(255, 255, 255, 75), ((ProjectileLogicGateContainer)ProjectileLogicGateForm.this.container).entity.tileX, ((ProjectileLogicGateContainer)ProjectileLogicGateForm.this.container).entity.tileY, camera);
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
        super.draw(tickManager, perspective, renderBox);
    }

    public void dispose() {
        super.dispose();
        this.applyListener.dispose();
    }
}