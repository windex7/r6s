package plugin.R6S.api;

import java.util.HashMap;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;

public class R6SCamera {
	static Plugin r6s = R6SPlugin.getInstance();
	static HashMap<Location, Long> cameract = new HashMap<>();

	public static void useSecurityCamera(Player player, String team, int y1, int y2, Location location) {
		int duration = 300; // 15sec
		if (StringUtils.isEmpty(team)) return;
		if (!(player != null)) return;

		// long time = Calendar.getInstance().getTimeInMillis();
		long time = Timing.getTime();
		long cooltime = 600; // 30sec
		if (cameract.containsKey(location)) {
			if (time - cameract.get(location) <= cooltime) {
				long ctsec = (cooltime - (time - cameract.get(location))) / 20;
				player.sendMessage(ChatColor.DARK_RED + "this security camera is still in cooldown! try again in " + String.valueOf(ctsec) + "sec!");
				return;
			} // else { // do below
			// }
		} else {
			cameract.put(location, time);
		}

		for (LivingEntity entity : player.getWorld().getLivingEntities()) {
			if (entity != null) {
				if (entity instanceof Player) {
					Player target = (Player)entity;
					if (target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR) continue;
					if (!(Objects.equals(Teaming.getPlayerTeam(target), team))) {
						checkTargetInCamera(target, y1, y2, duration);
					}
				}
			}
		}
	}

	public static void checkTargetInCamera(Player player, int y1, int y2, int duration) {
		if (duration <= 0) return;
		int interval = 20; // duration(300) must be able to be devided by interval(20)
		int remainlength = duration - interval;
		double y = player.getLocation().getY();
		if ((y1 >= y && y >= y2) || (y1 <= y && y <= y2)) {
			Glowing.setPlayerGlowing(player, interval * 3);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
			@Override
			public void run() {
				checkTargetInCamera(player, y1, y2, remainlength);
			}
		}, interval);
		return;
	}
}
