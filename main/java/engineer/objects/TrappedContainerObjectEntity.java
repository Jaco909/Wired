package engineer.objects;

import engineer.packets.OpenTrapChestPacket;
import necesse.engine.Settings;
import necesse.engine.gameLoop.tickManager.Performance;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.save.levelData.InventorySave;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.entity.objectEntity.interfaces.OEInventory;
import necesse.entity.objectEntity.interfaces.OEUsers;
import necesse.gfx.fairType.FairType;
import necesse.gfx.fairType.FairTypeDrawOptions;
import necesse.gfx.forms.presets.containerComponent.object.OEInventoryContainerForm;
import necesse.gfx.gameFont.FontOptions;
import necesse.gfx.gameTooltips.*;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryFilter;
import necesse.inventory.InventoryItem;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.interfaces.OpenSound;
import necesse.level.maps.Level;

import java.util.ArrayList;

public class TrappedContainerObjectEntity extends ObjectEntity implements OEInventory, OEUsers {
    private String name;
    private FairTypeDrawOptions textDrawOptions;
    private int textDrawFontSize;
    public final Inventory inventory;
    public final int slots;
    public boolean active;
    public int tileX;
    public int tileY;
    private boolean pleaseClose;
    private boolean stayOpen;
    private boolean interactedWith;
    public final OEUsers.Users users = this.constructUsersObject(2000L);

