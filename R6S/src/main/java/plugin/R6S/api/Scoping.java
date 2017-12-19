package plugin.R6S.api;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import plugin.R6S.R6SPlugin;

public class Scoping {
	static Plugin r6s = R6SPlugin.getInstance();

	public static void checkAllPlayerScoping() {
		for (World world : r6s.getServer().getWorlds()) {
			for (Player player : world.getPlayers()) {
				if (player.getGameMode() == GameMode.ADVENTURE || player.getGameMode() == GameMode.SURVIVAL) {
					if (player.isSneaking()) {
						if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() != Material.AIR) {
							Gun.redirectGun(player, player.getInventory().getItemInMainHand(), new Object[] {"scope"});
						}
					} else {
						if (player.hasPotionEffect(PotionEffectType.SLOW)) {
							player.removePotionEffect(PotionEffectType.SLOW);
						}
					}
				}
			}
		}
	}
}
