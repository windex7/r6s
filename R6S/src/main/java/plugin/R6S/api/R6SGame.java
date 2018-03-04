package plugin.R6S.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import plugin.R6S.R6SPlugin;
import plugin.R6S.listener.ManageExplosion;

public class R6SGame {
	static Plugin r6s = R6SPlugin.getInstance();
	static int minstartnum = 2;
	static int waittillstart = 20; // sec
	static int countdown;
	static boolean isGameGoing = false;
	static boolean isCountingDown = false;
	static boolean isSwitched = false;
	static List<Player> queue = new ArrayList<Player>();
	static List<Player> playerlist = new ArrayList<Player>();

	static String red = "Terrorist";
	static String blue = "CounterTerrorist";
	static String white = "FFA";
	static String defaultcolor = "nocollision";

	static String stage = "stage1";

	public static String getStage() {
		return stage;
	}

	public static void changeStage(String stagename) {
		switch (stagename) {
		case "stage1":
			stage = stagename;
			break;
		default:
			break;
		}
		return;
	}

	public static String getTeamName(String color) {
		switch (color) {
		case "red":
			return red;
		case "blue":
			return blue;
		case "white":
			return white;
		case "default":
		default:
			return defaultcolor;
		}
	}

	static List<Player> redalive = new ArrayList<Player>();
	static List<Player> bluealive = new ArrayList<Player>();
	static int redpoint = 0;
	static int bluepoint = 0;
	static int maxpoint = 2;
	static int round = 0;
	static int maxround = 5;
	static int switchteaminterval = maxpoint;

	public static void addAliveList(Player player, String team) {
		switch (team) {
		case "red":
			if (!redalive.contains(player)) {
				redalive.add(player);
			}
			break;
		case "blue":
			if (!bluealive.contains(player)) {
				bluealive.add(player);
			}
			break;
		default:
			break;
		}
	}

	public static void removeAliveList(Player player) {
		if (redalive.contains(player)) {
			redalive.remove(player);
		}
		if (bluealive.contains(player)) {
			bluealive.remove(player);
		}
		checkAliveNumber();
	}

	public static boolean isAliveList(Player player) {
		if (redalive.contains(player)) {
			return true;
		} else if (bluealive.contains(player)) {
			return true;
		} else {
			return false;
		}
	}

	public static void clearAliveList() {
		redalive.clear();
		bluealive.clear();
	}

	public static void checkAliveNumber() {
		if (redalive.size() == 0) {
			endRound("blue");
		} else if (bluealive.size() == 0) {
			endRound("red");
		}
	}

	public static void endRound(String team) {
		switch (team) {
		case "red":
			redpoint++;
			if (redpoint > maxpoint) {
				endGame();
				return;
			}
			break;
		case "blue":
			bluepoint++;
			if (bluepoint > maxpoint) {
				endGame();
				return;
			}
			break;
		default:
			break;
		}
		r6s.getServer().broadcastMessage("redpoint: " + redpoint + ", bluepoint: " + bluepoint);
		round++;
		if (round >= maxround) {
			endGame();
			return;
		}
		if (round % switchteaminterval == 0) {
			switchTeamSpawn();
		}
		startRound();
	}

