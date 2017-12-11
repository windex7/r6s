package plugin.R6S.api;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class DamageTick {
	public static void removeDamageTick(LivingEntity entity) {
		entity.setNoDamageTicks(0);
		return;
	}

	public static void removeDamageTickAllEntity() {
		for (World world : Bukkit.getServer().getWorlds()) {
			for (LivingEntity entity : world.getLivingEntities()) {
				removeDamageTick(entity);
			}
		}
	}
}
