package plugin.R6S.api;

import org.bukkit.Location;
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

	public static boolean redirectGun(Player player, ItemStack gun, Object[] args) {
		switch (gun.getItemMeta().getDisplayName()) {
		case "Shotgun":
			Shotgun shotgun = new Shotgun();
			shotgun.gun(player, gun, args);
			return true;
		case "Sniper":
			Sniper sniper = new Sniper();
			sniper.gun(player, gun, args);
			return true;
		case "Rifle":
			Rifle rifle = new Rifle();
			rifle.gun(player, gun, args);
			return true;
		default:
			return false;
		}
	}

	public static Vector getAIM(Player player) {
		Location aimloc = player.getEyeLocation();
		Vector aim = aimloc.getDirection();
		return aim;
	}

	public static void shootBullet(Player player, ItemStack gun, double speed, double damage, boolean isdamagetruevalue, double kb, long number, double spread, double recoil, String gunname) {
		Vector aim = getAIM(player);
		player.setVelocity(player.getVelocity().add(aim.clone().multiply(recoil * -1)));
		shootBullet(player, gun, player.getEyeLocation(), aim, speed, damage, isdamagetruevalue, kb, number, spread, gunname);
		return;
	}

	public static void shootBullet(Player shooter, ItemStack gun, Location loc, Vector direction, double speed, double damage, boolean isdamagetruevalue, double kb, long number, double spread, String gunname) {
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
			Metadata.setMetadata(bullet, "isdamagetruevalue", isdamagetruevalue);
			Metadata.setMetadata(bullet, "kb", kb);
			Metadata.setMetadata(bullet, "gunname", gunname);
			Metadata.setMetadata(bullet, "gun", gun);
		}
	}

	public static void interact(Player shooter, ItemStack gun, LivingEntity target, double damage, long number, boolean isdamagetruevalue) {
		Damage.entityDamage(shooter, damage * number, target, isdamagetruevalue);
		// target.damage(damage * number, player);
		return;
	}

	public static void hitBullet(LivingEntity defender, Snowball bullet) {
		if (Metadata.getMetadata(bullet, "gunname") != null) {
			LivingEntity damager = (LivingEntity) bullet.getShooter();
			ItemStack gun = (ItemStack) Metadata.getMetadata(bullet, "gun");
			double damage = (double) Metadata.getMetadata(bullet, "damage");
			boolean isdamagetruevalue = (boolean) Metadata.getMetadata(bullet, "isdamagetruevalue");
			// defender.damage(damage, (Entity)bullet.getShooter());
			if (defender instanceof Player && damager instanceof Player) {
				Player attacker = (Player) damager;
				Player victim = (Player) defender;
				if (defender.getUniqueId() == damager.getUniqueId()) {
					return; // prevent self-fire bug
				}
				String attackerteam = Teaming.getPlayerTeam(attacker);
				String victimteam = Teaming.getPlayerTeam(victim);
				if (victimteam.equals(attackerteam)) { // friendly fire
					Object[] gundamage = {gun, damage};
					punishFriendlyFire(attacker, victim, gundamage);
					return;
				}
			}
			Damage.entityDamage(damager, damage, defender, isdamagetruevalue);
			double kb = (double) Metadata.getMetadata(bullet, "kb");
			defender.setVelocity(defender.getVelocity().add(bullet.getVelocity().normalize().multiply(kb)));
			if (bullet.getShooter() instanceof Player) {
				Player shooter = (Player) damager;
				Object[] args = {"hiteffect", defender};
				redirectGun(shooter, gun, args);
			}
		} else {
			return;
		}
	}

	public static void punishFriendlyFire(Player attacker, Player victim, Object[] gundamage) {
		return;
	}

	public static Vector getRandomVector() {
		Vector random1 = Vector.getRandom();
		Vector random2 = Vector.getRandom();
		Vector random1clone = random1.clone();
		Vector random = random1clone.subtract(random2);
		return random;
	}
}
