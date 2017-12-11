package plugin.R6S;

import java.io.File;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_9_R2.Item;
import plugin.R6S.api.DamageTick;
import plugin.R6S.command.Dev;
import plugin.R6S.command.R6S;
import plugin.R6S.listener.ChangePlayerAttributes;
import plugin.R6S.listener.ClickedCommandBlock;
import plugin.R6S.listener.DisableOffhand;
import plugin.R6S.listener.PreventCertainExplosion;
import plugin.R6S.listener.PreventHangingGlitch;
import plugin.R6S.listener.PreventInteractGlitch;
import plugin.R6S.listener.PreventInventoryGlitch;
import plugin.R6S.listener.PreventPlayerBreakBlock;
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
				new ClickedCommandBlock(),
				new DisableOffhand(),
				new PreventCertainExplosion(),
				new PreventHangingGlitch(),
				new PreventInteractGlitch(),
				new PreventInventoryGlitch(),
				new PreventPlayerBreakBlock(),
				new ProjectileEpicGlass(),
				new Rapeling(),
				new ReleasePlayerData(),
				new RemoveDamageTick(),
				new SpecialItems()
				);

		// register commands
		getCommand("dev").setExecutor(new Dev());
		getCommand("r6s").setExecutor(new R6S());

		// modify the number of stack size of specified items
		try {
			Item enderpearl = Item.d("ender_pearl");
			Item cobweb = Item.d("web");
			Item slimeball = Item.d("slime_ball");
			Item skull = Item.d("skull");
			Field field;
			field = Item.class.getDeclaredField("maxStackSize");
			field.setAccessible(true);
			field.setInt(enderpearl, 1);
			field.setInt(cobweb, 1);
			field.setInt(slimeball, 1);
			field.setInt(skull, 1);
			field.setAccessible(false);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}

		// remove all entities' damage tick every tick
		new BukkitRunnable() {
			@Override
			public void run() {
				DamageTick.removeDamageTickAllEntity();
			}
		}.runTaskTimer(instance, 20, 1);
		//Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
		//	@Override
		//	public void run() {
		//		DamageTick.removeDamageTickAllEntity();
		//	}
		//}, 1, 1);

		// success message
		getLogger().info("****Successfully enabled R6S plugin!****");
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

	public void registerEvents(Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
	}
}
