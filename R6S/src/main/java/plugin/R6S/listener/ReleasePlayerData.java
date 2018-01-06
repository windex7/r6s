package plugin.R6S.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import plugin.R6S.api.InventoryIO;
import plugin.R6S.api.R6SGame;

public class ReleasePlayerData implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		releasePlayerData(player);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		resetPlayerData(player);
		Location deathloc = event.getEntity().getLocation();
		R6SGame.onPlayerDie(player, deathloc);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (R6SGame.isPlaying(player)) {
			InventoryIO.loadInventory(player, "player", "inventory");
		}
		releasePlayerData(player);
	}

	public static void releasePlayerData(Player player) {
		R6SGame.removeQueue(player);
		R6SGame.removePlayerList(player);
	}

	public static void resetPlayerData(Player player) {
		Rapeling.setPlayerRapeling(player, false);
	}
}
