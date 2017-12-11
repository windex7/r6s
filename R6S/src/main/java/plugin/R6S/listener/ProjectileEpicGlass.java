package plugin.R6S.listener;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Metadata;

public class ProjectileEpicGlass implements Listener {
	Plugin r6s = R6SPlugin.getInstance();
	public static ArrayList<Material> breakableblocks = new ArrayList<Material>(
			Arrays.asList(Material.GLASS, Material.STAINED_GLASS, Material.STAINED_GLASS_PANE, Material.THIN_GLASS));

	final int arrowblockdamage = 5;
	final int snowballblockdamage = 3;
	final int blockdurability = 20;

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof EnderPearl) {
			int entityid = event.getEntity().getEntityId();
			if (SpecialItems.grenades.contains(entityid)) {
				SpecialItems.grenades.remove((Integer) entityid);
				event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 2.5F);
			}
			// if (OnInteractBlock.fraggrenades.contains(entityid)) {
			// OnInteractBlock.fraggrenades.remove((Integer) entityid);
			// //event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(),
			// 4F);
			// }
		}

		if (event.getEntity() instanceof Arrow) {
			// int entityid = event.getEntity().getEntityId();
			Arrow arrow = (Arrow) event.getEntity();
			if (!(arrow.getShooter() instanceof Player))
				return;
			Player shooter = (Player) arrow.getShooter();
			Block hitblock;
			BlockIterator iterator = new BlockIterator(arrow.getWorld(), arrow.getLocation().toVector(),
					arrow.getVelocity().normalize().multiply(0.3), 0.0D, 10);
			while (iterator.hasNext()) {
				hitblock = iterator.next();
				if (hitblock.getTypeId() != 0) {
					if (epicGlass(hitblock)) {
						cloneArrow(arrow, shooter);
						break;
					} else {
						if (PreventCertainExplosion.isBreakable(hitblock) == false) {
							continue;
						} else {
							if (hitblock.getType() == Material.TNT) {
								hitblock.setType(Material.AIR);
								Entity tnt = hitblock.getWorld().spawn(hitblock.getLocation(), TNTPrimed.class);
								((TNTPrimed) tnt).setFuseTicks(20);
								continue;
							}
							if (hitblock.hasMetadata("damage")) {
								for (MetadataValue meta : hitblock.getMetadata("damage")) {
									hitblock.setMetadata("damage",
											new FixedMetadataValue(r6s, meta.asInt() + arrowblockdamage));
									if (meta.asInt() >= blockdurability) {
										hitblock.removeMetadata("damage", r6s);
										hitblock.setType(Material.AIR);
										// arrow.remove();
										// cloneArrow(arrow, shooter);
									}
									break;
								}
							} else {
								hitblock.setMetadata("damage", new FixedMetadataValue(r6s, arrowblockdamage));
							}
							arrow.remove();
							break;
						}
					}
				}
			}
			// if (arrow.getLocation().getBlock().getTypeId() != 0) {
			// hitblock = arrow.getLocation().getBlock();
			// if (epicGlass(hitblock)) {
			// cloneArrow(arrow, shooter);
			// return;
			// }
			// }
			return;
		}

		if (event.getEntity() instanceof Snowball) {
			Snowball snowball = (Snowball) event.getEntity();
			if (!(snowball.getShooter() instanceof Player)) {
				return;
			}
			Player shooter = (Player) snowball.getShooter();
			// double blockmultiplier = 0.1;
			// Block hitblock = snowball.getLocation().add(snowball.getVelocity().normalize().multiply(blockmultiplier)).getBlock();
			// Block hitblock = snowball.getLocation().getBlock();
			Block hitblock;
			BlockIterator iterator = new BlockIterator(snowball.getWorld(), snowball.getLocation().toVector(),
					snowball.getVelocity().normalize().multiply(0.3), 0.0D, 10);
			while (iterator.hasNext()) {
				hitblock = iterator.next();
				if (hitblock.getTypeId() != 0) {
					if (epicGlass(hitblock)) {
						cloneSnowball(snowball, shooter);
						break;
					} else {
						if (PreventCertainExplosion.isBreakable(hitblock) == false) {
							continue;
						} else {
							if (hitblock.getType() == Material.TNT) {
								hitblock.setType(Material.AIR);
								Entity tnt = hitblock.getWorld().spawn(hitblock.getLocation(), TNTPrimed.class);
								((TNTPrimed) tnt).setFuseTicks(20);
								continue;
							}
							if (hitblock.hasMetadata("damage")) {
								for (MetadataValue meta : hitblock.getMetadata("damage")) {
									hitblock.setMetadata("damage",
											new FixedMetadataValue(r6s, meta.asInt() + snowballblockdamage));
									if (meta.asInt() >= blockdurability) {
										hitblock.removeMetadata("damage", r6s);
										hitblock.setType(Material.AIR);
										// arrow.remove();
										// cloneArrow(arrow, shooter);
									}
									break;
								}
							} else {
								hitblock.setMetadata("damage", new FixedMetadataValue(r6s, snowballblockdamage));
							}
							// snowball.remove();
							break;
						}
					}
				}
			}
			//while (iterator.hasNext()) {
			//	hitblock = iterator.next();
			//	if (hitblock.getTypeId() != 0) {
			//		if (epicGlass(hitblock)) {
			//			cloneSnowball(snowball, shooter);
			//			break;
			//		} else {
			//			if (PreventCertainExplosion.isBreakable(hitblock)) {
			//				if (hitblock.getType() == Material.TNT) {
			//					hitblock.setType(Material.AIR);
			//					Entity tnt = hitblock.getWorld().spawn(hitblock.getLocation(), TNTPrimed.class);
			//					((TNTPrimed) tnt).setFuseTicks(20);
			//				} else if (hitblock.hasMetadata("damage")) {
			//					for (MetadataValue meta : hitblock.getMetadata("damage")) {
			//						hitblock.setMetadata("damage",
			//								new FixedMetadataValue(r6s, meta.asInt() + snowballblockdamage));
			//						if (meta.asInt() >= blockdurability) {
			//							hitblock.removeMetadata("damage", r6s);
			//							hitblock.setType(Material.AIR);
			//						} else {
			//							hitblock.setMetadata("damage",
			//									new FixedMetadataValue(r6s, snowballblockdamage));
			//						}
			//					}
			//				}
			//			}
			//		}
			//	}
			//}
			return;
		}
	}

	@SuppressWarnings("deprecation")
	public static boolean epicGlass(Block block) {
		if (breakableblocks.contains(block.getType())) {
			if (block.getType() == Material.STAINED_GLASS_PANE) {
				if (block.getData() == 3)
					return false;
			}
			block.setType(Material.AIR);
			return true;
		} else
			return false;
	}

	public static void cloneArrow(Arrow arrow, Player shooter) {
		Arrow clonearrow = (Arrow) arrow.getWorld().spawnEntity(arrow.getLocation(), EntityType.ARROW);
		Double decayrate = 0.8;
		clonearrow.setVelocity(arrow.getVelocity().multiply(decayrate));
		clonearrow.setCritical(arrow.isCritical());
		clonearrow.setShooter(shooter);
		arrow.remove();
	}

	public static void cloneSnowball(Snowball snowball, Player shooter) {
		Snowball clonesnowball = (Snowball) snowball.getWorld().spawnEntity(snowball.getLocation(),
				EntityType.SNOWBALL);
		Double decayrate = 0.9;
		clonesnowball.setVelocity(snowball.getVelocity().multiply(decayrate));
		clonesnowball.setShooter(shooter);
		if (Metadata.getMetadata(snowball, "damage") != null) {
			Metadata.setMetadata(clonesnowball, "damage", Metadata.getMetadata(snowball, "damage"));
		}
		if (Metadata.getMetadata(snowball, "kb") != null) {
			Metadata.setMetadata(clonesnowball, "kb", Metadata.getMetadata(snowball, "kb"));
		}
		if (Metadata.getMetadata(snowball, "gunname") != null) {
			Metadata.setMetadata(clonesnowball, "gunname", Metadata.getMetadata(snowball, "gunname"));
		}
		snowball.remove();
	}
}
