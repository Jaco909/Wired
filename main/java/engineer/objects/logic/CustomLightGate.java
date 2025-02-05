package engineer.objects.logic;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.particle.Particle;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.SharedTextureDrawOptions;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.gameTexture.GameTexture;
import necesse.level.gameLogicGate.GameLogicGate;
import necesse.level.gameLogicGate.entities.LogicGateEntity;
import necesse.level.gameObject.ObjectDamagedTextureArray;
import necesse.level.maps.Level;
import necesse.level.maps.TilePosition;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.function.BiFunction;

public class CustomLightGate extends GameLogicGate {
    public BiFunction<GameLogicGate, TilePosition, LogicGateEntity> provider;

    public CustomLightGate(BiFunction<GameLogicGate, TilePosition, LogicGateEntity> provider) {
        this.provider = provider;
    }

    public LogicGateEntity getNewEntity(Level level, int tileX, int tileY) {
        return (LogicGateEntity)this.provider.apply(this, new TilePosition(level, tileX, tileY));
    }
    public void drawPreview(Level level, int tileX, int tileY, float alpha, PlayerMob player, GameCamera camera) {
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        this.texture.initDraw().alpha(alpha).light(new GameLight(100)).draw(drawX, drawY);
    }
}