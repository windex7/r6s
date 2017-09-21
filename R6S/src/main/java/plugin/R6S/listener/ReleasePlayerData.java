package plugin.R6S.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import plugin.R6S.api.Metadata;

public class ReleasePlayerData implements Listener {
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		releasePlayerData(player);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		releasePlayerData(player);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		releasePlayerData(player);
	}

	public static void releasePlayerData(Player player) {
		// cancel rapeling
		Rapeling.setPlayerRapeling(player, false);
		// String[] keylist = { "team" };
		// Metadata.clearMetadata(player, keylist);
		Metadata.setMetadata(player, "rapeling", false);
		// Metadata.setMetadata(player, "snapped", false);
		// Metadata.setMetadata(player, "rapelingtimer", Calendar.getInstance().getTimeInMillis());
		Metadata.setMetadata(player, "cancelrapeling", false);
	}
}
