package engineer.objects;

import engineer.Engineer;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.localization.Localization;
import necesse.engine.network.packet.PacketOpenContainer;
import necesse.engine.registries.ContainerRegistry;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.objectEntity.ObjectEntity;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptions;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.toolItem.ToolType;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.ObjectDamagedTextureArray;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.util.List;

public class LEDPanelObject extends GameObject {
    public ObjectDamagedTextureArray texture;

    public LEDPanelObject() {
        this.setItemCategory(new String[]{"wiring"});
        this.setCraftingCategory(new String[]{"wiring"});
        this.mapColor = new Color(255, 255, 255);
        this.displayMapTooltip = true;
        this.showsWire = true;
        this.objectHealth = 1;
        this.toolType = ToolType.ALL;
        this.isLightTransparent = true;
        this.roomProperties.add("lights");
    }
    public GameLight getLight(Level level, int layerID, int tileX, int tileY) {
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(tileX, tileY);
        if (objectEntity instanceof LEDObjectEntity) {
            LEDObjectEntity ledEnt = (LEDObjectEntity) objectEntity;
            if (ledEnt.wireInputs[3] && level.wireManager.isWireActive(tileX,tileY,3)) {
                return level.lightManager.newLight(new Color(ledEnt.color4.getRed(), ledEnt.color4.getGreen(), ledEnt.color4.getBlue()), 1, (float) this.getLightLevel(level, layerID, tileX, tileY));
            }
            if (ledEnt.wireInputs[2] && level.wireManager.isWireActive(tileX,tileY,2)) {
                return level.lightManager.newLight(new Color(ledEnt.color3.getRed(), ledEnt.color3.getGreen(), ledEnt.color3.getBlue()), 1, (float) this.getLightLevel(level, layerID, tileX, tileY));
            }
            if (ledEnt.wireInputs[1] && level.wireManager.isWireActive(tileX,tileY,1)) {
                return level.lightManager.newLight(new Color(ledEnt.color2.getRed(), ledEnt.color2.getGreen(), ledEnt.color2.getBlue()), 1, (float) this.getLightLevel(level, layerID, tileX, tileY));
            }
            if (ledEnt.wireInputs[0] && level.wireManager.isWireActive(tileX,tileY,0)) {
                return level.lightManager.newLight(new Color(ledEnt.color1.getRed(), ledEnt.color1.getGreen(), ledEnt.color1.getBlue()), 1, (float) this.getLightLevel(level, layerID, tileX, tileY));
            } else {
                return level.lightManager.newLight(new Color(255, 255, 255), 1, (float) this.getLightLevel(level, layerID, tileX, tileY));
            }
        } else {
            return level.lightManager.newLight(new Color(255, 255, 255), 1, (float) this.getLightLevel(level, layerID, tileX, tileY));
        }
    }

    public boolean canInteract(Level level, int x, int y, PlayerMob player) {
        return true;
    }
    public void interact(Level level, int x, int y, PlayerMob player) {
        if (level.isServer()) {
            PacketOpenContainer p = PacketOpenContainer.ObjectEntity(Engineer.LED_CONTAINER, level.entityManager.getObjectEntity(x, y));
            ContainerRegistry.openAndSendContainer(player.getServerClient(), p);
        }
    }

    public void tick(Level level, int x, int y) {
        level.lightManager.updateStaticLight(x, y);
    }
    public void refreshLight(Level level, int x, int y, Color var1){
        level.lightManager.updateStaticLight(x, y);
    }

    public ObjectEntity getNewObjectEntity(Level level, int x, int y) {
        return new LEDObjectEntity(level, x, y);
    }

    public void loadTextures() {
        super.loadTextures();
        this.texture = ObjectDamagedTextureArray.loadAndApplyOverlay(this, "objects/ledpanel");
    }

