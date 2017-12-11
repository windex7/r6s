package plugin.R6S.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import plugin.R6S.api.DamageTick;

public class RemoveDamageTick implements Listener {
	@EventHandler
	public static void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		//if (event.getEntity() instanceof Player) {
		//	Player player = (Player) event.getEntity();
		//	DamageTick.removeDamageTick(player);
		//	// player.setNoDamageTicks(0);
		//	return;
		//}
		if (event.getEntity() instanceof LivingEntity) {
			DamageTick.removeDamageTick((LivingEntity) event.getEntity());
		}
	}
}
