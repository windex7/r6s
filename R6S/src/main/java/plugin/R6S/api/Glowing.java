package plugin.R6S.api;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Glowing {
	public static void setPlayerGlowing(Player player) {
		if (player != null) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));
		}
	}
}
