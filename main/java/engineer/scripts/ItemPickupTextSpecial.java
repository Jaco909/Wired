package engineer.scripts;

import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.gfx.gameFont.FontOptions;
import necesse.inventory.InventoryItem;
import necesse.level.maps.hudManager.HudManager;
import necesse.level.maps.hudManager.floatText.ItemPickupText;

import java.awt.*;

public class ItemPickupTextSpecial extends ItemPickupText {
    private InventoryItem iteam;
    public ItemPickupTextSpecial(int x, int y, InventoryItem item) {
        super(x, y, item);
    }
    public ItemPickupTextSpecial(Mob mob, InventoryItem itemz, Color avg) {
        this(mob.getX(), mob.getY() - 16, itemz);
        this.fontOptions = (new FontOptions(22)).outline().color(avg);
        this.iteam = itemz;
        this.iteam.setAmount(0);
        this.updateText(itemz);
    }
    public void updateText(InventoryItem itemz) {
        this.setText(Integer.toString(itemz.getGndData().getInt("channel",0)));
    }
    public void init(HudManager manager) {
        super.init(manager);
        manager.removeElements((element) -> {
            if (element.isRemoved()) {
                return false;
            } else {
                if (element != this && element instanceof ItemPickupText) {
                    ItemPickupText other = (ItemPickupText)element;
                    if (other.getItemID() == this.getItemID()) {
                        return true;
                    }
                }

                return false;
            }
        });
    }
    public void updateText() {
        return;
    }
}