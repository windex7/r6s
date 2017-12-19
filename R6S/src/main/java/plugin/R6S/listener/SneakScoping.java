package plugin.R6S.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;

public class SneakScoping implements Listener{
	static Plugin r6s = R6SPlugin.getInstance();

	@EventHandler
	public static void onSneak(PlayerToggleSneakEvent event) {
		// Player player = event.getPlayer();
		// if (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL) {
		//	if (player.isSneaking()) {
		//		Gun.redirectGun(player, player.getInventory().getItemInMainHand(), new Object[] {"scope"});
		//	} else {
		//		player.removePotionEffect(PotionEffectType.SLOW);
		//	}
		// }
	}
}
