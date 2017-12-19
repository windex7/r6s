package plugin.R6S.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import plugin.R6S.R6SPlugin;

public class R6SGame {
	static Plugin r6s = R6SPlugin.getInstance();
	static int minstartnum = 2;
	static int waittillstart = 10; // sec
	static int countdown = 10; // sec
	static boolean isGameGoing = false;
	static boolean isCountingDown = false;
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
		if (isQueue(player)) {
			playerlist.remove(player);
		}
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
		if (getNumberOfQueue() >= minstartnum && !(isCountingDown)) {
			preStartGame();
		}
	}

	public static void removeQueue(Player player) {
		if (isQueue(player)) {
			queue.remove(player);
		}
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

	public static void preStartGame() {
		isCountingDown = true;
		countdown = waittillstart;
		new BukkitRunnable() {
			public void run() {
				if (countdown > 0) {
					countdown--;
					r6s.getServer().broadcastMessage("the game starts in " + countdown + " sec!");
					if (getNumberOfQueue() < minstartnum) {
						cancelStartGame();
						cancel();
					}
				} else if (countdown == 0) {
					startGame();
					cancel();
				}
			}
		}.runTaskTimer(r6s, 20, 20);
	}

	public static void cancelStartGame() {
		isCountingDown = false;
		isGameGoing = false;
	}

	public static void startGame() {
		isCountingDown = false;
		isGameGoing = true;
		clearQueue();
	}

	public static void onPlayerDie(Player player) {

	}
}
