package engineer.objects;

import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.*;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class FakeWallObject extends GameObject {
    public ObjectDamagedTextureArray texture;

    public FakeWallObject() {
        this.setItemCategory(new String[]{"wiring"});
        this.setCraftingCategory(new String[]{"wiring"});
        this.mapColor = new Color(188, 188, 188);
        this.displayMapTooltip = false;
        this.showsWire = true;
        this.objectHealth = 1;
        this.toolType = ToolType.UNBREAKABLE;
        this.isLightTransparent = true;
        this.isSolid = false;
    }

    public void loadTextures() {
        super.loadTextures();
        this.texture = ObjectDamagedTextureArray.loadAndApplyOverlay(this, "objects/fakewall");
    }
    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        int sprite = 0;

        /*ObjectEntity objectEntity = level.entityManager.getObjectEntity(tileX, tileY);
        if (objectEntity instanceof LEDObjectEntity) {
            LEDObjectEntity ledEnt = (LEDObjectEntity) objectEntity;
            GameTexture texture = this.texture.getDamagedTexture(this, level, tileX, tileY);
            final TextureDrawOptions options = texture.initDraw().sprite(sprite, 0, 32, texture.getHeight()).light(light).pos(drawX, drawY - (texture.getHeight() - 32)).colorMult(new Color(ledEnt.red,ledEnt.green,ledEnt.blue,255));
            tileList.add(new LevelSortedDrawable(this, tileX, tileY) {
                public int getSortY() {
                    return 16;
                }

                public void draw(TickManager tickManager) {
                    options.draw();
                }
            });
        }*/
    }

    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha, PlayerMob player, GameCamera camera) {
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        GameTexture texture = this.texture.getDamagedTexture(0.0F);
        texture.initDraw().sprite(0, 0, 32).alpha(alpha).draw(drawX, drawY - 32);
        texture.initDraw().sprite(0, 1, 32).alpha(alpha).draw(drawX, drawY);
    }

    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return false;
    }
    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new FakeWallEntity(level, x, y);
    }
}