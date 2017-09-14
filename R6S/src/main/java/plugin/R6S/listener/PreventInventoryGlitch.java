package plugin.R6S.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.NBT;

public class PreventInventoryGlitch implements Listener {
	@EventHandler
	public void onInvInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		// String invname = inventory.getName();
		ItemStack cursoritem = event.getCursor();
		ItemStack item = event.getCurrentItem();
		if (item != null && item.getType() != Material.AIR) {
			if (NBT.readItemTag(item, "string", "unavailable") != null) {
				// --any item with nbttag which key is "unavailable" can't be
				// moved whatever the value is.--
				event.setCancelled(true);
				event.setResult(Result.DENY);
				player.updateInventory();
			}
		}
		if (cursoritem != null && cursoritem.getType() != Material.AIR) {
			if (NBT.readItemTag(cursoritem, "string", "unavailable") != null) {
				// --any item with nbttag which key is "unavailable" can't be
				// moved whatever the value is.--
				event.setCancelled(true);
				event.setResult(Result.DENY);
				player.updateInventory();
			}
		}
	}

}
