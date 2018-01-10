package plugin.R6S.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.Gun;
import plugin.R6S.api.Teaming;

public class PreventInteractGlitch implements Listener {
	@EventHandler
	public static void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked() instanceof LivingEntity) {
			LivingEntity target = (LivingEntity) event.getRightClicked();
			if (player.getInventory().getItemInMainHand() != null) {
				ItemStack item = player.getInventory().getItemInMainHand();
				if (target instanceof Player) {
					Player victim = (Player) target;
					if (Teaming.getPlayerTeam(victim).equals(Teaming.getPlayerTeam(player))) {
						Object[] gundamage = { item };
						Gun.punishFriendlyFire(player, victim, gundamage);
						event.setCancelled(true);
						return;
					}
				}
				Object args[] = { "interact", target };
				Gun.redirectGun(player, item, args);
			}
		}
		event.setCancelled(true);
		return;
	}
}
