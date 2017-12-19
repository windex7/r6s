package plugin.R6S.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import plugin.R6S.R6SPlugin;

public class PlaySound {
	static Plugin r6s = R6SPlugin.getInstance();

	public static void play(Player player, Location loc, String soundname, float volume, float pitch, long delay, String mode) {
		if (delay > 0) {
			new BukkitRunnable() {
				public void run() {
					play(player, loc, soundname, volume, pitch, 0, mode);
				}
			}.runTaskLater(r6s, delay);
		}
		Sound sound = convertStringToSound(soundname);
		switch (mode) {
		case "player":
			player.playSound(loc, sound, volume, pitch);
			return;
		case "world":
			for (World world : Bukkit.getServer().getWorlds()) {
				world.playSound(loc, sound, volume, pitch);
			}
			return;
		default:
			return;
		}
	}

	public static Sound convertStringToSound(String soundname) {
		return Sound.valueOf(soundname);
	}

	public static void play(Player player, Location loc, String[] soundnames, float[] volumes, float[] pitchs, long[] delays, String mode) {
		if (soundnames.length == volumes.length && volumes.length == pitchs.length && pitchs.length == delays.length) {
			for (int i = 0; i < soundnames.length; i++) {
				play(player, loc, soundnames[i], volumes[i], pitchs[i], delays[i], mode);
			}
		}
	}
}
