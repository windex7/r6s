package plugin.R6S.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Glowing;
import plugin.R6S.api.Metadata;
import plugin.R6S.api.Teaming;

public class R6SCmd implements CommandExecutor{
	static Plugin r6s = R6SPlugin.getInstance();
	static HashMap<Location, Long> cameract = new HashMap<>();
	static List<Player> queue = new ArrayList<Player>();
	static int minstartnum = 2;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (StringUtils.equals(args[0], "teaming") && StringUtils.equals(args[1], "player")) {
			// case "player":
					Player target = r6s.getServer().getPlayer(args[2]);
					// Metadata.setMetadata(target, "team", args[3]);
					switch (args[3]) {
					case "red":
						Teaming.registerPlayerTeam(target, "red");
						break;
					case "blue":
						Teaming.registerPlayerTeam(target, "blue");
						break;
					default:
						return false;
					}
					return true;
		}
		if (sender instanceof BlockCommandSender) {
			BlockCommandSender commandblock = (BlockCommandSender)sender;
			Block cblock = commandblock.getBlock();
			Location location = cblock.getLocation();
			if (args.length == 0) return false;
			switch (args[0]) {
			case "camera":
				if (args.length <= 2) return false;
				for (Entity entity : cblock.getWorld().getNearbyEntities(location, 4, 4, 4)) {
					if (entity instanceof Player) {
						if (Metadata.getMetadata(entity, "stonebutton") != null) {
							if (Calendar.getInstance().getTimeInMillis() - (long)Metadata.getMetadata(entity, "stonebutton") <= 2000) { // 2sec
								Player player = (Player)entity;
								if (Metadata.getMetadata(player, "team") != null) {
									// String team = Metadata.getMetadata(player, "team").toString();
									String team = Teaming.getPlayerTeam(player);
									useSecurityCamera(player, team, Integer.parseInt(args[1]), Integer.parseInt(args[2]), location);
									return true;
								}
							}
						}
					}
				}
			case "joingame":
				if (args.length <= 2) return false;
				switch (args[1]) {
				case "random":
					int defaultradius = 5;
					int defaultdepth = 5;
					if (!(StringUtils.isEmpty(args[2]))) {
						defaultradius = Integer.parseInt(args[2]);
						if (!(StringUtils.isEmpty(args[3]))) {
							defaultdepth = Integer.parseInt(args[3]);
						}
					}
					// List<Player> playerlist = new ArrayList<Player>();
					for (Entity entity : cblock.getWorld().getNearbyEntities(location, defaultradius, defaultdepth, defaultradius)) {
						if (entity instanceof Player) {
							Player targetplayer = (Player) entity;
							if (targetplayer.getGameMode() == GameMode.ADVENTURE || targetplayer.getGameMode() == GameMode.SURVIVAL) {
								// playerlist.add(targetplayer);
								queue.add(targetplayer);
							}
						}
					}
					break;
				default:
					if (queue.size() >= minstartnum) {

					}
					break;
				}
				return true;
			}
		}
		return false;
	}

	public static void useSecurityCamera(Player player, String team, int y1, int y2, Location location) {
		int duration = 300; // 15sec
		if (StringUtils.isEmpty(team)) return;
		if (!(player != null)) return;

		long time = Calendar.getInstance().getTimeInMillis();
		long cooltime = 30000; // 30sec
		if (cameract.containsKey(location)) {
			if (time - cameract.get(location) <= cooltime) {
				long ctsec = (time - cameract.get(location)) / 1000;
				player.sendMessage(ChatColor.DARK_RED + "this security camera is still in cooldown! try again in" + String.valueOf(ctsec) + "sec!");
				return;
			} // else { // do below
			// }
		} else {
			cameract.put(location, time);
		}

		for (LivingEntity entity : player.getWorld().getLivingEntities()) {
			if (entity != null) {
				if (entity instanceof Player) {
					Player target = (Player)entity;
					if (target.getGameMode() == GameMode.CREATIVE || target.getGameMode() == GameMode.SPECTATOR) continue;
					if (!(Objects.equals(Teaming.getPlayerTeam(target), team))) {
						checkTargetInCamera(target, y1, y2, duration);
					}
				}
			}
		}
	}

	public static void checkTargetInCamera(Player player, int y1, int y2, int duration) {
		if (duration <= 0) return;
		int interval = 20; // duration(300) must be able to be devided by interval(20)
		int remainlength = duration - interval;
		double y = player.getLocation().getY();
		if ((y1 >= y && y >= y2) || (y1 <= y && y <= y2)) {
			Glowing.setPlayerGlowing(player, interval * 3);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
			@Override
			public void run() {
				checkTargetInCamera(player, y1, y2, remainlength);
			}
		}, interval);
		return;
	}
}
