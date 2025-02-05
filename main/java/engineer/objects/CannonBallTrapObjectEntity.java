package engineer.objects;

import engineer.projectiles.CannonballTrapProjectile;

import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.objectEntity.TrapObjectEntity;
import necesse.entity.projectile.CannonBallProjectile;
import necesse.entity.projectile.laserProjectile.VoidTrapProjectile;
import necesse.level.maps.Level;

import java.awt.*;

public class CannonBallTrapObjectEntity extends TrapObjectEntity {
    public static GameDamage damage = new GameDamage(50.0F, 20.0F, 0.0F, 2.0F, 1.0F);

    public CannonBallTrapObjectEntity(Level level, int x, int y) {
        super(level, x, y, 1000L);
        this.shouldSave = false;
    }

    public void triggerTrap(int wireID, int dir) {
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
                if (this.getServer().getPlayersOnline() > 0) {
                    this.getLevel().entityManager.projectiles.add(new engineer.projectiles.CannonballTrapProjectile(var10003, var10004, var10005, (float)(yPos + targetDir.y), 200, 150, damage, 0, null));
                }
                this.startCooldown();
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