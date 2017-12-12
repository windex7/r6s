package plugin.R6S.listener;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.Gun;

public class PreventInteractGlitch implements Listener {
	@EventHandler
	public static void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		if (event.getRightClicked() instanceof LivingEntity) {
			LivingEntity target = (LivingEntity) event.getRightClicked();
			if (player.getInventory().getItemInMainHand() != null) {
				ItemStack item = player.getInventory().getItemInMainHand();
				if (item.getItemMeta().getDisplayName() != null) {
					Object args[] = {"interact", target};
					Gun.redirectGun(player, item, args);
				}
			}
		}
		event.setCancelled(true);
		return;
	}
}
