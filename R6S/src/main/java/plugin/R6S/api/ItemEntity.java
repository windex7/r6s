package plugin.R6S.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemEntity {
	public static void dropItem(Location loc, ItemStack item) {
		Item entityitem = loc.getWorld().dropItem(loc, item);
		return;
	}

	public static void dropItem(Player player, ItemStack item) {
		Inventory inv = player.getInventory();
		// inv.remove(item);
		for (int index = 9; index < 45; index++) {
			ItemStack content = inv.getItem(index);
			if (content != null) {
				if (content.getType() == Material.AIR) {
					inv.setItem(index, item);
					player.updateInventory();
					return;
				}
			} else {
				inv.setItem(index, item);
				player.updateInventory();
				return;
			}
		}
		dropItem(player.getEyeLocation(), item);
		player.sendMessage("dropped " + item.getType().toString());
		return;
	}
}
