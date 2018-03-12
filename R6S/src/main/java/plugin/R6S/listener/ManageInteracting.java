package plugin.R6S.listener;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import plugin.R6S.api.Gun;
import plugin.R6S.api.Teaming;

public class ManageInteracting implements Listener {
	@EventHandler
	public static void onPlayerInteractEntity(PlayerInteractAtEntityEvent event) { // previously used PlayerInteractEntityEvent
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
				Vector vec = event.getClickedPosition();
				Location loc = new Location(player.getWorld(), vec.getX(), vec.getY(), vec.getZ());
				Object args[] = {"interact", target, loc};
				Gun.redirectGun(player, item, args);
			}
		}
		event.setCancelled(true);
		return;
	}
}
