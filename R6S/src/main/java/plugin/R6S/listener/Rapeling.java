package plugin.R6S.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.google.common.base.Objects;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Metadata;

public class Rapeling implements Listener {
	static Plugin r6s = R6SPlugin.getInstance();
	static float flyspeed = 0.01f;
	static float defaultflyspeed = 0.2f;

	@EventHandler
	public void onPlayerGrapple(PlayerFishEvent event) {
		Player player = event.getPlayer();
		GameMode gamemode = player.getGameMode();
		//long canceltimer = 5000; // ms

		// if (player exists in rapelable region) <- needs WorldGuard
		// configuration
		if (gamemode == GameMode.SURVIVAL || gamemode == GameMode.ADVENTURE) {
			if (event.getState() == org.bukkit.event.player.PlayerFishEvent.State.IN_GROUND
					|| event.getState() == org.bukkit.event.player.PlayerFishEvent.State.FAILED_ATTEMPT) {
				Location location = event.getHook().getLocation();
				Location blockloc = location.add(0, -1, 0);
				if (location.getBlock().getType() == Material.AIR && blockloc.getBlock().getType() == Material.AIR) {
					setPlayerRapeling(player, false);
					return;
				}
				if (Metadata.getMetadata(player, "rapeling").equals(false)) {
					event.setCancelled(true);
					Metadata.setMetadata(player, "snapped", false);
					setPlayerRapeling(player, "decelerate");
					checkPlayerFish(player, event.getHook().getLocation(), event.getHook().getEntityId());
					return;
				//} else if (Metadata.getMetadata(player, "rapeling").equals(true) && Metadata.getMetadata(player, "toggledfly").equals(true)) {
				//	event.setCancelled(true);
				//	setPlayerRapeling(player, "decelerate");
				} else {
					setPlayerRapeling(player, false);
					return;
				}
			} else if (event.getState() == org.bukkit.event.player.PlayerFishEvent.State.CAUGHT_FISH) {
				event.setCancelled(true);
				event.getHook().remove();
				Metadata.setMetadata(player, "snapped", true);
				setPlayerRapeling(player, false);
				return;
			}
		}
	}

	public static void setPlayerRapeling(Player player, boolean state) {
		GameMode gamemode = player.getGameMode();
		if (gamemode == GameMode.SURVIVAL || gamemode == GameMode.ADVENTURE) {
			if (state) {
				setRodStatement(player, "rapeling");
				player.setAllowFlight(true);
				player.setFlying(true);
				player.setFlySpeed(flyspeed);
				Metadata.setMetadata(player, "rapeling", true);
				Metadata.setMetadata(player, "decelerate", false);
			} //else
			if (state == false || isPlayerGrappleSnapped(player)) { // as deceleration is glitched
				setRodStatement(player, "disabled");
				player.setAllowFlight(false);
				player.setFlying(false);
				player.setFlySpeed(defaultflyspeed);
				Metadata.setMetadata(player, "rapeling", false);
				Metadata.setMetadata(player, "decelerate", false);
				//Metadata.setMetadata(player, "rapelingtimer", Calendar.getInstance().getTimeInMillis());
			}
		}
	}

