package plugin.R6S.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;

public class Config {
	static Plugin r6s = R6SPlugin.getInstance();
	static FileConfiguration config = r6s.getConfig();
	static File devfile = new File(r6s.getDataFolder(), "devfile.yml");
	static FileConfiguration devconfig = YamlConfiguration.loadConfiguration(devfile);

	public static void setGameConfig(String key, Object data) {
		config.set(key, data);
		r6s.saveConfig();
	}

	public static Object getGameConfigData(String key) {
		return config.get(key);
	}

	public static void setDevConfig(String key, Object data) {
		devconfig.set(key, data);
		try {
			devconfig.save(devfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object getDevConfigData(String key) {
		return devconfig.get(key);
	}

	public static void setConfig(String configname, String key, Object data) {
		File targetfile = new File(r6s.getDataFolder(), configname + ".yml");
		FileConfiguration targetconfig = YamlConfiguration.loadConfiguration(targetfile);
		targetconfig.set(key, data);
		try {
			targetconfig.save(targetfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	public static Object getConfig(String configname, String key) {
		File targetfile = new File(r6s.getDataFolder(), configname + ".yml");
		FileConfiguration targetconfig = YamlConfiguration.loadConfiguration(targetfile);
		return targetconfig.get(key);
	}

	public static File getPlayerFile(Player player) {
		return new File(r6s.getDataFolder(), player.getUniqueId() + ".uml");
	}

	public static FileConfiguration getPlayerConfig(Player player) {
		return YamlConfiguration.loadConfiguration(getPlayerFile(player));
	}
}
