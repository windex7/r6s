package plugin.R6S.customitem;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.Gun;
import plugin.R6S.api.Message;
import plugin.R6S.api.NBT;
import plugin.R6S.api.Timing;

public class Shotgun {

	static int cooltime = 800; // ms
	static double speed = 3;
	static double damage = 4;
	static double kb = 0.1;
	static long number = 9;
	static double spread = 0.3;
	static double recoil = 0.4;

	public static void shoot(Player player, ItemStack gun, String action) {
		switch (action) {
		case "trigger":
			// String key = "shotgun";
			// Object lastshot = Metadata.getMetadata(player, "shotgun");
			String key = "ct";
			Object lastshot = NBT.readItemTag(gun, key, "long");
			if (lastshot != null) {
				long leftct = cooltime - Timing.getTimeDiff((long)lastshot);
				if (leftct > 0) {
					Message.sendMessage(player, "CT left: " + leftct);
					break;
				}
			}
			player.getInventory().setItemInMainHand(NBT.writeItemTag(gun, key, Timing.getTime(), "long"));
			Gun.shootBullet(player, speed, damage, kb, number, spread, recoil, "Shotgun");
			break;
		default:
			return;
		}
	}

	public static void interact(Player player, LivingEntity target, ItemStack gun) {
		String key = "ct";
		Object lastshot = NBT.readItemTag(gun, key, "long");
		if (lastshot != null) {
			long leftct = cooltime - Timing.getTimeDiff((long)lastshot);
			if (leftct > 0) {
				Message.sendMessage(player, "CT left: " + leftct);
				return;
			}
		}
		player.getInventory().setItemInMainHand(NBT.writeItemTag(gun, key, Timing.getTime(), "long"));
		target.damage(damage * number, player);
		return;
	}
}
