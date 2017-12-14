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
	final static Team redteam = board.getTeam(red);
	final static Team blueteam = board.getTeam(blue);

	public static String getTeamName(String color) {
		switch (color) {
		case "red":
			return red;
		case "blue":
			return blue;
		case "white":
			return white;
		default:
			return null;
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
		switch (team) {
		case "red":
			Teaming.removeEntry(target.getName(), blue);
			Teaming.addEntry(target.getName(), red);
			break;
		case "blue":
			Teaming.removeEntry(target.getName(), red);
			Teaming.addEntry(target.getName(), blue);
			break;
		}
	}

	public static String getPlayerTeam(Player target) {
		if (redteam.hasEntry(target.getName())) {
			return "red";
		} else if (blueteam.hasEntry(target.getName())) {
			return "blue";
		} else {
			return "white";
		}
	}
}
