package plugin.R6S.api;

import java.util.Arrays;
import java.util.Objects;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;

public class R6SKit {
	static String[] kits = {
		"default",
		"sniper",
		"shotgun"
	};

	static Enchantment kititemench = Enchantment.DURABILITY;

	public static Enchantment getKitEnch() {
		return kititemench;
	}

	public static void enchKitItem(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		item.addEnchantment(getKitEnch(), 1);
	}

	static Plugin r6s = R6SPlugin.getInstance();

	public static void selectKit(Player player, ItemStack item) {
		if (!(Objects.equals(NBT.readItemTag(item, "openinv", "string"), null))) {
			String value = NBT.readItemTag(item, "openinv", "string").toString();
			switch (value) {
			case "kitselect":
				// InventoryIO.openInventory(player, "config", "inv.kitselect", 3 * 9, "Select Kit");
				Inventory kitinv = InventoryIO.getInventory("config", "inv.kitselect", 3 * 9, "Select Kit");
				String selectedkit;
				if (Objects.equals(Metadata.getMetadata(player, "kit"), null)) {
					selectedkit = "default";
				} else {
					selectedkit = Metadata.getMetadata(player, "kit").toString();
				}
				//ItemStack[] kitinvcontents = kitinv.getContents();
				//for (ItemStack content : kitinvcontents) {
				//	if (!(content != null)) continue;
				//	if (content.getType() == Material.AIR) continue;
				//	String contentskit = NBT.readItemTag(content, "kit", "string").toString();
				//	if (Objects.equals(contentskit, selectedkit)) {
				//		if (!(content.containsEnchantment(getKitEnch()))) {
				//			enchKitItem(content);
				//		}
				//	} else if (content.containsEnchantment(getKitEnch())) {
				//		content.removeEnchantment(getKitEnch());
				//	}
				//}
				ItemStack[] kitinvcontents = enchKitInvContents(kitinv.getContents(), selectedkit, "kit", "string");
				kitinv.setContents(kitinvcontents);
				InventoryIO.openInventory(player, kitinv);
				break;
			case "grenadeselect":
				int grenumber = 4;
				Inventory greinv = InventoryIO.getInventory("config", "inv.grenadeselect", 6 * 9, "Select Grenade");
				String[] selectedgre = new String[grenumber];
				String keys[] = new String[grenumber];
				for (int i = 1; i <= grenumber; i++) {
					keys[i-1] = "grenade" + i;
					if (Objects.equals(Metadata.getMetadata(player, keys[i-1]), null)) {
						selectedgre[i] = "default";
					} else {
						selectedgre[i] = Metadata.getMetadata(player, keys[i-1]).toString();
					}
				}
				ItemStack[] greinvcontents = enchKitInvContents(greinv.getContents(), selectedgre, keys, "string");
				greinv.setContents(greinvcontents);
				InventoryIO.openInventory(player, greinv);
				// InventoryIO.openInventory(player, "config", "inv.greanadeselect", 6 * 9, "Select Grenade");
				break;
			default:
				break;
			}
			return;
		}
	}

	public static ItemStack[] enchKitInvContents(ItemStack[] contents, String selectednbt, String key, String datatype) {
		ItemStack[] itemstacks = contents;
		for (ItemStack content : itemstacks) {
			if (!(content != null)) continue;
			if (content.getType() == Material.AIR) continue;
			if (Objects.equals(NBT.readItemTag(content, key, datatype), null)) continue;
			String contentnbt = NBT.readItemTag(content, key, datatype).toString();
			if (Objects.equals(contentnbt, selectednbt)) {
				if (!(content.containsEnchantment(getKitEnch()))) {
					enchKitItem(content);
				}
			} else if (content.containsEnchantment(getKitEnch())) {
				content.removeEnchantment(getKitEnch());
			}
		}
		return itemstacks;
	}

	public static ItemStack[] enchKitInvContents(ItemStack[] contents, String[] selectednbt, String[] key, String datatype) {
		int num = selectednbt.length;
		ItemStack[] returncontents = contents;
		for (int i = 0; i < num; i++) {
			returncontents = enchKitInvContents(returncontents, selectednbt[i], key[i], datatype);
		}
		return returncontents;
	}

	public static String getKit(Player player) {
		if (Objects.equals(Metadata.getMetadata(player, "kit"), null)) {
			return "default";
		} else if (Arrays.asList(kits).contains(Metadata.getMetadata(player, "kit").toString())) {
			return Metadata.getMetadata(player, "kit").toString();
		} else {
			return "default";
		}
	}

	public static ItemStack[] getKitContents(String kit, String team) {
		if (Objects.equals(team, "red") || Objects.equals(team, "blue") || Objects.equals(team, "white")) {
			if (Arrays.asList(kits).contains(kit)) {
				ItemStack[] kitcontents = Base64Item.itemFromStringList(Config.getGameConfigData("kit." + kit + "." + team).toString());
				return kitcontents;
			} else {
				ItemStack[] defaultkitcontents = Base64Item.itemFromStringList(Config.getGameConfigData("kit.default." + team).toString());
				r6s.getLogger().info("invalid kit name! : " + kit);
				return defaultkitcontents;
			}
		}
		r6s.getLogger().info("invalid team name! : " + team);
		return Base64Item.itemFromStringList(Config.getGameConfigData("kit.default.red").toString());
	}
}
