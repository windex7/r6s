package plugin.R6S.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class RemoveDamageTick implements Listener {
	@EventHandler
	public static void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			// if (event.getDamager() instanceof Player) {
			// }
			Player player = (Player) event.getEntity();
			if (event.getDamager() instanceof Projectile) {
				player.setNoDamageTicks(0);
			}
		}
	}
}
