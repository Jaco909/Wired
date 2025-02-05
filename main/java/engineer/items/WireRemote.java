package engineer.items;

import engineer.scripts.ItemPickupTextSpecial;
import necesse.engine.localization.Localization;
import necesse.engine.network.PacketReader;
import necesse.engine.network.packet.PacketLevelEvent;
import necesse.engine.registries.DamageTypeRegistry;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameBlackboard;
import necesse.engine.util.GameRandom;
import necesse.entity.levelEvent.mobAbilityLevelEvent.ToolItemMobAbilityEvent;
import necesse.entity.mobs.AttackAnimMob;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.GameResources;
import necesse.gfx.drawOptions.itemAttack.ItemAttackDrawOptions;
import necesse.gfx.gameFont.FontManager;
import necesse.gfx.gameTooltips.ListGameTooltips;
import necesse.inventory.InventoryItem;
import necesse.inventory.PlayerInventorySlot;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.item.ItemInteractAction;
import necesse.inventory.item.ItemStatTipList;
import necesse.inventory.item.toolItem.ToolItem;
import necesse.level.maps.Level;

import java.awt.*;
import java.util.Iterator;

public class WireRemote extends ToolItem implements ItemInteractAction {

    public WireRemote(int enchantCost) {
        super(0);
        this.setItemCategory(new String[]{"equipment", "tools"});
        this.setItemCategory(ItemCategory.equipmentManager, (String[])null);
        this.keyWords.add("tool");
        this.damageType = DamageTypeRegistry.TRUE;
        this.attackDamage.setBaseValue(1.0F);
        this.attackAnimTime.setBaseValue(300);
        this.attackRange.setBaseValue(1);
        this.attackXOffset=12;
        this.attackYOffset=10;
    }
    public ListGameTooltips getPreEnchantmentTooltips(InventoryItem item, PlayerMob perspective, GameBlackboard blackboard) {
        ListGameTooltips tooltips = super.getPreEnchantmentTooltips(item, perspective, blackboard);
        tooltips.add(Localization.translate("itemtooltip", "remotechanneltip","channel",item.getGndData().getInt("channel",0)));
        tooltips.add(Localization.translate("itemtooltip", "remotetip1"));
        tooltips.add(Localization.translate("itemtooltip", "remotetip2"));
        return tooltips;
    }
    public void setDrawAttackRotation(InventoryItem item, ItemAttackDrawOptions drawOptions, float attackDirX, float attackDirY, float attackProgress) {
        if (this.getAnimInverted(item)) {
            drawOptions.swingRotationInv(20);
        } else {
            drawOptions.swingRotation(20);
        }

    }
    public void showAttack(Level level, int x, int y, AttackAnimMob mob, int attackHeight, InventoryItem item, int seed, PacketReader contentReader) {
        if (level.isClient()) {
            SoundManager.playSound(GameResources.tick, SoundEffect.effect(mob).volume(2f).pitch(GameRandom.globalRandom.getFloatBetween(1f, 1f)));
        }
    }
    public InventoryItem onAttack(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int animAttack, int seed, PacketReader contentReader) {
        if (animAttack == 0) {
            int animTime = this.getAttackAnimTime(item, player);
            int currentChannel = item.getGndData().getInt("channel",0);
            if (currentChannel+1 > 10) {
                currentChannel = 0;
            }
            item.setGndData(item.getGndData().setInt("channel",currentChannel+1));
            ToolItemMobAbilityEvent event = new ToolItemMobAbilityEvent(player, seed, item, x - player.getX(), y - player.getY() + attackHeight, animTime, animTime);
            level.entityManager.addLevelEventHidden(event);
            if (level.isServer() && player.isServerClient()) {
                level.getServer().network.sendToClientsWithEntityExcept(new PacketLevelEvent(event), event, player.getServerClient());
            }
            if (!level.isServer()) {
                level.hudManager.addElement((new ItemPickupTextSpecial(player, item, new Color(200,200,200))));
            }
        }

        return item;
    }
    public void draw(InventoryItem item, PlayerMob perspective, int x, int y, boolean inInventory) {
        super.draw(item, perspective, x, y, inInventory);
        if (inInventory) {
            int ammoAmount = item.getGndData().getInt("channel",0);

            String amountString = String.valueOf(ammoAmount);
            int width = FontManager.bit.getWidthCeil(amountString, tipFontOptions);
            FontManager.bit.drawString((float)(x + 28 - width), (float)(y + 16), amountString, tipFontOptions);
        }
    }
    public boolean isEnchantable(InventoryItem item) {
        return false;
    }
    public String getIsEnchantableError(InventoryItem item) {
        return null;
    }
    public void addStatTooltips(ItemStatTipList list, InventoryItem currentItem, InventoryItem lastItem, Mob perspective, boolean forceAdd) {
    }
    public String getCanBeUpgradedError(InventoryItem item) {
        return Localization.translate("ui", "itemnotupgradable");
    }
    public float getAttackMovementMod(InventoryItem item) {
        return 1;
    }
    public boolean canLevelInteract(Level level, int x, int y, PlayerMob player, InventoryItem item) {
        return true;
    }
    public InventoryItem onLevelInteract(Level level, int x, int y, PlayerMob player, int attackHeight, InventoryItem item, PlayerInventorySlot slot, int seed, PacketReader contentReader) {
        Iterator objects = level.entityManager.objectEntities.iterator();
        while (objects.hasNext()) {
            System.out.println(objects.next());
        }
        return item;
    }
}
