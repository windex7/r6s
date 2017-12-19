package plugin.R6S.api;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;

import plugin.R6S.listener.ProjectileEpicGlass;

public class EpicGlass {
	static Map<Location, Integer> damagedblock = new HashMap<>();
	static int blockdurability = ProjectileEpicGlass.getBlockDurability();

	public static void addDamage(Location loc, int damage) {
		if (damagedblock.containsKey(loc)) {
			int finaldamage = damagedblock.get(loc) + damage;
			if (finaldamage >= blockdurability) {
				removeBlock(loc);
				return;
			} else {
				damagedblock.put(loc, finaldamage);
				return;
			}
		} else {
			damagedblock.put(loc, damage);
			return;
		}
	}

	public static Integer getDamage(Location loc) {
		if (damagedblock.containsKey(loc)) {
			return damagedblock.get(loc);
		} else {
			return 0;
		}
	}

	public static void clearDamage(Location loc) {
		if (damagedblock.containsKey(loc)) {
			damagedblock.remove(loc);
		}
	}

	public static void clearAllDamage() {
		damagedblock.clear();
	}

	public static void removeBlock(Location loc) {
		loc.getBlock().setType(Material.AIR);
		return;
	}
}
