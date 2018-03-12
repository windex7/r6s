package plugin.R6S.api;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import plugin.R6S.R6SPlugin;

public class Teaming {
	static Plugin r6s = R6SPlugin.getInstance();
	static ScoreboardManager manager = Bukkit.getScoreboardManager();
	static Scoreboard board = manager.getMainScoreboard();

	final static String red = "Terrorist";
	final static String blue = "CounterTerrorist";
	final static String white = "FFA";
	final static String defaultcolor = "nocollision";
	static Team redteam = board.getTeam(red);
	static Team blueteam = board.getTeam(blue);
	static Team whiteteam = board.getTeam(white);
	static Team defaultteam = board.getTeam(defaultcolor);

	public static void addEntry(String entry, String team) {
		Team targetteam = board.getTeam(team);
		if (!(targetteam.hasEntry(entry))) {
			targetteam.addEntry(entry);
		}
	}

	public static void removeEntry(String entry, String team) {
		if (team != null) {
			Team targetteam = board.getTeam(team);
			if (targetteam.hasEntry(entry)) {
				targetteam.removeEntry(entry);
			}
			// R6SGame.removeAliveList(r6s.getServer().getPlayer(entry));
		}
	}

	public static void registerPlayerTeam(Player target, String team) {
		String targetname = target.getName();
		// if (getPlayerTeam(target) != null) {
		// 	removeEntry(targetname, getPlayerTeam(target));
		// }
		if (board.getTeams().contains(board.getTeam(team))) {
			addEntry(targetname, team);
			return;
		}
		// legacy code :
		switch (team) {
		case "red":
		case red:
			addEntry(targetname, red);
			break;
		case "blue":
		case blue:
			addEntry(targetname, blue);
			break;
		case "white":
		case white:
			addEntry(targetname, white);
			break;
		case "default":
		case defaultcolor:
		default:
			addEntry(targetname, defaultcolor);
			break;
		}
	}

	public static String getPlayerTeam(Player target) {
		String targetname = target.getName();
		if (redteam.hasEntry(targetname)) {
			return "red";
		} else if (blueteam.hasEntry(targetname)) {
			return "blue";
		} else if (whiteteam.hasEntry(targetname)){
			return "white";
		} else if (defaultteam.hasEntry(targetname)){
			return "default";
		} else {
			return null;
		}
	}

	public static Set<String> getAllPlayerOnTeam(String team) {
		if (board.getTeams().contains(board.getTeam(team))) {
			return board.getTeam(team).getEntries();
		} else {
			return null;
		}
	}

	public static int getNumberOfTeamMember(String team) {
		if (board.getTeams().contains(board.getTeam(team))) {
			return board.getTeam(team).getEntries().size();
		} else {
			return 0;
		}
	}

	public static void resetAllPlayerTeams() {
		Team[] targetteams = {redteam, blueteam, whiteteam};
		for (Team team: targetteams) {
			for (String entry : team.getEntries()) {
				removeEntry(entry, team.getName());
			}
		}
	}

	public static List<Player> shuffleList(List<Player> playerlist) {
		List<Player> list = playerlist;
		Collections.shuffle(list);
		return list;
	}

	public static void teamingPlayer(Player player, String mode) {
		switch (mode) {
		case "normal":
			int rednumber = getNumberOfTeamMember(red);
			int bluenumber = getNumberOfTeamMember(blue);
			if (rednumber > bluenumber) {
				registerPlayerTeam(player, "blue");
			} else if (bluenumber > rednumber) {
				registerPlayerTeam(player, "red");
			} else {
				if (Math.random() < 0.5) {
					registerPlayerTeam(player, "red");
				} else {
					registerPlayerTeam(player, "blue");
				}
			}
			break;
		default:
			return;
		}
	}

	public static void spawnTeamMember(boolean isSwitched) {
		R6SGame.clearAliveList();
		for (String playername : getAllPlayerOnTeam(red)) {
			Player player = r6s.getServer().getPlayer(playername);
			if (player != null) {
				R6SGame.addAliveList(player, "red");
				//InventoryIO.backupPlayerInventory(player);
				player.getInventory().clear();
				R6SGame.applyEquipments(player, "red");
				player.setGameMode(GameMode.SURVIVAL);
				player.setHealth(player.getMaxHealth());
				if (isSwitched) {
					player.teleport(R6SConfig.getSpawnpoint("blue"));
				} else {
					player.teleport(R6SConfig.getSpawnpoint("red"));
				}
			}
		}
		for (String playername : getAllPlayerOnTeam(blue)) {
			Player player = r6s.getServer().getPlayer(playername);
			if (player != null) {
				R6SGame.addAliveList(player, "blue");
				//InventoryIO.backupPlayerInventory(player);
				player.getInventory().clear();
				R6SGame.applyEquipments(player, "blue");
				player.setGameMode(GameMode.SURVIVAL);
				player.setHealth(player.getMaxHealth());
				player.setFoodLevel(20);
				player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5, 5, false, false), false);
				if (isSwitched) {
					player.teleport(R6SConfig.getSpawnpoint("red"));
				} else {
					player.teleport(R6SConfig.getSpawnpoint("blue"));
				}
			}
		}
	}
}
