package engineer.objects.vanilla;

import engineer.forms.arrowTrap.ArrowContainer;
import engineer.forms.arrowTrap.ArrowContainerForm;
import engineer.packets.ArrowTrapInventoryPacket;
import necesse.engine.gameLoop.tickManager.Performance;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketTrapTriggered;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.registries.ProjectileRegistry;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.save.levelData.InventorySave;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.InventoryObjectEntity;
import necesse.entity.objectEntity.TrapObjectEntity;
import necesse.entity.objectEntity.interfaces.OEUsers;
import necesse.entity.projectile.Projectile;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.fairType.FairTypeDrawOptions;
import necesse.inventory.Inventory;
import necesse.inventory.InventoryFilter;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.arrowItem.ArrowItem;
import necesse.level.maps.Level;
import necesse.level.maps.presets.PresetRotation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NewArrowObjectEntity extends InventoryObjectEntity {
    public long cooldown;
    private long startCooldownTime;
    public int itemID = 0;
    public int itemAmount = 0;
    public final Inventory inventory;
    public final int slots = 1;
    private boolean interactedWith;
    public final OEUsers.Users users = this.constructUsersObject(2000L);

    public NewArrowObjectEntity(Level level, int x, int y) {
        super(level, x, y,1);
        this.cooldown = 1000L;
        this.startCooldownTime = 0L;
        this.shouldSave = false;
        this.inventory = new Inventory(slots) {
            public void updateSlot(int slot) {
                super.updateSlot(slot);
                NewArrowObjectEntity.this.onInventorySlotUpdated(slot);
                if (level.isLoadingComplete()) {
                    NewArrowObjectEntity.this.triggerInteracted();
                }

            }
        };
        this.inventory.filter = new InventoryFilter() {
            public boolean isItemValid(int slot, InventoryItem item) {
                return NewArrowObjectEntity.this.isItemValid(slot, item);
            }

            public int getItemStackLimit(int slot, InventoryItem item) {
                return NewArrowObjectEntity.this.getItemStackLimit(slot, item);
            }
        };
        if (level != null && !level.isLoadingComplete()) {
            this.interactedWith = false;
            this.inventory.spoilRateModifier = -1415.0F;
        } else {
            this.interactedWith = true;
        }
    }
    public boolean shouldSave() {
        return true;
    }
    public void addSaveData(SaveData save) {
        save.addInt("itemID",this.itemID);
        save.addInt("itemAmount",this.itemAmount);
    }
    public void applyLoadData(LoadData save) {
        this.itemID = save.getInt("itemID");
        this.itemAmount = save.getInt("itemAmount");
        firstSaveSetup();
    }
    public void addPresetSaveData(SaveData save) {
        this.addSaveData(save);
    }
    public void firstSaveSetup() {
        if (this.isServer()) {
            this.inventory.clearSlot(0);
            this.inventory.setItem(0, new InventoryItem(ItemRegistry.getItem(this.itemID), this.itemAmount), true);
            /*if (this.inventory.getItem(0) != null) {
                this.getLevel().getServer().network.sendToAllClients(new ArrowTrapInventoryPacket(this.itemID, this.itemAmount, this));
            } else {
                System.out.println("error1");
                this.getLevel().getServer().network.sendToAllClients(new ArrowTrapInventoryPacket(0, 0, this));
            }*/
        } else {
            //this.inventory.clearSlot(0);
            this.inventory.setItem(0, new InventoryItem(ItemRegistry.getItem(this.itemID), this.itemAmount), true);
        }
    }

    public void applyPresetLoadData(LoadData save, boolean mirrorX, boolean mirrorY, PresetRotation rotation) {
        this.applyLoadData(save);
    }
    public void setupContentPacket(PacketWriter writer) {
        //super.setupContentPacket(writer);
        writer.putNextInt(this.itemID);
        writer.putNextInt(this.itemAmount);
        //this.inventory.writeContent(writer);
    }
    public void applyContentPacket(PacketReader reader) {
        //super.applyContentPacket(reader);
        this.itemID = reader.getNextInt();
        this.itemAmount = reader.getNextInt();
        //this.inventory.override(Inventory.getInventory(reader));
    }
    public Inventory getInventory() {
        return this.inventory;
    }

    public boolean isItemValid(int slot, InventoryItem item) {
        return item.item.getStringID().contains("arrow");
    }

    public int getItemStackLimit(int slot, InventoryItem item) {
        return item == null ? Integer.MAX_VALUE : item.itemStackSize();
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

    protected void onInventorySlotUpdated(int slot) {
        if (!this.isServer()) {
            if (this.inventory.getItem(slot) != null) {
                if (this.itemID != this.inventory.getItem(slot).item.getID() || this.itemAmount != this.inventory.getAmount(slot)) {
                    this.getLevel().getClient().network.sendPacket(new ArrowTrapInventoryPacket(this.inventory.getItem(slot).item.getID(), this.inventory.getAmount(slot), this));
                }
            } else {
                this.getLevel().getClient().network.sendPacket(new ArrowTrapInventoryPacket(0, 0, this));
            }
        }
    }


    //Trap entity code
    public boolean shouldRequestPacket() {
        return true;
    }

    public boolean onCooldown() {
        return this.startCooldownTime + this.cooldown > this.getWorldEntity().getTime();
    }

    public void startCooldown() {
        this.startCooldownTime = this.getWorldEntity().getTime();
    }

    public long getTimeSinceActivated() {
        return this.getWorldEntity().getTime() - this.startCooldownTime;
    }

    public void onClientTrigger() {
    }

    public void sendClientTriggerPacket() {
        if (this.isServer()) {
            this.getLevel().getServer().network.sendToClientsWithTile(new PacketTrapTriggered(this.getTileX(), this.getTileY()), this.getLevel(), this.getTileX(), this.getTileY());
        }
    }

    public Point getPos(int x, int y, int dir) {
        if (dir == 0) {
            return new Point(x, y - 1);
        } else if (dir == 1) {
            return new Point(x + 1, y);
        } else if (dir == 2) {
            return new Point(x, y + 1);
        } else {
            return dir == 3 ? new Point(x - 1, y) : new Point(x, y);
        }
    }

    public boolean otherWireActive(int exceptionWireID) {
        for(int i = 0; i < 4; ++i) {
            if (i != exceptionWireID && this.getLevel().wireManager.isWireActive(this.getX(), this.getY(), i)) {
                return true;
            }
        }

        return false;
    }

    public void triggerTrap(int wireID, int dir) {
        if (this.isClient()) {
            //this.getClient().getFocusForm().dispose();
            this.getClient().closeContainer(true);
        }
        if (!this.isClient() && !this.onCooldown()) {
            if (!this.otherWireActive(wireID)) {
                Point position = this.getPos(this.getX(), this.getY(), dir);
                Point targetDir = this.getDir(dir);
                int xPos = position.x * 32;
                if (targetDir.x == 0) {
                    xPos += 16;
                } else if (targetDir.x == -1) {
                    xPos += 30;
                } else if (targetDir.x == 1) {
                    xPos += 2;
                }

                int yPos = position.y * 32;
                if (targetDir.y == 0) {
                    yPos += 16;
                } else if (targetDir.y == -1) {
                    yPos += 30;
                } else if (targetDir.y == 1) {
                    yPos += 2;
                }

                float var10003 = (float)xPos;
                float var10004 = (float)yPos;
                float var10005 = (float)(xPos + targetDir.x);
                ArrowItem arrow;
                Projectile projectile;
                if (this.itemID == 0) {
                    arrow = (ArrowItem)ItemRegistry.getItem("stonearrow");
                    projectile = ProjectileRegistry.getProjectile("traparrow");
                } else {
                    try {
                        arrow = (ArrowItem) ItemRegistry.getItem(this.itemID);
                    } catch (Exception e) {
                        System.err.println("INVALID ARROW TRAP PROJECTILE USED (mod arrow item). ERROR ITEM: " + ItemRegistry.getItem(this.itemID).getStringID());
                        System.err.println("REVERTING TO DEFAULT ARROW");
                        arrow = (ArrowItem)ItemRegistry.getItem("stonearrow");
                    }
                    try {
                        projectile = ProjectileRegistry.getProjectile(arrow.getStringID());
                    } catch (Exception e) {
                        projectile = ProjectileRegistry.getProjectile("traparrow");
                    }

                }

                projectile.x = var10003;
                projectile.y = var10004;
                projectile.setTarget(var10005,(float)(yPos + targetDir.y));
                projectile.speed = 200;
                projectile.distance = 400;
                projectile.setDamage(new GameDamage(arrow.damage*3.5F,(arrow.armorPen*2)+10,arrow.critChance+0,2F,1F));
                projectile.knockback = 10;
                projectile.setOwner(null);
                this.getLevel().entityManager.projectiles.add(projectile);
                this.startCooldown();
                if (this.itemAmount-- == 0) {
                    this.inventory.setItem(0,new InventoryItem(ItemRegistry.getItem(0),0),true);
                } else if (this.itemID == 0 && this.itemAmount <= 0) {
                    this.inventory.setItem(0,new InventoryItem(ItemRegistry.getItem(0),0),true);
                } else {
                    this.inventory.setItem(0,new InventoryItem(ItemRegistry.getItem(this.itemID),this.itemAmount),true);
                    this.getLevel().getServer().network.sendToAllClients(new ArrowTrapInventoryPacket(this.itemID, this.itemAmount, this));
                }
            }
        }
    }

    public Point getDir(int dir) {
        if (dir == 0) {
            return new Point(0, -1);
        } else if (dir == 1) {
            return new Point(1, 0);
        } else if (dir == 2) {
            return new Point(0, 1);
        } else {
            return dir == 3 ? new Point(-1, 0) : new Point();
        }
    }
}