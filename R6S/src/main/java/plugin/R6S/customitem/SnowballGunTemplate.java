package plugin.R6S.customitem;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import plugin.R6S.api.Gun;
import plugin.R6S.api.NBT;
import plugin.R6S.api.PlaySound;
import plugin.R6S.api.Timing;

public class SnowballGunTemplate {
	String ctkey = "ct";
	long cooltime; // tick
	long reloadtime;
	String magazinekey = "remainbullet";
	long magazinesize = 1;
	double speed;
	double damage;
	boolean isdamagetruevalue = false;
	double kb;
	long number;
	double spread;
	double recoil;
	int scopelevel = 4;
	String firesound[] = {"ENTITY_ZOMBIE_ATTACK_DOOR_WOOD", "ENTITY_ZOMBIE_ATTACK_DOOR_WOOD"};
	float volume[] = {1, 1};
	float pitch[] = {0, 2};
	long delay[] = {0, 0};
	String soundmode = "world";

	public void gun(Player shooter, ItemStack gun, Object[] args) {
		switch (args[0].toString()) {
		case "trigger":
			if (isCT(gun)) {
				// Message.sendMessage(shooter, "CT left: " + leftct);
				break;
			}
			if (isEmpty(gun)) {
				setReloaded(shooter, gun);
				break;
			}
			setFired(shooter, gun);
			// setCTed(shooter, gun, cooltime);
			Gun.shootBullet(shooter, gun, speed, damage, isdamagetruevalue, kb, number, spread, recoil, gun.getItemMeta().getDisplayName());
			playSound(shooter, shooter.getLocation(), soundmode);
			break;
		case "interact":
			if (isCT(gun)) {
				// Message.sendMessage(shooter, "CT left: " + leftct);
				break;
			}
			if (isEmpty(gun)) {
				setReloaded(shooter, gun);
				break;
			}
			setFired(shooter, gun);
			// setCTed(shooter, gun, cooltime);
			Gun.interact(shooter, gun, (LivingEntity) args[1], damage, number, isdamagetruevalue);
			playSound(shooter, shooter.getLocation(), soundmode);
			break;
		case "hiteffect":
			hiteffect(shooter, gun, args[1]);
			break;
		case "scope":
			scope(shooter);
			break;
		case "reload":
			setReloaded(shooter, gun);
			break;
		default:
			return;
		}
	}

	public void playSound(Player player, Location loc, String mode) {
		PlaySound.play(player, loc, firesound, volume, pitch, delay, mode);
		return;
	}

	public void hiteffect(Player player, ItemStack gun, Object arg) {

	}

	public void scope(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, scopelevel, false, false), false);
		return;
	}

	public boolean isEmpty(ItemStack gun) {
		long bulletnumber = getMagazineLeft(gun);
		if (bulletnumber == 0) {
			return true;
		}
		return false;
	}

	public void setFired(Player player, ItemStack gun) {
		player.getInventory().setItemInMainHand(fireBullet(gun));
		player.sendMessage(String.valueOf(NBT.readItemTag(gun, magazinekey, "long")));
	}

	public ItemStack fireBullet(ItemStack gun) {
		ItemStack ctedgun = setCT(gun, cooltime);
		long bulletnumber = getMagazineLeft(gun);
		long leftbulletnumber = bulletnumber - 1;
		if (bulletnumber > 0) {
			return setMagazineLeft(ctedgun, leftbulletnumber);
		} else if (bulletnumber == 0) {
			becomeEmpty(ctedgun);
			return reload(ctedgun);
		} else {
			long leftmagazine = magazinesize - 1;
			if (leftmagazine > 1) {
				return setMagazineLeft(ctedgun, leftmagazine);
			} else {
				return reload(ctedgun);
			}
		}
	}

	public ItemStack setMagazineLeft(ItemStack gun, long magazine) {
		short maxdurability = gun.getType().getMaxDurability();
		long durability = maxdurability * (magazine / magazinesize);
		gun.setDurability((short)durability);
		return NBT.writeItemTag(gun, magazinekey, magazine, "long");
	}

	public long getMagazineLeft(ItemStack gun) {
		if (NBT.readItemTag(gun, magazinekey, "long") != null) {
			return (long) NBT.readItemTag(gun, magazinekey, "long");
		} else {
			return -1;
		}
	}

	public void setReloaded(Player player, ItemStack gun) {
		player.getInventory().setItemInMainHand(reload(gun));
	}

	public void becomeEmpty(ItemStack gun) {
		gun.setDurability((short)1);
	}

	public ItemStack reload(ItemStack gun) {
		if (reloadtime == 0) {
			return gun;
		} else {
			return setCT(gun, reloadtime);
		}
	}

	public void charge(Player player, ItemStack gun) {

	}

	public void setCTed(Player player, ItemStack gun, long ct) {
		player.getInventory().setItemInMainHand(setCT(gun, ct));
	}

	public ItemStack setCT(ItemStack gun, long ct) {
		// ItemStack firedgun = fireBullet(gun);
		if (getCT(gun) >= Timing.getTime() + ct) {
			return gun;
		} else {
			return NBT.writeItemTag(gun, ctkey, Timing.getTime() + ct, "long");
		}
	}

	public long getCT(ItemStack gun) {
		if (NBT.readItemTag(gun, ctkey, "long") != null) {
			return (long) NBT.readItemTag(gun, ctkey, "long");
		} else {
			return 0;
		}
	}

	public boolean isCT(ItemStack gun) {
		if (getCT(gun) > Timing.getTime()) {
			return true;
		} else {
			return false;
		}
	}
}
