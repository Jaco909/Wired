package engineer.objects.vanilla;

import necesse.engine.localization.Localization;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.MergeFunction;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.MaskedPressurePlateObject;
import necesse.level.maps.Level;

import java.awt.*;
import java.io.FileNotFoundException;

public class TileMaskedPressurePlateObject extends MaskedPressurePlateObject {
    protected String maskTextureName;
    protected String tileTextureName;
    public GameTexture texture;
    public TileMaskedPressurePlateObject(String maskTextureName, String tileTextureName, Color mapColor){
        super(maskTextureName,tileTextureName,mapColor);
    }
    public ListGameTooltips getItemTooltips(InventoryItem item, PlayerMob perspective) {
        ListGameTooltips tooltips = new ListGameTooltips();
        //tooltips.add(Localization.translate("itemtooltip", "activewiretip"));
        tooltips.add(Localization.translate("itemtooltip", "placetippath"));
        return tooltips;
    }
    public GameTexture generateItemTexture() {
        super.loadTextures();
        this.texture = new GameTexture(GameTexture.fromFile("objects/" + this.getStringID(), true));
        Point tileSprite = new Point(0, 0);

        GameTexture tileTexture;
        try {
            tileTexture = GameTexture.fromFileRaw("tiles/" + this.tileTextureName + "_splat", true);
            tileSprite = new Point(3, 0);
        } catch (FileNotFoundException var4) {
            tileTexture = GameTexture.fromFile("tiles/" + this.tileTextureName, true);
        }

        MergeFunction mergeFunction = (currentColor, mergeColor) -> currentColor.equals(Color.WHITE) ? mergeColor : currentColor;
        this.texture.merge(tileTexture, 0, 0, tileSprite.x * 32, tileSprite.y * 32 + 2, 32, 30, mergeFunction);
        this.texture.merge(tileTexture, 32, 0, tileSprite.x * 32, tileSprite.y * 32, 32, 32, mergeFunction);
        return new GameTexture(this.texture, 0, 0, 32);
    }
    public boolean canPlaceOn(Level level, int layerID, int x, int y, GameObject other) {
        if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("stonepathtile") && this.idData.getStringID().equalsIgnoreCase("stonepathpressureplate")) {
           return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("sandstonepathtile") && this.idData.getStringID().equalsIgnoreCase("sandstonepathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("woodpathtile") && this.idData.getStringID().equalsIgnoreCase("woodpathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("spidercastlecarpet") && this.idData.getStringID().equalsIgnoreCase("spidercastlecarpetpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("dawnpath") && this.idData.getStringID().equalsIgnoreCase("dawnpathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("lavapath") && this.idData.getStringID().equalsIgnoreCase("lavapathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("moonpath") && this.idData.getStringID().equalsIgnoreCase("moonpathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("swampstonepath") && this.idData.getStringID().equalsIgnoreCase("swampstonepathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("cryptpath") && this.idData.getStringID().equalsIgnoreCase("cryptpathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("cryptpath") && this.idData.getStringID().equalsIgnoreCase("cryptpathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("darkmoonpath") && this.idData.getStringID().equalsIgnoreCase("darkmoonpathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("darkfullmoonpath") && this.idData.getStringID().equalsIgnoreCase("darkfullmoonpathpressureplate")) {
            return true;
        } else if (level.getTile(x,y).idData.getStringID().equalsIgnoreCase("swampstonepathtile") && this.idData.getStringID().equalsIgnoreCase("swampstonepathpressureplate")) {
            return true;
        } else {
            return false;
        }
    }
}
