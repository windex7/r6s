package plugin.R6S.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Base64Item;
import plugin.R6S.api.Config;
import plugin.R6S.api.InventoryIO;
import plugin.R6S.api.Metadata;
import plugin.R6S.api.NBT;
import plugin.R6S.api.R6SConfig;
import plugin.R6S.api.R6SGame;
import plugin.R6S.api.R6SStage;
import plugin.R6S.listener.ManageExplosion;

public class DevCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Plugin r6s = R6SPlugin.getInstance();
		Player player = (Player) sender;
		if (args.length >= 1) {
			switch (args[0]) {
			case "invsave":
				try {
					if (args.length <= 1)
						return false;
					if (args.length == 4) {
						InventoryIO.savePlayerOpeningInventory(r6s.getServer().getPlayer(args[1]), args[2], args[3]);
						return true;
					}
					if (args.length == 3) {
						// --specify filepath. args[1]:filename, args[2]:path--
						//File targetfile = new File(r6s.getDataFolder(), args[1] + ".yml");
						//FileConfiguration targetconfig = YamlConfiguration.loadConfiguration(targetfile);
						//targetconfig.set(args[2].toString(), Base64Item.itemToStringList(invcontents));
						//targetconfig.save(targetfile);
						InventoryIO.savePlayerInventory(player, args[1], args[2]);
						return true;
					}
					// --save inv to devfile.yml\inv.arg[1]--
					//devconfig.set("inv." + args[1], Base64Item.itemToStringList(invcontents));
					//devconfig.save(devfile);
					return false;
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not save to the target file.");
				}
				break;
			case "itemsave":
				try {
					if (args.length <= 1)
						return false;
					if (args.length == 3) {
						Config.setConfig(args[1], args[2], Base64Item.itemToString(player.getInventory().getItemInMainHand()));
						return true;
					} else {
						Config.setGameConfig(args[1], Base64Item.itemToString(player.getInventory().getItemInMainHand()));
						return true;
					}
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not save to the target file.");
				}
				break;
			case "invbackup":
				InventoryIO.backupPlayerInventory(player);
				return true;
			case "invload":
				try {
					if (args.length <= 1)
						return false;
					if (args.length >= 3) {
						// --specify filepath. args[1]:filename, args[2]:path--
						//player.getInventory()
						//		.setContents(Base64Item.itemFromStringList(YamlConfiguration
						//				.loadConfiguration(new File(r6s.getDataFolder(), args[1] + ".yml"))
						//				.getString(args[2].toString())));
						InventoryIO.loadPlayerInventory(player, args[1], args[2]);
						return true;
					}
					// --load inv from devfile.yml\inv.arg[1]--
					//player.getInventory()
					//		.setContents(Base64Item.itemFromStringList(devconfig.getString("inv." + args[1])));
					//return true;
					return false;
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not load from the target file.");
				}
				break;
			case "invrollback":
				InventoryIO.rollbackPlayerInventory(player);
				return true;
			case "readnbtstring":
				if (args.length <= 1)
					return false;
				player.sendMessage("key: " + args[1] + " , value: "
						+ NBT.readItemTag(player.getInventory().getItemInMainHand(), args[1], "string"));
				return true;
			case "writenbtstring":
				if (args.length <= 2)
					return false;
				player.getInventory().setItemInMainHand(
						NBT.writeItemTag(player.getInventory().getItemInMainHand(), args[1], args[2], "string"));
				player.sendMessage("successfully wrote nbt to the item in your main hand! key: " + args[1]
						+ " , value: " + args[2]);
				return true;
			case "writenbtint":
				if (args.length <= 2)
					return false;
				player.getInventory().setItemInMainHand(
						NBT.writeItemTag(player.getInventory().getItemInMainHand(), args[1], args[2], "int"));
				player.sendMessage("successfully wrote nbt to the item in your main hand! key: " + args[1]
						+ " , value: " + args[2]);
				return true;
			case "writemetadata":
				if (args.length <= 3)
					return false;
				Player writetarget = r6s.getServer().getPlayer(args[1]);
				Metadata.setMetadata(writetarget, args[2], args[3]);
				return true;
			case "readmetadata":
				if (args.length <= 2)
					return false;
				Player readtarget = r6s.getServer().getPlayer(args[1]);
				String value = Metadata.getMetadata(readtarget, args[2]).toString();
				player.sendMessage("key: " + args[2] + ", value: " + value);
				return true;
			case "readconfig":
				if (args.length <= 2) return false;
				switch (args[1]) {
				case "config":
					String gameconfigdata = Config.getGameConfigData(args[2]).toString();
					player.sendMessage("key: " + args[2] + ", value: " + gameconfigdata);
					return true;
				case "dev":
				case "devfile":
					String devconfigdata;
					devconfigdata = Config.getDevConfigData(args[2]).toString();
					player.sendMessage("key: " + args[2] + ", value: " + devconfigdata);
					return true;
				default:
					String configdata;
					configdata = Config.getConfig(args[1], args[2]).toString();
					player.sendMessage("key: " + args[2] + ", value: " + configdata);
					return true;
				}
			case "writeconfig":
				if (args.length <= 3) return false;
				switch (args[1]) {
				case "config":
					Config.setGameConfig(args[2], args[3]);
					return true;
				case "dev":
				case "devfile":
					Config.setDevConfig(args[2], args[3]);
					return true;
				default:
					Config.setConfig(args[1], args[2], args[3]);
					return true;
				}
			case "writelocation":
				if (args.length <= 2) return false;
				switch (args[1]) {
				case "config":
					Config.setGameConfig(args[2], player.getLocation());
					return true;
				case "dev":
				case "devfile":
					Config.setDevConfig(args[2], player.getLocation());
					return true;
				default:
					Config.setConfig(args[1], args[2], player.getLocation());
					return true;
				}
			case "regenstage":
				R6SStage.pasteSchematic("stage", R6SConfig.getWaypoint(R6SGame.getStage(), "stage"));
				return true;
			case "reloadconfig":
				R6SConfig.reload();
				player.sendMessage("reloaded config data!");
				return true;
			case "preventexplosion":
				if (args.length <= 1) return false;
				ManageExplosion.setExplosionDisabled(Boolean.valueOf(args[1]));
				return true;
			case "repair":
				player.getInventory().getItemInMainHand().setDurability((short)0);
				return true;
			default:
				return false;
			}
		}
		return false;
	}

}
