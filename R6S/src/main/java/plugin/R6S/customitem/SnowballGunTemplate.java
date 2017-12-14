package plugin.R6S.customitem;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.Damage;
import plugin.R6S.api.Gun;
import plugin.R6S.api.Message;
import plugin.R6S.api.NBT;
import plugin.R6S.api.PlaySound;
import plugin.R6S.api.Timing;

public class SnowballGunTemplate {
	int cooltime; // ms
	double speed;
	double damage;
	double kb;
	long number;
	double spread;
	double recoil;
	String firesound = "BLOCK_NOTE_BASS";
	float volume = 12;
	float pitch = 1;
	String soundmode = "world";

	public void gun(Player shooter, ItemStack gun, Object[] args) {
		switch (args[0].toString()) {
		case "trigger":
			String key = "ct";
			Object lastshot = NBT.readItemTag(gun, key, "long");
			if (lastshot != null) {
				long leftct = cooltime - Timing.getTimeDiff((long) lastshot);
				if (leftct > 0) {
					Message.sendMessage(shooter, "CT left: " + leftct);
					break;
				}
			}
			shooter.getInventory().setItemInMainHand(NBT.writeItemTag(gun, key, Timing.getTime(), "long"));
			Gun.shootBullet(shooter, gun, speed, damage, kb, number, spread, recoil, gun.getItemMeta().getDisplayName());
			playSound(shooter, shooter.getLocation(), "world");
			break;
		case "interact":
			interact(shooter, gun, (LivingEntity) args[1]);
			playSound(shooter, shooter.getLocation(), "world");
			break;
		case "hiteffect":
			hiteffect(shooter, gun, args[1]);
			break;
		default:
			return;
		}
	}

	public void interact(Player shooter, ItemStack gun, LivingEntity target) {
		String key = "ct";
		Object lastshot = NBT.readItemTag(gun, key, "long");
		if (lastshot != null) {
			long leftct = cooltime - Timing.getTimeDiff((long) lastshot);
			if (leftct > 0) {
				Message.sendMessage(shooter, "CT left: " + leftct);
				return;
			}
		}
		shooter.getInventory().setItemInMainHand(NBT.writeItemTag(gun, key, Timing.getTime(), "long"));
		Damage.entityDamage(shooter, damage * number, target, false);
		// target.damage(damage * number, player);
		return;
	}

	public void playSound(Player player, Location loc, String mode) {
		PlaySound.play(player, loc, firesound, volume, pitch, mode);
	}

	public void hiteffect(Player player, ItemStack gun, Object arg) {

	}
}
