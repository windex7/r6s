package plugin.R6S.api;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import plugin.R6S.customitem.Rifle;
import plugin.R6S.customitem.Shotgun;
import plugin.R6S.customitem.Sniper;

public class Gun {

	public static void redirectGun(Player player, ItemStack gun, Object[] args) {
		switch (gun.getItemMeta().getDisplayName()) {
		case "Shotgun":
			Shotgun.shoot(player, gun, args);
		case "Sniper":
			Sniper.shoot(player, gun, args);
		case "Rifle":
			Rifle.shoot(player, gun, args);
		}
	}

	public static Vector getAIM(Player player) {
		Location aimloc = player.getEyeLocation();
		Vector aim = aimloc.getDirection();
		return aim;
	}

	public static void shootBullet(Player player, ItemStack gun, double speed, double damage, double kb, long number, double spread, double recoil, String gunname) {
		Vector aim = getAIM(player);
		player.setVelocity(player.getVelocity().add(aim.clone().multiply(recoil * -1)));
		shootBullet(player, gun, player.getEyeLocation(), aim, speed, damage, kb, number, spread, gunname);
		return;
	}

	public static void shootBullet(Player shooter, ItemStack gun, Location loc, Vector direction, double speed, double damage, double kb, long number, double spread, String gunname) {
		for (int i = 0; i < number; i++) {
			Vector directionclone = direction.clone();
			Vector aim = directionclone.add(getRandomVector().multiply(spread)).normalize().multiply(speed);
			double modifier = 0.3;
			Location locclone = loc.clone();
			Location newloc = locclone.add((direction.clone()).normalize().multiply(modifier));
			// Snowball bullet = (Snowball) loc.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
			Snowball bullet = (Snowball) newloc.getWorld().spawnEntity(loc, EntityType.SNOWBALL);
			bullet.setShooter(shooter);
			bullet.setVelocity(aim);
			Metadata.setMetadata(bullet, "damage", damage);
			Metadata.setMetadata(bullet, "kb", kb);
			Metadata.setMetadata(bullet, "gunname", gunname);
			Metadata.setMetadata(bullet, "gun", gun);
		}
	}

	public static void hitBullet(LivingEntity defender, Snowball bullet) {
		if (Metadata.getMetadata(bullet, "gunname") != null) {
			ItemStack gun = (ItemStack) Metadata.getMetadata(bullet, "gun");
			double damage = (double) Metadata.getMetadata(bullet, "damage");
			defender.damage(damage, (Entity)bullet.getShooter());
			// DamageTick.removeDamageTick(defender);
			double kb = (double) Metadata.getMetadata(bullet, "kb");
			defender.setVelocity(defender.getVelocity().add(bullet.getVelocity().normalize().multiply(kb)));
			if (bullet.getShooter() instanceof Player) {
				Player shooter = (Player) bullet.getShooter();
				Object[] args = {"hiteffect", defender};
				redirectGun(shooter, gun, args);
			}
		} else {
			return;
		}
	}

	public static Vector getRandomVector() {
		Vector random1 = Vector.getRandom();
		Vector random2 = Vector.getRandom();
		Vector random1clone = random1.clone();
		Vector random = random1clone.subtract(random2);
		return random;
	}
}
