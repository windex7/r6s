package plugin.R6S.api;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

public class Damage {
	public static void entityDamage(LivingEntity damager, double damage, LivingEntity defender, boolean truevalue) {
		if (truevalue) {
			defender.damage(damage, damager);
			return;
		} else {
			// ((net.minecraft.server.v1_9_R2.EntityLiving)defender).damageEntity((DamageSource)damager, (float)damage);
			defender.damage(damage, damager);
			return;
		}
	}

	public static void removeDamageTick(LivingEntity entity) {
		// entity.setNoDamageTicks(0);
		entity.setMaximumNoDamageTicks(0);
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
