package plugin.R6S.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

@SuppressWarnings("deprecation")
public class ScoreboardTeam {
	public static void addPlayer(Player player, String team) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Team targetteam = board.getTeam(team);
		targetteam.addPlayer(player);
	}

	public static void removePlayer(Player player, String team) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Team targetteam = board.getTeam(team);
		targetteam.removePlayer(player);
	}

	public static void checkPlayerTeam(Player player) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
	}
}
