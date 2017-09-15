package plugin.R6S.listener;

import java.util.Calendar;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import plugin.R6S.api.Metadata;

public class ClickedCommandBlock implements Listener {
	@EventHandler
	public static void onClickStoneButton(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack item = event.getItem();
		if (action == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			if (block != null) {
				if (block.getType() == Material.STONE_BUTTON) {
					Metadata.setMetadata(player, "stonebutton", Calendar.getInstance().getTimeInMillis());
				}
			}
		}
	}
}
