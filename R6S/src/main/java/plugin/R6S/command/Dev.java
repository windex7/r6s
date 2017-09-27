package plugin.R6S.command;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Base64Item;
import plugin.R6S.api.Metadata;
import plugin.R6S.api.NBT;

public class Dev implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) return false;
		Plugin r6s = R6SPlugin.getInstance();
		Player player = (Player)sender;
		String playeruuid = player.getUniqueId().toString();
		File playerconfig = new File(r6s.getDataFolder(), playeruuid + ".yml");
		FileConfiguration playerdata = YamlConfiguration.loadConfiguration(playerconfig);
		ItemStack[] invcontents = player.getInventory().getContents();
		FileConfiguration config = r6s.getConfig();
		File devfile = new File(r6s.getDataFolder(), "devfile.yml");
		FileConfiguration devconfig = YamlConfiguration.loadConfiguration(devfile);
		if (args.length >= 1) {
			switch (args[0]) {
			case "invsave":
				try {
					if (args.length <= 1) return false;
					if (args.length >= 3) {
						// --specify filepath. args[1]:filename, args[2]:path--
						File targetfile = new File(r6s.getDataFolder(), args[1] + ".yml");
						FileConfiguration targetconfig = YamlConfiguration.loadConfiguration(targetfile);
						targetconfig.set(args[2].toString(), Base64Item.itemToStringList(invcontents));
						targetconfig.save(targetfile);
						return true;
					}
					// --save inv to devfile.yml\inv.arg[1]--
					devconfig.set("inv." + args[1], Base64Item.itemToStringList(invcontents));
					devconfig.save(devfile);
					return true;
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not save to the target file.");
				}
				break;
			case "invload":
				try {
					if (args.length <= 1) return false;
					if (args.length >= 3) {
						// --specify filepath. args[1]:filename, args[2]:path--
						player.getInventory().setContents(Base64Item.itemFromStringList(YamlConfiguration.loadConfiguration(new File(r6s.getDataFolder(), args[1] + ".yml")).getString(args[2].toString())));
						return true;
					}
					// --load inv from devfile.yml\inv.arg[1]--
					player.getInventory().setContents(Base64Item.itemFromStringList(devconfig.getString("inv." + args[1])));
					return true;
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not load from the target file.");
				}
				break;
			case "readnbtstring":
				if (args.length <= 1) return false;
				player.sendMessage("key: " + args[1] + " , value: " + NBT.readItemTag(player.getInventory().getItemInMainHand(), args[1], "string"));
				return true;
			case "writenbtstring":
				if (args.length <= 2) return false;
				player.getInventory().setItemInMainHand(NBT.writeItemTag(player.getInventory().getItemInMainHand(), args[1], args[2], "string"));
				player.sendMessage("successfully wrote nbt to the item in your main hand! key: " + args[1] + " , value: " + args[2]);
				return true;
			case "writenbtint":
				if (args.length <= 2) return false;
				player.getInventory().setItemInMainHand(NBT.writeItemTag(player.getInventory().getItemInMainHand(), args[1], args[2], "int"));
				player.sendMessage("successfully wrote nbt to the item in your main hand! key: " + args[1] + " , value: " + args[2]);
				return true;
			case "writemetadata":
				if (args.length <= 3) return false;
				Player writetarget = r6s.getServer().getPlayer(args[1]);
				Metadata.setMetadata(writetarget, args[2], args[3]);
				return true;
			case "readmetadata":
				if (args.length <= 2) return false;
				Player readtarget = r6s.getServer().getPlayer(args[1]);
				String value = Metadata.getMetadata(readtarget, args[2]).toString();
				player.sendMessage("key: " + args[2] + ", value: " + value);
				return true;
			default:
				return false;
			}
	}
	return false;
	}

}
