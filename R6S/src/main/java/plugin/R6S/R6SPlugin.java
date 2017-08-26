package plugin.R6S;

import java.io.File;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class R6SPlugin extends JavaPlugin implements Listener{
	private static R6SPlugin instance;
	public R6SPlugin () {
		instance = this;
	}

	public static R6SPlugin getInstance() {
		return instance;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {

		// --check if datafolder and config.yml exist--
		try {
			if (!getDataFolder().exists()) {
				getLogger().info("datafolder is not found, creating...");
				getDataFolder().mkdirs();
			}
			if (!new File(getDataFolder(), "config.yml").exists()) {
				getLogger().info("config.yml is not found, creating...");
				saveDefaultConfig();
			}
			else {
				getLogger().info("config.yml is found, loading...");
			}
			File devfile = new File(getDataFolder(), "devfile.yml");
			if (!devfile.exists()) {
				getLogger().info("devfile.yml is not found, creating...");
				devfile.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
