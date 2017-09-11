package plugin.R6S.listener;

import org.apache.commons.lang.StringUtils;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import plugin.R6S.api.Metadata;

public class SecurityCamera implements Listener{
	public void onInteractCamera(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		String team = Metadata.getMetadata(player, "r6steam").toString();
		if (StringUtils.isEmpty(team)) {
			return;
		} else {
			switch (team) {
			case "red":

			case "blue":

			case "white":

			default:
			}
		}
	}
}
