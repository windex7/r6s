package plugin.R6S;

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
}
