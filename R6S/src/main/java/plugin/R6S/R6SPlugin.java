package plugin.R6S;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_9_R2.Item;

public class R6SPlugin extends JavaPlugin implements Listener {
	private static R6SPlugin instance;

	public R6SPlugin() {
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
		// check config files
		checkFiles();

		// register listners
		Bukkit.getPluginManager().registerEvents(new R6SListener(), this);

		// modify the number of stack size of specified items
		Map<String, Integer> stacksize = new HashMap<String, Integer>();
		stacksize.put("enderpearl", 1);
		stacksize.put("cobweb", 1);
		stacksize.put("slimeball", 1);
		stacksize.put("skull", 1);
		//stacksize.put("", 1);
		for(Map.Entry<String, Integer> entry : stacksize.entrySet()) {
			setStackSize(entry.getKey(), entry.getValue());
		}
	}

	public void checkFiles() {
		try {
			if (!getDataFolder().exists()) {
				getLogger().info("datafolder is not found, creating...");
				getDataFolder().mkdirs();
			}
			if (!new File(getDataFolder(), "config.yml").exists()) {
				getLogger().info("config.yml is not found, creating...");
				saveDefaultConfig();
			} else {
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

	public void setStackSize(String itemname, int stacksize) {
		try {
			Item item = Item.d(itemname);
			Field field;
			field = Item.class.getDeclaredField("maxStackSize");
			field.setAccessible(true);
			field.setInt(item, stacksize);
			field.setAccessible(false);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
}
