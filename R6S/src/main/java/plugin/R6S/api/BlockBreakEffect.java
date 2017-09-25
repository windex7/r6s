package plugin.R6S.api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_9_R2.CraftServer;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.EntityHuman;
import net.minecraft.server.v1_9_R2.PacketPlayOutBlockBreakAnimation;

public class BlockBreakEffect {
	public static void playBlockBreakEffect(Location location, int damage, Player player) {
		Block block = location.getBlock();
		PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(0, new BlockPosition(block.getX(), block.getY(), block.getZ()), damage);
		int dimension = ((CraftWorld) player.getWorld()).getHandle().dimension;
		((CraftServer) player.getServer()).getHandle().sendPacketNearby((EntityHuman)player, block.getX(), block.getY(), block.getZ(), 120, dimension, packet);
	}
}
