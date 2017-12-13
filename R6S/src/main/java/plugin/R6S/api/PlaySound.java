package plugin.R6S.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PlaySound {
	public static void play(Location loc, Sound sound, float volume, float pitch, Object[] args) {
		String mode = args[0].toString();
		switch (mode) {
		case "player":
			Player player = (Player) args[1];
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
}
