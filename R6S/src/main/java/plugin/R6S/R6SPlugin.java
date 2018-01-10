package plugin.R6S;

import java.io.IOException;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_9_R2.Item;
import plugin.R6S.api.Config;
import plugin.R6S.api.Damage;
import plugin.R6S.api.R6SConfig;
import plugin.R6S.api.ReleaseData;
import plugin.R6S.api.Scoping;
import plugin.R6S.api.Timing;
import plugin.R6S.command.DevCmd;
import plugin.R6S.command.R6SCmd;
import plugin.R6S.listener.ChangePlayerAttributes;
import plugin.R6S.listener.ClickedCommandBlock;
import plugin.R6S.listener.DisableOffhand;
import plugin.R6S.listener.InventoryListener;
import plugin.R6S.listener.PreventCertainExplosion;
import plugin.R6S.listener.PreventHangingGlitch;
import plugin.R6S.listener.PreventInteractGlitch;
import plugin.R6S.listener.PreventPlayerBreakBlock;
import plugin.R6S.listener.ProjectileEpicGlass;
import plugin.R6S.listener.Rapeling;
import plugin.R6S.listener.ReleasePlayerData;
import plugin.R6S.listener.RemoveDamageTick;
import plugin.R6S.listener.SneakScoping;
import plugin.R6S.listener.SpecialItems;

public class R6SPlugin extends JavaPlugin implements Listener {
	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		// check config files
		try {
			new Config();
			new R6SConfig();
		} catch (IOException e) {
			getLogger().info("failed to create config file!");
			e.printStackTrace();
		}

		// reset data
		ReleaseData.onEnabled();

		// register listners
		registerEvents(
				new ChangePlayerAttributes(),
				new ClickedCommandBlock(),
				new DisableOffhand(),
				new PreventCertainExplosion(),
				new PreventHangingGlitch(),
				new PreventInteractGlitch(),
				new InventoryListener(),
				new PreventPlayerBreakBlock(),
				new ProjectileEpicGlass(),
				new Rapeling(),
				new ReleasePlayerData(),
				new RemoveDamageTick(),
				new SneakScoping(),
				new SpecialItems()
				);

		// register commands
		getCommand("dev").setExecutor(new DevCmd());
		getCommand("r6s").setExecutor(new R6SCmd());

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

		Timing.resetTick();
		new BukkitRunnable() {
			@Override
			public void run() {
				Timing.tick();
				Damage.removeDamageTickAllEntity();
				Scoping.checkAllPlayerScoping();
				DisableOffhand.checkAllOffhand();
			}
		}.runTaskTimer(instance, 1, 1);
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

	public void registerEvents(Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
		}
	}
}
