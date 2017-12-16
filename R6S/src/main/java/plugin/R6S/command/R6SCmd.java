package plugin.R6S.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Metadata;
import plugin.R6S.api.R6SCamera;
import plugin.R6S.api.R6SGame;
import plugin.R6S.api.Teaming;
import plugin.R6S.api.Timing;

public class R6SCmd implements CommandExecutor{
	static Plugin r6s = R6SPlugin.getInstance();

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
							long buttonpressed = (long)Metadata.getMetadata(entity, "stonebutton");
							if (Timing.getTimeDiff(buttonpressed) <= 2000) { // 2sec
								Player player = (Player)entity;
								if (Teaming.getPlayerTeam(player) != null) {
									String team = Teaming.getPlayerTeam(player);
									R6SCamera.useSecurityCamera(player, team, Integer.parseInt(args[1]), Integer.parseInt(args[2]), location);
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
								R6SGame.addQueue(targetplayer);
							}
						}
					}
					break;
				default:
					break;
				}
				return true;
			}
		}
		return false;
	}
}
