package plugin.R6S.api;

import java.util.Arrays;
import java.util.Objects;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import plugin.R6S.R6SPlugin;

public class R6SKit {
	static String[] kits = {
			"default",
			"sniper"
	};

	static Plugin r6s = R6SPlugin.getInstance();

	public static String getKit(Player player) {
		if (Objects.equals(Metadata.getMetadata(player, "kit"), null)) {
			return "default";
		} else if (Arrays.asList(kits).contains(Metadata.getMetadata(player, "kit").toString())) {
			return Metadata.getMetadata(player, "kit").toString();
		} else {
			return "default";
		}
	}

	public static ItemStack[] getKitContents(String kit, String team) {
		if (Objects.equals(team, "red") || Objects.equals(team, "blue") || Objects.equals(team, "white")) {
			if (Arrays.asList(kits).contains(kit)) {
				ItemStack[] kitcontents = Base64Item.itemFromStringList(Config.getGameConfigData("kit." + kit + "." + team).toString());
				return kitcontents;
			} else {
				ItemStack[] defaultkitcontents = Base64Item.itemFromStringList(Config.getGameConfigData("kit.default." + team).toString());
				r6s.getLogger().info("invalid kit name! : " + kit);
				return defaultkitcontents;
			}
		}
		r6s.getLogger().info("invalid team name! : " + team);
		return Base64Item.itemFromStringList(Config.getGameConfigData("kit.default.red").toString());
	}
}
