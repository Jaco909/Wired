package engineer.forms.arrowTrap;

import engineer.objects.vanilla.NewArrowObjectEntity;
import necesse.engine.network.NetworkClient;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.objectEntity.interfaces.OEInventory;
import necesse.entity.objectEntity.interfaces.OEUsers;
import necesse.inventory.*;
import necesse.inventory.container.customAction.BooleanCustomAction;
import necesse.inventory.container.customAction.EmptyCustomAction;
import necesse.inventory.container.customAction.IntCustomAction;
import necesse.inventory.container.customAction.StringCustomAction;
import necesse.inventory.container.logicGate.WireSelectCustomAction;
import necesse.inventory.container.object.OEInventoryContainer;
import necesse.inventory.container.settlement.SettlementContainerObjectStatusManager;
import necesse.inventory.container.slots.ContainerSlot;
import necesse.inventory.container.slots.OEInventoryContainerSlot;
import necesse.level.maps.Level;

import java.awt.*;

public class ArrowContainer extends OEInventoryContainer {

    public ArrowContainer(final NetworkClient client, int uniqueSeed, final OEInventory oeInventory, PacketReader reader) {
        super(client, uniqueSeed,oeInventory,reader);
    }
}