	public static void setPlayerRapeling(Player player, String state) {
		// if (StringUtils.equals(state, "true") || StringUtils.equals(state, "false")) return;
		if (isPlayerGrappleSnapped(player)) {
			setPlayerRapeling(player, false);
			return;
		}
		int interval = 1;
		double deceleration = 0.1;
		double minvel = -0.1;
		// long rapelingtimer = 1000; // ms
		GameMode gamemode = player.getGameMode();
		if (gamemode == GameMode.SURVIVAL || gamemode == GameMode.ADVENTURE) {
			switch (state) {
			case "decelerate":
				//if (Metadata.getMetadata(player, "rapelingtimer") != null) {
				//	if (Calendar.getInstance().getTimeInMillis() - (long)Metadata.getMetadata(player, "rapelingtimer") <= rapelingtimer) {
				//		return;
				//	}
				//}
				player.setAllowFlight(false);
				player.setFlying(false);
				player.setFlySpeed(defaultflyspeed);
				Metadata.setMetadata(player, "rapeling", false);
				Metadata.setMetadata(player, "decelerate", true);
				setRodStatement(player, "decelerate");
				if (player.getVelocity().getY() >= minvel) {
					setPlayerRapeling(player, true);
					return;
				}
				player.setVelocity(new Vector(player.getVelocity().getX(), player.getVelocity().getY() + deceleration, player.getVelocity().getZ()));
				Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
					@Override
					public void run() {
						setPlayerRapeling(player, "decelerate");
					}
				}, interval);
				return;
			}
		}
	}

	public static void checkPlayerFish(Player player, Location location, int entityid) {
		//checkPlayerDistance(player, location);
		//checkPlayerFlying(player, location, entityid);
		int checkdelay = 1;
		int accuracy = 1;
		double allowedstep = 2;
		double declinespeed = -0.3;
		//if (Metadata.getMetadata(player, "rapeling").equals(false)) {
		//	if (Metadata.getMetadata(player, "decelerate").equals(false)) {
		//		setPlayerRapeling(player, false);
		//	return;
		//	}
		//}
		if (location != null) {
			for (Entity hook : location.getWorld().getNearbyEntities(location, accuracy, accuracy, accuracy)) {
				if (hook.getEntityId() == entityid) {
					if (player.isSneaking() && player.isFlying()) {
						player.setVelocity(new Vector(player.getVelocity().getX(), declinespeed, player.getVelocity().getZ()));
					}
					if (player.getLocation().getY() - allowedstep >= location.getY()) {
						if (player.isFlying()) {
							setPlayerRapeling(player, false);
							// player.setAllowFlight(false);
							// player.setFlying(false);
							// Metadata.setMetadata(player, "cancelrapeling", false);
						}
					} //else if (player.getAllowFlight() == false) {
						// setPlayerRapeling(player, "decelerate");
					//}
					if (Metadata.getMetadata(player, "cancelrapeling") != null) {
						if (Metadata.getMetadata(player, "cancelrapeling").equals(true)) {
							setPlayerRapeling(player, false);
							Metadata.setMetadata(player, "cancelrapeling", false);
							return;
						}
					}
					Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
						@Override
						public void run() {
							checkPlayerFish(player, location, entityid);
							checkPlayerDistance(player, location);
							checkPlayerFlying(player, location, entityid);
						}
					}, checkdelay);
					return;
				}
			}
			setPlayerRapeling(player, false);
			Metadata.setMetadata(player, "snapped", true);
			return;
		} else {
			setPlayerRapeling(player, false);
			Metadata.setMetadata(player, "snapped", true);
			return;
		}
	}

	public static boolean isPlayerGrappleSnapped(Player player) {
		if (Objects.equal(Metadata.getMetadata(player, "snapped"), false)) {
			return false;
		} else {
			return true;
		}
	}

	public static void checkPlayerDistance(Player player, Location location) {
		if (location != null) {
			if (player.isFlying()) {
				int guaranteedhdistance = 5; // allowed horizontal distance
				double hvrate = 0.5;
				Vector extent = player.getLocation().toVector().subtract(location.toVector());
				double hdistance = new Vector(extent.getX(), 0, extent.getZ()).length();
				double vdistance = extent.getY() * -1;
				if (hdistance > guaranteedhdistance && hdistance >= vdistance * hvrate) {
					Vector playervelocity = player.getVelocity();
					double force = hdistance - vdistance * hvrate;
					double forcerate = 0.005;
					player.setVelocity(new Vector(extent.getX() * -1 * force * forcerate, playervelocity.getY(), extent.getZ() * -1 * force * forcerate));
				}
			}
		}
	}

	public static void checkPlayerFlying(Player player, Location location, int entityid) {
		if (player.isFlying() == false && player.getAllowFlight()) {
			if (Metadata.getMetadata(player, "rapeling").equals(true)) {
				// if (Metadata.getMetadata(player, "decelerate").equals(true)) return;
				if (player.getLocation().getY() >= location.getY() + 3 || player.getLocation().getY() <= location.getY() - 3) {
					//int accuracy = 1;
					//for (Entity hook : location.getWorld().getNearbyEntities(location, accuracy, accuracy, accuracy)) {
					//	if (hook.getEntityId() == entityid) {
					//		 hook.remove();
					//	}
					setPlayerRapeling(player, false);
					Metadata.setMetadata(player, "cancelrapeling", true);
				//}
				}
			}
		}
	}

	@EventHandler
	public static void pullupPlayerGrapple(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		double risespeed = 0.5;
		if (item != null) {
			if (item.getType() == Material.FISHING_ROD && player.isFlying()
					&& (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
				player.setVelocity(new Vector(player.getVelocity().getX(), risespeed, player.getVelocity().getZ()));
			}
		}
	}

	public static void setRodStatement(Player player, String state) {
		for (ItemStack item : player.getInventory()) {
			if (item != null) {
				if (item.getType() == Material.FISHING_ROD) {
					ItemMeta meta = item.getItemMeta();
					switch (state) {
					case "decelerate":
						meta.setDisplayName("[DECELERATING]");
						item.setItemMeta(meta);
						if (item.containsEnchantment(Enchantment.DURABILITY)) item.removeEnchantment(Enchantment.DURABILITY);
						break;
					case "rapeling":
						meta.setDisplayName("[RAPELING]");
						item.setItemMeta(meta);
						item.addEnchantment(Enchantment.DURABILITY, 1);
						break;
					case "disabled":
						meta.setDisplayName("Grapple");
						item.setItemMeta(meta);
						if (item.containsEnchantment(Enchantment.DURABILITY)) item.removeEnchantment(Enchantment.DURABILITY);
						break;
					}
					player.updateInventory();
				}
			}
		}
	}
}
