package plugin.R6S.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;

import plugin.R6S.R6SPlugin;

@SuppressWarnings("deprecation")
public class R6SStage {
	static Plugin r6s = R6SPlugin.getInstance();

	public static void regenStage() {
		R6SStage.pasteSchematic("stage", R6SConfig.getWaypoint("stage"));
	}

	public static void pasteSchematic(String schematicname, Location loc) {
		WorldEditPlugin we = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
		File schematic = new File(r6s.getDataFolder(), schematicname + ".schematic");
		EditSession session = we.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(loc.getWorld()), 1000000);
		try {
			MCEditSchematicFormat.getFormat(schematic).load(schematic).paste(session, new Vector (loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), false);
			return;
		} catch (DataException | MaxChangedBlocksException | IOException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not load " + schematicname + ".schematic !");
		}
		resetBlockDecay();
	}

	public static void resetBlockDecay() {
		EpicGlass.clearAllDamage();
	}
}
