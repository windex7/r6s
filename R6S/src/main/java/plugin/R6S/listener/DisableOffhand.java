package plugin.R6S.listener;

import java.util.Objects;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.NBT;

public class DisableOffhand implements Listener{
	static String offhandtag = "offhand";

	public static boolean isAllowedOffhand(ItemStack item) {
		if (Objects.equals(NBT.readItemTag(item, offhandtag, "String"), "true")) {
			return true;
		} else {
			return false;
		}
	}

	@EventHandler
	public static void disableSwapping(PlayerSwapHandItemsEvent event) {
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE || event.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
		if (isAllowedOffhand(event.getOffHandItem())) {
			return;
		} else {
			if (event.getOffHandItem().getType().equals(Material.AIR)) {
				return;
			} else {
				event.setCancelled(true);
				return;
			}
		}
	}

}
