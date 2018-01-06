package plugin.R6S.customitem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Gun;
import plugin.R6S.api.NBT;
import plugin.R6S.api.PlaySound;
import plugin.R6S.api.Timing;

public class SnowballGunTemplate {
	Plugin r6s = R6SPlugin.getInstance();
	String ctkey = "ct";
	long cooltime; // tick
	String reloadkey = "isreloading";
	long reloadtime;
	String magazinekey = "remainbullet";
	short magazinesize = 1;
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
		if (!(args != null)) return;
		switch (args[0].toString()) {
		case "trigger":
			if (isEmpty(gun)) {
				if (isReloading(gun)) {
					break;
				} else {
					forceReloading(shooter, gun);
					break;
				}
			}
			if (isCT(gun)) {
				break;
			}
			setFired(shooter, gun);
			// setCTed(shooter, gun, cooltime);
			Gun.shootBullet(shooter, gun, speed, damage, isdamagetruevalue, kb, number, spread, recoil, gun.getItemMeta().getDisplayName());
			playSound(shooter, shooter.getLocation(), soundmode);
			break;
		case "interact":
			if (isEmpty(gun)) {
				if (isReloading(gun)) {
					break;
				} else {
					forceReloading(shooter, gun);
					break;
				}
			}
			if (isCT(gun)) {
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
			forceReloading(shooter, gun);
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
		player.getInventory().setItemInMainHand(fireBullet(player, gun));
		player.sendMessage(String.valueOf(getMagazineLeft(gun)));
	}

	public ItemStack fireBullet(Player player, ItemStack gun) {
		ItemStack ctedgun = setCT(gun, cooltime);
		short bulletnumber = (short) getMagazineLeft(gun);
		short leftbulletnumber = (short) (bulletnumber- 1);
		if (bulletnumber > 0) {
			return setMagazineLeft(ctedgun, leftbulletnumber);
		} else if (bulletnumber == 0) {
			becomeEmpty(ctedgun);
			return reload(player, ctedgun);
		} else {
			short leftmagazine = (short) (magazinesize - 1);
			if (leftmagazine > 1) {
				return setMagazineLeft(ctedgun, leftmagazine);
			} else {
				return reload(player, ctedgun);
			}
		}
	}

	public ItemStack setMagazineLeft(ItemStack gun, short magazine) {
		short maxdurability = gun.getType().getMaxDurability();
		short durability = (short)(maxdurability * (magazinesize - magazine) / magazinesize);
		if (durability == maxdurability) {
			durability = maxdurability--;
			gun.setDurability((short)durability);
		} else {
			gun.setDurability((short)durability);
		}
		return NBT.writeItemTag(gun, magazinekey, (long) magazine, "long");
	}

	public long getMagazineLeft(ItemStack gun) {
		if (NBT.readItemTag(gun, magazinekey, "long") != null) {
			return (long) NBT.readItemTag(gun, magazinekey, "long");
		} else {
			return -1;
		}
	}

	public void forceReloading(Player player, ItemStack gun) {
		player.getInventory().setItemInMainHand(reload(player, gun));
	}

	public void becomeEmpty(ItemStack gun) {
		gun.setDurability((short)1);
	}

	public ItemStack reload(Player player, ItemStack gun) {
		if (reloadtime == 0) {
			return setMagazineLeft(gun, magazinesize);
		} else {
			charge(player, gun, reloadtime);
			ItemStack setreloadinggun = setReloading(gun, true);
			return setCT(setreloadinggun, reloadtime);
		}
	}

	public boolean isReloading(ItemStack gun) {
		if (NBT.readItemTag(gun, reloadkey, "boolean") != null) {
			boolean isreloading = (boolean) NBT.readItemTag(gun, reloadkey, "boolean");
			if (isreloading) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public ItemStack setReloading(ItemStack gun, boolean isreloading) {
		return NBT.writeItemTag(gun, reloadkey, isreloading, "boolean");
	}

	public void charge(Player player, ItemStack gun, long fullct) {
		long triggeredtime = Timing.getTime();
		new BukkitRunnable() {
			public void run() {
				ItemStack handitem = player.getInventory().getItemInMainHand();
				if (handitem.getType().equals(gun.getType())) {
					long leftct = (triggeredtime + fullct) - Timing.getTime();
					if (leftct > 0) {
						short maxdurability = handitem.getType().getMaxDurability();
						short reloadprogress = (short)(maxdurability * leftct / fullct);
						handitem.setDurability(reloadprogress);
					} else if (leftct <= 0) {
						handitem.setDurability((short)0);
						ItemStack reloadedgun = setMagazineLeft(handitem, magazinesize);
						ItemStack setreloadedgun = setReloading(reloadedgun, false);
						player.getInventory().setItemInMainHand(setreloadedgun);
						cancel();
					}
				} else {
					for (int i = 0; i < 36; i++) {
						ItemStack item = player.getInventory().getItem(i);
						if (isReloading(item)) {
							player.getInventory().setItem(i, setReloading(item, false));
							player.updateInventory();
						}
					}
					for (int i = 80; i <= 83; i++) {
						ItemStack item = player.getInventory().getItem(i);
						if (isReloading(item)) {
							player.getInventory().setItem(i, setReloading(item, false));
							player.updateInventory();
						}
					}
					if (!(player.getItemOnCursor().getType().equals(Material.AIR))) {
						if (isReloading(player.getItemOnCursor())) {
							player.setItemOnCursor(setReloading(player.getItemOnCursor(), false));
							player.updateInventory();
						}
					}
					cancel();
				}
			}
		}.runTaskTimer(this.r6s, 0, 1);
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

	public long getRemainCT(ItemStack gun) {
		if (getCT(gun) == 0 || !(isCT(gun))) {
			return 0;
		} else {
			return -(Timing.getTimeDiff(getCT(gun)));
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
