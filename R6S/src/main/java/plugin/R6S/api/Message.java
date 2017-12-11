package plugin.R6S.api;

import org.bukkit.entity.Player;

public class Message {
	public static void sendMessage(Player player, String message) {
		player.sendMessage(message);
	}
}
