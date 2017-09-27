package plugin.R6S.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class ScoreboardTeam {
	static ScoreboardManager manager = Bukkit.getScoreboardManager();
	static Scoreboard board = manager.getMainScoreboard();

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
			ScoreboardTeam.removeEntry(target.getName(), "CounterTerrorist");
			ScoreboardTeam.addEntry(target.getName(), "Terrorist");
			break;
		case "blue":
			ScoreboardTeam.removeEntry(target.getName(), "Terrorist");
			ScoreboardTeam.addEntry(target.getName(), "CounterTerrorist");
			break;
		}
	}
}
