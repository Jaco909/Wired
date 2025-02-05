package engineer.objects.logic;

import engineer.Engineer;
import necesse.engine.GameTileRange;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ContainerRegistry;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;
import necesse.engine.util.GameMath;
import necesse.entity.projectile.Projectile;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;

import java.awt.*;
import java.util.Iterator;

public class ProjectileLogicGateEntity extends LogicGateEntity {
    public boolean[] wireOutputs;
    public static GameTileRange[] TILE_RANGES;
    public int range;
    public boolean fromPlayer;
    public boolean fromMob;
    public boolean fromDamaging;
    public boolean fromTrap;
    public boolean fromOther;
    private boolean active;
    public boolean inverted;
    public static GameTileRange getTileRange(int range) {
        range = GameMath.limit(range, 1, TILE_RANGES.length);
        GameTileRange tileRange = TILE_RANGES[range - 1];
        if (tileRange == null) {
            tileRange = new GameTileRange(range, new Point[0]);
            TILE_RANGES[range - 1] = tileRange;
        }

        return tileRange;
    }

    public ProjectileLogicGateEntity(GameLogicGate logicGate, Level level, int tileX, int tileY) {
        super(logicGate, level, tileX, tileY);
        this.wireOutputs = new boolean[4];
        this.range = 3;
        this.fromPlayer = false;
        this.fromMob = false;
        this.fromDamaging = false;
        this.fromTrap = false;
        this.fromOther = false;
        this.active = false;
        this.inverted = false;
    }

    public ProjectileLogicGateEntity(GameLogicGate logicGate, TilePosition pos) {
        this(logicGate, pos.level, pos.tileX, pos.tileY);
    }

    public void addSaveData(SaveData save) {
        super.addSaveData(save);
        save.addSmallBooleanArray("wireOutputs", this.wireOutputs);
        save.addInt("range", this.range);
        save.addBoolean("fromPlayer", this.fromPlayer);
        save.addBoolean("fromMob", this.fromMob);
        save.addBoolean("fromDamaging", this.fromDamaging);
        save.addBoolean("fromTrap", this.fromTrap);
        save.addBoolean("fromOther", this.fromOther);
        save.addBoolean("inverted", this.inverted);
    }

    public void applyLoadData(LoadData save) {
        super.applyLoadData(save);
        this.wireOutputs = save.getSmallBooleanArray("wireOutputs", this.wireOutputs);
        this.range = save.getInt("range", 3);
        this.fromPlayer = save.getBoolean("fromPlayer", false);
        this.fromMob = save.getBoolean("fromMob", false);
        this.fromDamaging = save.getBoolean("fromDamaging", false);
        this.fromTrap = save.getBoolean("fromTrap", false);
        this.fromOther = save.getBoolean("fromOther", false);
        this.inverted = save.getBoolean("inverted", false);
        this.updateOutputs(true);
    }

    public void writePacket(PacketWriter writer) {
        super.writePacket(writer);

        for(int i = 0; i < 4; ++i) {
            writer.putNextBoolean(this.wireOutputs[i]);
        }
        writer.putNextInt(this.range);
        writer.putNextBoolean(this.fromPlayer);
        writer.putNextBoolean(this.fromMob);
        writer.putNextBoolean(this.fromDamaging);
        writer.putNextBoolean(this.fromTrap);
        writer.putNextBoolean(this.fromOther);
        writer.putNextBoolean(this.inverted);
    }

    public void applyPacket(PacketReader reader) {
        super.applyPacket(reader);

        for(int i = 0; i < 4; ++i) {
            this.wireOutputs[i] = reader.getNextBoolean();
        }
        this.range = reader.getNextInt();
        this.fromPlayer = reader.getNextBoolean();
        this.fromMob = reader.getNextBoolean();
        this.fromDamaging = reader.getNextBoolean();
        this.fromTrap = reader.getNextBoolean();
        this.fromOther = reader.getNextBoolean();
        this.inverted = reader.getNextBoolean();
    }

    public void tick() {
        super.tick();
        if (this.isServer()) {
            this.checkState();
        }
    }

    public void checkState() {
        if (!this.level.entityManager.projectiles.getInRegionByTileRange(this.tileX,this.tileY,this.range).isEmpty()) {
            Iterator projectiles = this.level.entityManager.projectiles.getInRegionByTileRange(this.tileX,this.tileY,this.range).iterator();
            while (projectiles.hasNext()) {
                Projectile projectile = (Projectile)projectiles.next();
                /*if (projectile.isTrapAttacker() && this.fromTrap) {
                    activeFlip();
                    this.updateOutputs(true);
                    break;
                } else*/ if (projectile.canHitMobs && this.fromDamaging) {
                    activeFlip();
                    this.updateOutputs(true);
                    break;
                } else if (projectile.getOwner() != null) {
                    if (projectile.getOwner().isPlayer && this.fromPlayer) {
                        activeFlip();
                        this.updateOutputs(true);
                        break;
                    } else if (!projectile.getOwner().isPlayer && this.fromMob) {
                        activeFlip();
                        this.updateOutputs(true);
                        break;
                    }
                } else if (this.fromOther || projectile.isTrapAttacker()) {
                    activeFlip();
                    this.updateOutputs(true);
                    break;
                }
            }
        } else if (this.level.entityManager.projectiles.getInRegionByTileRange(this.tileX,this.tileY,this.range).isEmpty() && this.inverted) {
            this.active = true;
            this.updateOutputs(true);
        } else if (!this.inverted) {
            this.active = false;
            this.updateOutputs(true);
        }
    }

    public void activeFlip() {
        if (!this.inverted) {
            this.active = true;
        } else {
            this.active = false;
        }
    }

    public void updateOutputs(boolean forceUpdate) {
        for(int i = 0; i < 4; ++i) {
            boolean desired = this.wireOutputs[i] && this.active;
            this.setOutput(i, desired, forceUpdate);
        }

    }

    public void openContainer(ServerClient client) {
        ContainerRegistry.openAndSendContainer(client, PacketOpenContainer.LevelObject(Engineer.PROJECTILE_LOGIC_GATE_CONTAINER, this.tileX, this.tileY));
    }
    static {
        TILE_RANGES = new GameTileRange[5];
    }
}