    public void addDrawables(List<LevelSortedDrawable> list, OrderableDrawables tileList, Level level, int tileX, int tileY, TickManager tickManager, GameCamera camera, PlayerMob perspective) {
        GameLight light = level.getLightLevel(tileX, tileY);
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        int sprite = 0;
        if (this.isLit(level, tileX, tileY)) {
            sprite = 1;
            light = new GameLight(150.0F);
        }

        ObjectEntity objectEntity = level.entityManager.getObjectEntity(tileX, tileY);
        if (objectEntity instanceof LEDObjectEntity) {
            LEDObjectEntity ledEnt = (LEDObjectEntity) objectEntity;
            GameTexture texture = this.texture.getDamagedTexture(this, level, tileX, tileY);
            if (ledEnt.wireInputs[3] && level.wireManager.isWireActive(tileX,tileY,3)) {
                final TextureDrawOptions options = texture.initDraw().sprite(sprite, 0, 32, texture.getHeight()).light(light).pos(drawX, drawY - (texture.getHeight() - 32)).colorMult(new Color(ledEnt.color4.getRed(),ledEnt.color4.getGreen(),ledEnt.color4.getBlue(),255));
                tileList.add(new LevelSortedDrawable(this, tileX, tileY) {
                    public int getSortY() {
                        return 16;
                    }

                    public void draw(TickManager tickManager) {
                        options.draw();
                    }
                });
            } else if (ledEnt.wireInputs[2] && level.wireManager.isWireActive(tileX,tileY,2)) {
                final TextureDrawOptions options = texture.initDraw().sprite(sprite, 0, 32, texture.getHeight()).light(light).pos(drawX, drawY - (texture.getHeight() - 32)).colorMult(new Color(ledEnt.color3.getRed(),ledEnt.color3.getGreen(),ledEnt.color3.getBlue(),255));
                tileList.add(new LevelSortedDrawable(this, tileX, tileY) {
                    public int getSortY() {
                        return 16;
                    }

                    public void draw(TickManager tickManager) {
                        options.draw();
                    }
                });
            } else if (ledEnt.wireInputs[1] && level.wireManager.isWireActive(tileX,tileY,1)) {
                final TextureDrawOptions options = texture.initDraw().sprite(sprite, 0, 32, texture.getHeight()).light(light).pos(drawX, drawY - (texture.getHeight() - 32)).colorMult(new Color(ledEnt.color2.getRed(),ledEnt.color2.getGreen(),ledEnt.color2.getBlue(),255));
                tileList.add(new LevelSortedDrawable(this, tileX, tileY) {
                    public int getSortY() {
                        return 16;
                    }

                    public void draw(TickManager tickManager) {
                        options.draw();
                    }
                });
            } else if (ledEnt.wireInputs[0] && level.wireManager.isWireActive(tileX,tileY,0)) {
                final TextureDrawOptions options = texture.initDraw().sprite(sprite, 0, 32, texture.getHeight()).light(light).pos(drawX, drawY - (texture.getHeight() - 32)).colorMult(new Color(ledEnt.color1.getRed(),ledEnt.color1.getGreen(),ledEnt.color1.getBlue(),255));
                tileList.add(new LevelSortedDrawable(this, tileX, tileY) {
                    public int getSortY() {
                        return 16;
                    }

                    public void draw(TickManager tickManager) {
                        options.draw();
                    }
                });
            } else {
                final TextureDrawOptions options = texture.initDraw().sprite(sprite, 0, 32, texture.getHeight()).light(light).pos(drawX, drawY - (texture.getHeight() - 32)).colorMult(new Color(200,200,200,255));
                tileList.add(new LevelSortedDrawable(this, tileX, tileY) {
                    public int getSortY() {
                        return 16;
                    }

                    public void draw(TickManager tickManager) {
                        options.draw();
                    }
                });
            }
        }
    }

    public void drawPreview(Level level, int tileX, int tileY, int rotation, float alpha, PlayerMob player, GameCamera camera) {
        int drawX = camera.getTileDrawX(tileX);
        int drawY = camera.getTileDrawY(tileY);
        GameTexture texture = this.texture.getDamagedTexture(0.0F);
        if (this.isLit(level, tileX, tileY)) {
            texture.initDraw().sprite(1, 0, 32).alpha(alpha).draw(drawX, drawY - 32);
            texture.initDraw().sprite(1, 1, 32).alpha(alpha).draw(drawX, drawY);
        } else {
            texture.initDraw().sprite(0, 0, 32).alpha(alpha).draw(drawX, drawY - 32);
            texture.initDraw().sprite(0, 1, 32).alpha(alpha).draw(drawX, drawY);
        }
    }

    public int getLightLevel(Level level, int layerID, int tileX, int tileY) {
        return this.isLit(level, tileX, tileY) ? 75 : 0;
    }

    public void onWireUpdate(Level level, int layerID, int tileX, int tileY, int wireID, boolean active) {
        level.lightManager.updateStaticLight(tileX, tileY);
    }

    public boolean isLit(Level level, int x, int y) {
        ObjectEntity objectEntity = level.entityManager.getObjectEntity(x, y);
        if (objectEntity instanceof LEDObjectEntity) {
            LEDObjectEntity ledEnt = (LEDObjectEntity) objectEntity;
            if (ledEnt.wireInputs[3] && level.wireManager.isWireActive(x,y,3)) {
                return true;
            } else if (ledEnt.wireInputs[2] && level.wireManager.isWireActive(x,y,2)) {
                return true;
            } else if (ledEnt.wireInputs[1] && level.wireManager.isWireActive(x,y,1)) {
                return true;
            } else if (ledEnt.wireInputs[0] && level.wireManager.isWireActive(x,y,0)) {
                return true;
            } else {
            return false;
            }
        } else {
            return false;
        }
    }

    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = super.getItemTooltips(item, perspective);
        tooltips.add(Localization.translate("itemtooltip", "activatedwiretip"));
        tooltips.add(Localization.translate("itemtooltip", "openLED"));
        return tooltips;
    }
}