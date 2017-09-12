package plugin.R6S.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Metadata;

public class Rapeling implements Listener {
	static Plugin r6s = R6SPlugin.getInstance();
	static float flyspeed = 0.05f;
	static float defaultflyspeed = 0.2f;

	@EventHandler
	public void onPlayerGrapple(PlayerFishEvent event) {
		Player player = event.getPlayer();
		GameMode gamemode = player.getGameMode();

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
					setPlayerRapeling(player, true);
					checkPlayerFish(player, event.getHook().getLocation(), event.getHook().getEntityId());
					return;
				} else {
					setPlayerRapeling(player, false);
					return;
				}

			} else if (event.getState() == org.bukkit.event.player.PlayerFishEvent.State.CAUGHT_FISH) {
				event.setCancelled(true);
				event.getHook().remove();
				setPlayerRapeling(player, false);
				return;
			}
		}
	}

	public static void setPlayerRapeling(Player player, boolean state) {
		GameMode gamemode = player.getGameMode();
		if (gamemode == GameMode.SURVIVAL || gamemode == GameMode.ADVENTURE) {
			if (state) {
				player.setAllowFlight(true);
				player.setFlying(true);
				player.setFlySpeed(flyspeed);
				Metadata.setMetadata(player, "rapeling", true);
			} else {
				player.setAllowFlight(false);
				player.setFlying(false);
				player.setFlySpeed(defaultflyspeed);
				Metadata.setMetadata(player, "rapeling", false);
			}
		}
	}

	public static void checkPlayerFish(Player player, Location location, int entityid) {
		int checkdelay = 10;
		int accuracy = 1;

		if (location != null) {
			for (Entity hook : location.getWorld().getNearbyEntities(location, accuracy, accuracy, accuracy)) {
				// if (hook.getType() == EntityType.FISHING_HOOK) {
				if (hook.getEntityId() == entityid) {
					if (player.getLocation().getY() + 1 <= location.getY()) {
						if (player.isFlying()) {
							player.setAllowFlight(false);
							player.setFlying(false);
						}
					} else if (player.getAllowFlight() == false) {
						player.setAllowFlight(true);
						player.setFlying(true);
					}
					Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
						@Override
						public void run() {
							checkPlayerFish(player, location, entityid);
						}
					}, checkdelay);
					return;
				}
				// }
			}
			setPlayerRapeling(player, false);
			return;
		} else {
			setPlayerRapeling(player, false);
			return;
		}
	}
}
