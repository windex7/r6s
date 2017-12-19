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

	public Config() throws IOException {
		if (!(r6s.getDataFolder().exists())) r6s.getDataFolder().mkdirs();
		r6s.saveConfig();
		if (!(devfile.exists())) devfile.createNewFile();
	}

	public static void setGameConfig(String key, Object data) {
		config.set(key, data);
		r6s.saveConfig();
	}

	public static Object getGameConfigData(String key) {
		if (config.get(key) != null) {
			return config.get(key);
		} else {
			return null;
		}
	}

	public static void setDevConfig(String key, Object data) {
		devconfig.set(key, data);
		try {
			devconfig.save(devfile);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	public static Object getDevConfigData(String key) {
		return getConfig("devfile", key);
	}

	public static File getConfigFile(String configname) {
		File configfile = new File(r6s.getDataFolder(), configname + ".yml");
		if (!(configfile.exists()))
			try {
				configfile.createNewFile();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		return configfile;
	}

	public static void setConfig(String configname, String key, Object data) {
		File targetfile = getConfigFile(configname);
		FileConfiguration targetconfig = YamlConfiguration.loadConfiguration(targetfile);
		targetconfig.set(key, data);
		try {
			targetconfig.save(targetfile);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return;
	}

	public static Object getConfig(String configname, String key) {
		File targetfile = getConfigFile(configname);
		FileConfiguration targetconfig = YamlConfiguration.loadConfiguration(targetfile);
		if (targetconfig.get(key) != null) {
			return targetconfig.get(key);
		} else {
			return null;
		}
	}

	public static File getPlayerFile(Player player) {
		return getConfigFile(player.getUniqueId().toString());
	}

	public static void setPlayerConfig(Player player, String key, Object data) {
		setConfig(player.getUniqueId().toString(), key, data);
	}

	public static Object getPlayerConfig(Player player, String key) {
		return getConfig(player.getUniqueId().toString(), key);
	}
}
