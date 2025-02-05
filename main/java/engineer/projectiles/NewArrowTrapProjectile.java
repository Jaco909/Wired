package engineer.projectiles;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.LocalMessage;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.entity.mobs.GameDamage;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.projectile.Projectile;
import necesse.entity.trails.Trail;
import necesse.gfx.GameResources;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.EntityDrawable;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class NewArrowTrapProjectile extends Projectile {
    public NewArrowTrapProjectile() {
    }

    public NewArrowTrapProjectile(float x, float y, float targetX, float targetY, GameDamage damage, Mob owner) {
        this.x = x;
        this.y = y;
        this.setTarget(targetX, targetY);
        this.speed = 200.0F;
        this.setDamage(damage);
        this.setOwner(owner);
        this.setDistance(400);
    }
    public void init() {
        super.init();
    }

    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, OrderableDrawables topList, OrderableDrawables overlayList, Level level, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        if (!this.removed()) {
            GameLight light = level.getLightLevel(this);
            int drawX = camera.getDrawX(this.x) - this.texture.getWidth() / 2;
            int drawY = camera.getDrawY(this.y);
            final TextureDrawOptions options = this.texture.initDraw().light(light).rotate(this.getAngle(), this.texture.getWidth() / 2, 0).pos(drawX, drawY - (int)this.getHeight());
            list.add(new EntityDrawable(this) {
                public void draw(TickManager tickManager) {
                    options.draw();
                }
            });
            this.addShadowDrawables(tileList, drawX, drawY, light, this.getAngle(), 0);
        }
    }

    public GameMessage getAttackerName() {
        Mob owner = this.getOwner();
        return (GameMessage)(owner != null ? owner.getAttackerName() : new LocalMessage("deaths", "arrowtrapname"));
    }

    public void applyDamage(Mob mob, float x, float y) {
        if (this.getLevel().isTrialRoom) {
            GameDamage trialDamage = new GameDamage(DamageTypeRegistry.TRUE, (float)mob.getMaxHealth() / 4.0F);
            mob.isServerHit(trialDamage, mob.x - x * -this.dx * 50.0F, mob.y - y * -this.dy * 50.0F, (float)this.knockback, this);
        } else {
            super.applyDamage(mob, x, y);
        }

    }

    protected void playHitSound(float x, float y) {
        SoundManager.playSound(GameResources.bowhit, SoundEffect.effect(x, y));
    }

    public boolean isTrapAttacker() {
        return true;
    }
}
