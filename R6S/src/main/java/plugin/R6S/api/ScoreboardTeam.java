package plugin.R6S.api;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class ScoreboardTeam {
	public static void addEntry(String entry, String team) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Team targetteam = board.getTeam(team);
		if (!(targetteam.hasEntry(entry))) {
			targetteam.addEntry(entry);
		}
	}

	public static void removeEntry(String entry, String team) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Team targetteam = board.getTeam(team);
		if (targetteam.hasEntry(entry)) {
			targetteam.removeEntry(entry);
		}
	}

	public static void checkEntryTeam(String entry) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
	}
}
