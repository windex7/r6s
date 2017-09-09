package plugin.R6S;

import java.io.File;
import java.lang.reflect.Field;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_9_R2.Item;
import plugin.R6S.command.Dev;
import plugin.R6S.listener.ChangePlayerAttributes;
import plugin.R6S.listener.PreventCertainExplosion;
import plugin.R6S.listener.PreventHangingGlitch;
import plugin.R6S.listener.ProjectileEpicGlass;
import plugin.R6S.listener.Rapeling;
import plugin.R6S.listener.ReleasePlayerData;
import plugin.R6S.listener.RemoveDamageTick;
import plugin.R6S.listener.SpecialItems;

public class R6SPlugin extends JavaPlugin implements Listener {
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		// check config files
		checkFiles();

		// register listners
		registerEvents(
				new ChangePlayerAttributes(),
				new PreventCertainExplosion(),
				new PreventHangingGlitch(),
				new ProjectileEpicGlass(),
				new Rapeling(),
				new ReleasePlayerData(),
				new RemoveDamageTick(),
				new SpecialItems()
				);

		// register commands
		getCommand("dev").setExecutor(new Dev());

		// modify the number of stack size of specified items
		setStackSize("enderpearl", 1);
		setStackSize("cobweb", 1);
		setStackSize("slimeball", 1);
		setStackSize("skull", 1);

		// success message
		getLogger().info("****Successfully enabled MZ3 plugin!****");
	}

	private static R6SPlugin instance;

	public R6SPlugin() {
		instance = this;
	}

	public static R6SPlugin getInstance() {
		return instance;
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
		if (StringUtils.isEmpty(itemname) || stacksize <= 0) return;
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

	public void registerEvents(Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
	}
}
