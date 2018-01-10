package plugin.R6S.listener;

import java.util.Objects;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.ItemEntity;
import plugin.R6S.api.NBT;
import plugin.R6S.api.R6SConfig;

public class DisableOffhand implements Listener{
	static String offhandtag = R6SConfig.getTag("offhand");

	public static boolean isAllowedOffhand(ItemStack item) {
		if (Objects.equals(NBT.readItemTag(item, offhandtag, "String"), "true")) {
			return true;
		} else {
			return false;
		}
	}

	public static void checkAllOffhand() {
		for (World world : R6SPlugin.getInstance().getServer().getWorlds()) {
			for (Player player : world.getPlayers()) {
				checkOffhand(player);
			}
		}
	}

	public static void checkOffhand(Player player) {
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;
		if (player.getInventory().getItemInOffHand().getType() != Material.AIR) {
			ItemStack offhanditem = player.getInventory().getItemInOffHand();
			if (!isAllowedOffhand(offhanditem)) {
				ItemEntity.dropItem(player, offhanditem);
				player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
			}
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
