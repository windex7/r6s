package plugin.R6S.api;

import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryIO {
	public static void saveInventory(Player player, Inventory inventory, String filename, String key) {
		ItemStack[] items = inventory.getContents();
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

	public static void savePlayerInventory(Player player, String filename, String key) {
		saveInventory(player, player.getInventory(), filename, key);
	}

	public static void savePlayerOpeningInventory(Player player, String filename, String key) {
		saveInventory(player, player.getOpenInventory().getTopInventory(), filename, key);
	}

	public static void backupPlayerInventory(Player player) {
		savePlayerInventory(player, "player", "inventory");
		return;
	}

	public static void loadPlayerInventory(Player player, String filename, String key) {
		String realfilename = Config.getRealFilename(filename, player);
		if (!(Config.getConfig(realfilename, key) != null)) return;
		String itemsstring = Config.getConfig(realfilename, key).toString();
		ItemStack[] invitem = Base64Item.itemFromStringList(itemsstring);
		player.getInventory().setContents(invitem);
		return;
	}

	public static void rollbackPlayerInventory(Player player) {
		String invstring = "inventory";
		if (Objects.equals(Config.getPlayerConfig(player, invstring), null)) {
			player.sendMessage("rollback failed! reason: no inv data found.");
			return;
		} else {
			loadPlayerInventory(player, player.getUniqueId().toString(), invstring);
			return;
		}
	}

	public static Inventory getInventory(String filename, String key, int size, String title) {
		String itemsstring = Config.getConfig(filename, key).toString();
		ItemStack[] invitem = Base64Item.itemFromStringList(itemsstring);
		Inventory inv = Bukkit.createInventory(null, size, title);
		inv.setContents(invitem);
		return inv;
	}

	public static void openInventory(Player player, String filename, String key, int size, String title) {
		Inventory inv = getInventory(filename, key, size, title);
		player.openInventory(inv);
		return;
	}

	public static void openInventory(Player player, Inventory inv) {
		player.openInventory(inv);
		return;
	}
}
