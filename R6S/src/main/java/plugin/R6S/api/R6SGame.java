package plugin.R6S.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class R6SGame {
	static int minstartnum = 2;
	static boolean isGameGoing = false;
	static List<Player> queue = new ArrayList<Player>();
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

	public static void addQueue(Player player) {
		queue.add(player);
	}

	public static void removeQueue(Player player) {
		queue.remove(player);
	}

	public static List<Player> getQueue() {
		return queue;
	}

	public static void clearQueue() {
		queue.clear();
	}

	public static boolean isQueue(Player player) {
		return queue.contains(player);
	}

	public static int getNumberOfQueue() {
		return queue.size();
	}

	public static void joinTeam(Player player, String team) {
		Teaming.addEntry(player.getName(), team);
	}

	public static void startGame() {

	}
}
