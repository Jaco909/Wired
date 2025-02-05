package engineer.objects;

import engineer.packets.OpenTrapChestPacket;
import necesse.engine.localization.Localization;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.container.object.OEInventoryContainer;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.ObjectDamagedTextureArray;
import necesse.level.gameObject.furniture.InventoryObject;
import necesse.level.maps.Level;

import java.awt.*;

public class TrappedContainerObject extends InventoryObject {
    public ObjectDamagedTextureArray texture;
    public ObjectDamagedTextureArray openTexture;
    protected String textureName;
    public int slots = 40;
    private boolean active;
    protected final GameRandom drawRandom;

    public TrappedContainerObject(String textureName, int slots, ToolType toolType, Color mapColor) {
        super(textureName,slots,new Rectangle(32, 32),toolType,mapColor);
        this.textureName = textureName;
        this.setItemCategory(new String[]{"wiring"});
        this.setCraftingCategory(new String[]{"wiring"});
        this.showsWire = true;
        this.isSwitch = true;
        this.isSwitched = false;
        this.toolType = toolType;
        this.mapColor = mapColor;
        this.slots = 40;
        this.objectHealth = 50;
        this.active = false;
        this.isLightTransparent = true;
        this.drawRandom = new GameRandom();
    }
    public GameTexture generateItemTexture() {
        GameTexture test = new GameTexture(GameTexture.fromFile("items/" + this.getStringID().substring(0,this.getStringID().length()-7)),0,0,32,32);
        GameTexture merger = GameTexture.fromFile("items/trapicon");
        test.merge(merger,12,14,MergeFunction.NORMAL);
        return test;
    }
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = new ListGameTooltips();
        tooltips.add(Localization.translate("itemtooltip", "placetip"));
        tooltips.add(Localization.translate("itemtooltip", "wiredtip"));
        return tooltips;
    }
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new TrappedContainerObjectEntity(level, x, y, this.slots);
    }
    public void interact(Level level, int x, int y, PlayerMob player) {
        if (level.isServer()) {
            OEInventoryContainer.openAndSendContainer(ContainerRegistry.OE_INVENTORY_CONTAINER, player.getServerClient(), level, x, y);
        } else {
            ObjectEntity objectEntity = level.entityManager.getObjectEntity(x, y);
            if (objectEntity instanceof TrappedContainerObjectEntity) {
                TrappedContainerObjectEntity invEnt = (TrappedContainerObjectEntity) objectEntity;
                player.getClient().network.sendPacket(new OpenTrapChestPacket(true, x, y, invEnt));
            }
        }
    }
}
