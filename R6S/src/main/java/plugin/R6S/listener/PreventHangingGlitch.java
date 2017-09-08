package plugin.R6S.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;

public class PreventHangingGlitch implements Listener{
	@EventHandler
	public static void onHangingBreak(HangingBreakEvent event) {
		if (event.getCause().equals(RemoveCause.PHYSICS) || event.getCause().equals(RemoveCause.OBSTRUCTION) || event.getCause().equals(RemoveCause.ENTITY)) return;
		event.setCancelled(true);
	}
}
