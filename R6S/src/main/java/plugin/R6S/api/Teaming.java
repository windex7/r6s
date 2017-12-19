package plugin.R6S.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class Teaming {
	static ScoreboardManager manager = Bukkit.getScoreboardManager();
	static Scoreboard board = manager.getMainScoreboard();

	final static String red = "Terrorist";
	final static String blue = "CounterTerrorist";
	final static String white = "FFA";
	final static String defaultcolor = "nocollision";
	final static Team redteam = board.getTeam(red);
	final static Team blueteam = board.getTeam(blue);
	final static Team whiteteam = board.getTeam(white);
	final static Team defaultteam = board.getTeam(defaultcolor);

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

	public static void addEntry(String entry, String team) {
		Team targetteam = board.getTeam(team);
		if (!(targetteam.hasEntry(entry))) {
			targetteam.addEntry(entry);
		}
	}

	public static void removeEntry(String entry, String team) {
		Team targetteam = board.getTeam(team);
		if (targetteam.hasEntry(entry)) {
			targetteam.removeEntry(entry);
		}
	}

	public static void registerPlayerTeam(Player target, String team) {
		String targetname = target.getName();
		if (getPlayerTeam(target) != null) {
			removeEntry(targetname, getPlayerTeam(target));
		}
		switch (team) {
		case "red":
			addEntry(targetname, red);
			break;
		case "blue":
			addEntry(targetname, blue);
			break;
		case "white":
			addEntry(targetname, white);
			break;
		case "default":
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
}
