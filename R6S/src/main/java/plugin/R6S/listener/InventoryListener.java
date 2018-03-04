package plugin.R6S.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.Gun;
import plugin.R6S.api.Metadata;
import plugin.R6S.api.NBT;
import plugin.R6S.api.R6SKit;

public class InventoryListener implements Listener {
	List<String> keywords = new ArrayList<String>() {
		{
			add("kit");
			add("grenade1");
			add("grenade2");
			add("grenade3");
			add("grenade4");
		}
	};

	@EventHandler
	public void onInvInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		// String invname = inventory.getName();
		ItemStack cursoritem = event.getCursor();
		ItemStack item = event.getCurrentItem();
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return;
		if (item != null && item.getType() != Material.AIR) {
			if (NBT.readItemTag(item, "bound", "string") != null) {
				// --any item with nbttag which key is "bound" can't be
				// moved whatever the value is.--
				event.setCancelled(true);
				event.setResult(Result.DENY);
				player.updateInventory();
			}
			for (String keyword : keywords) {
				if (NBT.readItemTag(item, keyword, "string") != null) {
					Metadata.setMetadata(player, keyword, NBT.readItemTag(item, keyword, "string"));
					for (ItemStack invitem : inventory.getContents()) {
						if (!(invitem != null)) continue;
						if (invitem.getType() == Material.AIR) continue;
						if (invitem.containsEnchantment(R6SKit.getKitEnch()) && NBT.readItemTag(invitem, keyword, "string") != null) {
							invitem.removeEnchantment(R6SKit.getKitEnch());
						}
					}
					R6SKit.enchKitItem(item);
				}
			}
			//if (NBT.readItemTag(item, "kit", "string") != null) {
			//	Metadata.setMetadata(player, "kit", NBT.readItemTag(item, "kit", "string"));
			//	for (ItemStack invitem : inventory.getContents()) {
			//		if (!(invitem != null)) continue;
			//		if (invitem.getType() == Material.AIR) continue;
			//		if (invitem.containsEnchantment(R6SKit.getKitEnch())) {
			//			invitem.removeEnchantment(R6SKit.getKitEnch());
			//		}
			//	}
			//	R6SKit.enchKitItem(item);
			//}
		}
		if (cursoritem != null && cursoritem.getType() != Material.AIR) {
			if (NBT.readItemTag(cursoritem, "bound", "string") != null) {
				// --any item with nbttag which key is "bound" can't be
				// moved whatever the value is.--
				event.setCancelled(true);
				event.setResult(Result.DENY);
				player.updateInventory();
			}
		}
	}

	@EventHandler
	public void onDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItemDrop().getItemStack();
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return;
		if (item != null && item.getType() != Material.AIR) {
			if (NBT.readItemTag(item, "gun", "string") != null) {
				if (Gun.redirectGun(player, item, null)) {
					event.getItemDrop().remove();
					Gun.redirectGun(player, item, new Object[] { "reload" });
				}
			}
		}
	}

}
