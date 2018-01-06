package plugin.R6S.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerInv {
	public static void saveInventory(Player player, String filename, String key) {
		ItemStack[] items = player.getInventory().getContents();
		String itemsstring = Base64Item.itemToStringList(items);
		switch (filename) {
		case "player":
			Config.setPlayerConfig(player, key, itemsstring);
			break;
		case "config":
			Config.setGameConfig(key, itemsstring);
			break;
		case "dev":
		case "devfile":
			Config.setDevConfig(key, itemsstring);
			break;
		default:
			Config.setConfig(filename, key, itemsstring);
			break;
		}
		return;
	}

	public static void loadInventory(Player player, String filename, String key) {
		if (!(Config.getConfig(filename, key) != null)) return;
		String itemsstring = Config.getConfig(filename, key).toString();
		ItemStack[] invitem = Base64Item.itemFromStringList(itemsstring);
		player.getInventory().setContents(invitem);
		return;
	}
}