	public static void startRound() {
		if (!isQueueEmpty()) {
			processQueue();
		}
		for (Player player : getPlayerList()) {
			player.sendMessage("Started Round: " + (round + 1));
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
			@Override
			public void run() {
				Teaming.spawnTeamMember(isSwitched);
			}
		}, 10);
	}

	public static void endGame() {
		r6s.getServer().broadcastMessage("redpoint: " + redpoint + ", bluepoint: " + bluepoint);
		String winteam;
		if (redpoint > bluepoint) {
			winteam = "red";
		} else if (redpoint < bluepoint){
			winteam = "blue";
		} else {
			winteam = "draw";
		}
		for (Player player : getPlayerList()) {
			player.teleport(R6SConfig.getWaypoint(stage, "spec"));
			InventoryIO.rollbackPlayerInventory(player);
			switch (winteam) {
			case "red":
				if (Teaming.getPlayerTeam(player).equals("red")) {
					player.sendMessage("You Win!!!");
				} else {
					player.sendMessage("You Lose...");
				}
				break;
			case "blue":
				if (Teaming.getPlayerTeam(player).equals("blue")) {
					player.sendMessage("You Win!!!");
				} else {
					player.sendMessage("You Lose...");
				}
				break;
			case "draw":
			default:
				player.sendMessage("Draw!");
				break;
			}
		}
		clearGameData();
		ManageExplosion.setExplosionDisabled(true);
		R6SStage.regenStage();
	}

	public static void clearGameData() {
		isGameGoing = false;
		isCountingDown = false;
		isSwitched = false;
		round = 0;
		redpoint = 0;
		bluepoint = 0;
		clearAliveList();
		clearPlayerList();
		clearQueue();
		Teaming.resetAllPlayerTeams();
	}

	public static List<Player> getPlayerList() {
		return playerlist;
	}

	public static void switchTeamSpawn() {
		isSwitched = !isSwitched;
		R6SStage.regenStage();
	}

	public static void addPlayerList(Player player) {
		playerlist.add(player);
		return;
	}

	public static void removePlayerList(Player player) {
		if (isPlaying(player)) {
			playerlist.remove(player);
		}
		Teaming.removeEntry(player.getName(), Teaming.getPlayerTeam(player));
		return;
	}

	public static boolean isPlaying(Player player) {
		return playerlist.contains(player);
	}

	public static void shufflePlayerList() {
		Collections.shuffle(playerlist);
	}

	public static void clearPlayerList() {
		playerlist.clear();
		return;
	}

	public static void addQueue(Player player) {
		queue.add(player);
		InventoryIO.backupPlayerInventory(player);
		player.teleport(R6SConfig.getWaypoint(stage, "lobby"));
		player.setGameMode(GameMode.SURVIVAL);
		Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
			@Override
			public void run() {
				InventoryIO.loadPlayerInventory(player, "config", "inv.queue");
				if (getNumberOfQueue() >= minstartnum && !(isCountingDown)) {
					preStartGame();
				}
			}
		}, 10);
	}

	public static void removeQueue(Player player) {
		if (isQueue(player)) {
			queue.remove(player);
			InventoryIO.rollbackPlayerInventory(player);
			player.teleport(R6SConfig.getWaypoint("hub", "lobby"));
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

	public static boolean isQueueEmpty() {
		if (queue.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public static int getNumberOfQueue() {
		return queue.size();
	}

	public static void processQueue() {
		for (Player player : queue) {
			if (playerlist.contains(player)) {
				r6s.getServer().getLogger().info("warning: " + player.getName() + " is already exists on playerlist!");
			} else {
				addPlayerList(player);
			}
		}
		shufflePlayerList();
		for (Player player : playerlist) {
			if (Objects.equals(Teaming.getPlayerTeam(player), "default") || Objects.equals(Teaming.getPlayerTeam(player), null)) {
				Teaming.teamingPlayer(player, "normal");
			}
		}
		clearQueue();
	}

	public static void joinTeam(Player player, String team) {
		Teaming.addEntry(player.getName(), team);
	}

	public static void preStartGame() {
		if (isGameGoing) {
			return;
		}
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
		isSwitched = false;
		round = 0;
		redpoint = 0;
		bluepoint = 0;
		processQueue();
		Teaming.spawnTeamMember(isSwitched);
		ManageExplosion.setExplosionDisabled(false);
		R6SStage.regenStage();
		r6s.getServer().broadcastMessage("Round: " + round);
	}

	public static void onPlayerDie(Player player, Location deathloc) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
			@Override
			public void run () {
				player.spigot().respawn();
			}
		}, 10);

		Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
			@Override
			public void run () {
				if (playerlist.contains(player)) {
					player.setGameMode(GameMode.SPECTATOR);
					if (isAliveList(player)) {
						removeAliveList(player);
						if (deathloc.getBlockY() > 0) {
							player.teleport(deathloc);
						} else {
							Location loc = deathloc.clone();
							loc.setY(10);
							player.teleport(loc);
						}
					}
				}
			}
		}, 20);
	}

	public static void applyEquipments(Player player, String team) {
		String kit = R6SKit.getKit(player);
		applyKit(player, kit, team);
		String[] subweapons = R6SKit.getSubweapon(player);
		for (String subweapon : subweapons) {
			applySubweapon(player, subweapon);
		}
	}

	public static void applyKit(Player player, String kit, String team) {
		String period = ".";
		String configname = "config";
		String kitstring = "kit";
		InventoryIO.loadPlayerInventory(player, configname, kitstring + period + kit + period + team);
	}

	public static void applySubweapon(Player player, String weapon) {
		String period = ".";
		String configname = "config";
		String substring = "item.grenade";
		String weaponstring = Config.getConfig(configname, substring + period + weapon).toString();
		ItemStack item = Base64Item.itemFromString(weaponstring);
		player.getInventory().addItem(item);
	}
}
