package plugin.R6S.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class R6SGame {
	static boolean isGameGoing = false;
	static List<Player> playerlist = new ArrayList<Player>();

	public static List<Player> getPlayerList() {
		return playerlist;
	}

	public static void addPlayerList(Player player) {
		playerlist.add(player);
		return;
	}

	public static void removePlayerList(Player player) {
		playerlist.remove(player);
		return;
	}

	public static boolean isPlaying(Player player) {
		return playerlist.contains(player);
	}

	public static void clearPlayerList() {
		playerlist.clear();
		return;
	}
}
