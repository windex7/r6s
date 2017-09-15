package plugin.R6S.api;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Glowing {
	public static void setPlayerGlowing(Player player, int interval) {
		if (player != null) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, interval * 2, 1));
		}
	}
}
