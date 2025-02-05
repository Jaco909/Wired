package engineer.forms.arrowTrap;

import engineer.forms.vanilla.FormContentIconButtonShaded;
import necesse.engine.Settings;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.network.client.Client;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.containerSlot.FormContainerSlot;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.presets.containerComponent.ContainerForm;
import necesse.gfx.forms.presets.containerComponent.settlement.SettlementObjectStatusFormManager;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.ui.ButtonColor;
import necesse.inventory.container.object.OEInventoryContainer;

import java.awt.*;

public class ArrowContainerForm<T extends ArrowContainer> extends ContainerForm<T> {
    public SettlementObjectStatusFormManager settlementObjectFormManager;
    public FormLabelEdit label;
    public FormContentIconButton edit;
    public FormContainerSlot[] slots;
    public LocalMessage renameTip;

    public ArrowContainerForm(Client client, final T container) {
        super(client, 400, 100, container);
        this.addComponent(new FormLocalLabel(container.objectEntity.getObject().getLocalization(), new FontOptions(20), -1, 4, 4));
        //this.addComponent(new FormLocalLabel(new LocalMessage("ui", "wallSensorLabel"), new FontOptions(16), -1, 160, 40));
        FormFlow flow = new FormFlow(34);
        this.addSlots(flow);
        flow.next(4);
    }

    public boolean shouldOpenInventory() {
        return true;
    }

    protected void addSlots(FormFlow flow) {
        this.slots = new FormContainerSlot[((OEInventoryContainer)this.container).INVENTORY_END - ((OEInventoryContainer)this.container).INVENTORY_START + 1];
        int currentY = flow.next();

        for(int i = 0; i < this.slots.length; ++i) {
            int slotIndex = i + ((OEInventoryContainer)this.container).INVENTORY_START;
            int x = i % 10;
            if (x == 0) {
                currentY = flow.next(40);
            }

            this.slots[i] = (FormContainerSlot)this.addComponent(new FormContainerSlot(this.client, this.container, slotIndex, 4 + x * 40, currentY));
        }

    }

    protected void init() {
        super.init();
    }

    public void draw(TickManager tickManager, PlayerMob perspective, Rectangle renderBox) {
        super.draw(tickManager, perspective, renderBox);
    }

    public void dispose() {
        super.dispose();
        this.container.oeUsers.stopUser(this.client.getPlayer());
    }
}