    public TrappedContainerObjectEntity(final Level level, int x, int y, int slots) {
        super(level, "inventory", x, y);
        this.slots = slots;
        this.active = false;
        this.name = "";
        this.tileX = x;
        this.tileY = y;
        this.pleaseClose = false;
        this.stayOpen = false;
        this.setInventoryName("");
        this.inventory = new Inventory(slots) {
            public void updateSlot(int slot) {
                super.updateSlot(slot);
                TrappedContainerObjectEntity.this.onInventorySlotUpdated(slot);
                if (level.isLoadingComplete()) {
                    TrappedContainerObjectEntity.this.triggerInteracted();
                }

            }
        };
        this.inventory.filter = new InventoryFilter() {
            public boolean isItemValid(int slot, InventoryItem item) {
                return TrappedContainerObjectEntity.this.isItemValid(slot, item);
            }

            public int getItemStackLimit(int slot, InventoryItem item) {
                return TrappedContainerObjectEntity.this.getItemStackLimit(slot, item);
            }
        };
        if (level != null && !level.isLoadingComplete()) {
            this.interactedWith = false;
            this.inventory.spoilRateModifier = -1415.0F;
        } else {
            this.interactedWith = true;
        }

    }

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addSafeString("name", this.name);
        save.addSaveData(InventorySave.getSave(this.inventory));
        if (this.interactedWith) {
            save.addBoolean("interactedWith", this.interactedWith);
        }
        save.addBoolean("active",this.active);
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.setInventoryName(save.getSafeString("name", this.name));
        this.inventory.override(InventorySave.loadSave(save.getFirstLoadDataByName("INVENTORY")));
        boolean loadedInteractedWith = save.getBoolean("interactedWith", this.interactedWith, false);
        if (loadedInteractedWith != this.interactedWith) {
            this.interactedWith = loadedInteractedWith;
            if (this.interactedWith && this.inventory.spoilRateModifier == -1415.0F) {
                this.inventory.spoilRateModifier = 1.0F;
            } else if (!this.interactedWith) {
                this.inventory.spoilRateModifier = 0.0F;
            }
        }
        this.active = save.getBoolean("active",false);

    }

    public void setupContentPacket(PacketWriter writer) {
        super.setupContentPacket(writer);
        this.users.writeUsersSpawnPacket(writer);
        this.inventory.writeContent(writer);
        writer.putNextString(this.name);
        writer.putNextBoolean(this.interactedWith);
        writer.putNextBoolean(this.active);
    }

    public void applyContentPacket(PacketReader reader) {
        super.applyContentPacket(reader);
        this.users.readUsersSpawnPacket(reader, this);
        this.inventory.override(Inventory.getInventory(reader));
        this.setInventoryName(reader.getNextString());
        boolean loadedInteractedWith = reader.getNextBoolean();
        if (loadedInteractedWith != this.interactedWith) {
            this.interactedWith = loadedInteractedWith;
            if (this.interactedWith && this.inventory.spoilRateModifier == -1415.0F) {
                this.inventory.spoilRateModifier = 1.0F;
            } else if (!this.interactedWith) {
                this.inventory.spoilRateModifier = 0.0F;
            }
        }
        this.active = reader.getNextBoolean();
    }

    public ArrayList<InventoryItem> getDroppedItems() {
        ArrayList<InventoryItem> list = new ArrayList();

        for(int i = 0; i < this.inventory.getSize(); ++i) {
            if (!this.inventory.isSlotClear(i)) {
                list.add(this.inventory.getItem(i));
            }
        }

        return list;
    }

    public void serverTick() {
        super.serverTick();
        Performance.record(this.getLevel().tickManager(), "tickItems", () -> this.inventory.tickItems(this));
        this.serverTickInventorySync(this.getLevel().getServer(), this);
        this.users.serverTick(this);
        if (this.pleaseClose && !this.active) {
            this.pleaseClose = false;
            this.stayOpen = false;
            this.getLevel().wireManager.updateWire(Math.round(this.x),Math.round(this.y),false);
            this.getLevel().sendWireUpdatePacket(Math.round(this.x), Math.round(this.y));
        }
        if (this.stayOpen) {
            this.getLevel().wireManager.updateWire(Math.round(this.x), Math.round(this.y), true);
            this.getLevel().sendWireUpdatePacket(Math.round(this.x), Math.round(this.y));
        }
    }

    public void clientTick() {
        super.clientTick();
        Performance.record(this.getLevel().tickManager(), "tickItems", () -> this.inventory.tickItems(this));
        this.users.clientTick(this);
        if (this.pleaseClose && !this.active) {
            this.pleaseClose = false;
            this.stayOpen = false;
            this.getLevel().wireManager.updateWire(Math.round(this.x),Math.round(this.y),false);
            this.getLevel().sendWireUpdatePacket(Math.round(this.x), Math.round(this.y));
        }
        if (this.stayOpen) {
            this.getLevel().wireManager.updateWire(Math.round(this.x), Math.round(this.y), true);
            this.getLevel().sendWireUpdatePacket(Math.round(this.x), Math.round(this.y));
        }
    }

    protected void onInventorySlotUpdated(int slot) {
    }

    public void onMouseHover(PlayerMob perspective, boolean debug) {
        super.onMouseHover(perspective, debug);
        if (!this.name.isEmpty()) {
            GameTooltipManager.addTooltip(new FairTypeTooltip(this.getTextDrawOptions()), TooltipLocation.INTERACT_FOCUS);
        }

    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean isItemValid(int slot, InventoryItem item) {
        return true;
    }

    public int getItemStackLimit(int slot, InventoryItem item) {
        return item == null ? Integer.MAX_VALUE : item.itemStackSize();
    }

    public void triggerInteracted() {
        if (!this.interactedWith) {
            if (this.inventory.spoilRateModifier == -1415.0F) {
                this.inventory.spoilRateModifier = 1.0F;
            }

            this.interactedWith = true;
            this.markDirty();
        }
        if (this.active) {
            this.getLevel().wireManager.updateWire(Math.round(this.x), Math.round(this.y), true);
            this.getLevel().sendWireUpdatePacket(Math.round(this.x), Math.round(this.y));
        }
    }

    public GameMessage getInventoryName() {
        return (GameMessage)(this.name.isEmpty() ? this.getLevel().getObjectName(this.getTileX(), this.getTileY()) : new StaticMessage(this.name));
    }

    public void setInventoryName(String name) {
        String oldName = this.name;
        if (this.getLevel().getObjectName(this.getX(), this.getY()).translate().equals(name)) {
            this.name = "";
        } else {
            this.name = name;
        }

        if (!this.name.equals(oldName)) {
            this.textDrawOptions = null;
        }

    }

    private FairTypeDrawOptions getTextDrawOptions() {
        if (this.textDrawOptions == null || this.textDrawFontSize != Settings.tooltipTextSize) {
            FairType type = new FairType();
            FontOptions fontOptions = (new FontOptions(Settings.tooltipTextSize)).outline();
            type.append(fontOptions, this.getInventoryName().translate());
            type.applyParsers(OEInventoryContainerForm.getParsers(fontOptions));
            this.textDrawOptions = type.getDrawOptions(FairType.TextAlign.LEFT);
            this.textDrawFontSize = fontOptions.getSize();
        }

        return this.textDrawOptions;
    }

    public boolean canSetInventoryName() {
        return true;
    }

    public OEUsers.Users getUsersObject() {
        return this.users;
    }

    public boolean canUse(Mob mob) {
        return true;
    }

    public void onUsageChanged(Mob mob, boolean using) {
    }

    public void remove() {
        super.remove();
        this.users.onRemoved(this);
    }

    public void onIsInUseChanged(boolean isInUse) {
        if (this.isClient()) {
            GameObject object = this.getObject();
            if (object instanceof OpenSound) {
                if (isInUse) {
                    this.stayOpen = true;
                    ((OpenSound) object).playOpenSound(this.getLevel(), this.getX(), this.getY());
                } else {
                    this.pleaseClose = true;
                    this.stayOpen = false;
                    ((OpenSound) object).playCloseSound(this.getLevel(), this.getX(), this.getY());
                    this.getClient().network.sendPacket(new OpenTrapChestPacket(false, Math.round(this.x), Math.round(this.y), this));
                }
            }
        }
    }

    public GameTooltips getMapTooltips() {
        ListGameTooltips tooltips = new ListGameTooltips();
        tooltips.add(new FairTypeTooltip(this.getTextDrawOptions()));
        return tooltips;
    }
}