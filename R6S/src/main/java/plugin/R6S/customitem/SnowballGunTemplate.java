package plugin.R6S.customitem;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.Gun;
import plugin.R6S.api.Message;
import plugin.R6S.api.NBT;
import plugin.R6S.api.Timing;

public class SnowballGunTemplate {
	static int cooltime; // ms
	static double speed;
	static double damage;
	static double kb;
	static long number;
	static double spread;
	static double recoil;

	public static void shoot(Player player, ItemStack gun, Object[] args) {
		switch (args[0].toString()) {
		case "trigger":
			String key = "ct";
			Object lastshot = NBT.readItemTag(gun, key, "long");
			if (lastshot != null) {
				long leftct = cooltime - Timing.getTimeDiff((long) lastshot);
				if (leftct > 0) {
					Message.sendMessage(player, "CT left: " + leftct);
					break;
				}
			}
			player.getInventory().setItemInMainHand(NBT.writeItemTag(gun, key, Timing.getTime(), "long"));
			Gun.shootBullet(player, gun, speed, damage, kb, number, spread, recoil, gun.getItemMeta().getDisplayName());
			break;
		case "interact":
			interact(player, gun, (LivingEntity) args[1]);
			break;
		case "hiteffect":
			hiteffect(player, gun, args);
		default:
			return;
		}
	}

	public static void interact(Player player, ItemStack gun, LivingEntity target) {
		String key = "ct";
		Object lastshot = NBT.readItemTag(gun, key, "long");
		if (lastshot != null) {
			long leftct = cooltime - Timing.getTimeDiff((long) lastshot);
			if (leftct > 0) {
				Message.sendMessage(player, "CT left: " + leftct);
				return;
			}
		}
		player.getInventory().setItemInMainHand(NBT.writeItemTag(gun, key, Timing.getTime(), "long"));
		target.damage(damage * number, player);
		return;
	}

	public static void hiteffect(Player player, ItemStack gun, Object[] args) {

	}
}
