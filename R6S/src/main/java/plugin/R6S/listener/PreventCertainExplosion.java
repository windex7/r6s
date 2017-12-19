package plugin.R6S.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class PreventCertainExplosion implements Listener {
	public static boolean disableexplosion = true;
	public static ArrayList<Material> cannotexplodeblocks = new ArrayList<Material>(Arrays.asList(Material.BARRIER,
			Material.SNOW_BALL, Material.SNOW_BLOCK, Material.STAINED_CLAY, Material.PAINTING, Material.SEA_LANTERN,
			Material.SNOW, Material.STONE_BUTTON, Material.BEDROCK, Material.COMMAND, Material.WATER));

	public static void setExplosionDisabled(boolean isdisabled) {
		disableexplosion = isdisabled;
	}

	@SuppressWarnings({ "rawtypes" })
	@EventHandler
	public static void onBlockExplode(BlockExplodeEvent event) {
		if (disableexplosion)
			event.blockList().clear();
		Iterator iterator = event.blockList().iterator();
		while (iterator.hasNext()) {
			Block block = (Block) iterator.next();
			if (isBreakable(block) == false)
				iterator.remove();
		}
	}

	@SuppressWarnings({ "rawtypes" })
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (disableexplosion)
			event.blockList().clear();
		Iterator iterator = event.blockList().iterator();
		while (iterator.hasNext()) {
			Block block = (Block) iterator.next();
			if (isBreakable(block) == false) {
				iterator.remove();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean isBreakable(Block block) {
		if (cannotexplodeblocks.contains(block.getType())) {
			return false;
		} else {
			if (block.getType() == Material.STAINED_GLASS_PANE && block.getData() == 3)
				return false;
			if (block.getType() == Material.STONE && block.getData() == 0)
				return false;
			if (block.getType() == Material.STONE && block.getData() == 4)
				return false;
		}
		return true;
	}
}
