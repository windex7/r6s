package plugin.R6S.listener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import plugin.R6S.R6SPlugin;
import plugin.R6S.api.Gun;
import plugin.R6S.api.Metadata;
import plugin.R6S.api.NBT;
import plugin.R6S.api.R6SKit;
import plugin.R6S.api.Timing;

public class SpecialItems implements Listener {
	static Plugin r6s = R6SPlugin.getInstance();
	public static List<Integer> grenades = new ArrayList<>();
	public static List<Integer> fraggrenades = new ArrayList<>();
	public static HashMap<String, Integer> fragdelay = new HashMap<>();
	public static HashMap<String, Location> c4location = new HashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public static void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		if (block.getType() == Material.SKULL) {
			Skull skull = (Skull) block.getState();
			String owner = skull.getOwner();
			//if (!(owner != null))
			//	return;
			List<String> tntblacklist = new ArrayList<String>() {
				{
					add("Zombie");
				}
			};
			//if (owner.equalsIgnoreCase("MHF_TNT2") {
			if (owner != null) {
				if (tntblacklist.contains(owner)) {
					return;
				}
			}
				ItemStack detonator = new ItemStack(Material.LEVER, 1);
				ItemMeta detonatormeta = detonator.getItemMeta();
				detonatormeta.setDisplayName("Detonator");
				detonatormeta.setLore(Arrays.asList(
						String.valueOf(new SimpleDateFormat("ddHHmmssSSS").format(Calendar.getInstance().getTime()))));
				detonator.setItemMeta(detonatormeta);
				SpecialItems.c4location.put(detonatormeta.getLore().toString(), block.getLocation());
				Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
					@Override
					public void run() {
						if (player.getInventory().getItemInMainHand() != null) {
							if (player.getInventory().getItemInOffHand() != null) {
								player.getInventory().addItem(detonator);
							} else {
								player.getInventory().setItemInOffHand(detonator);
							}
						} else {
							player.getInventory().setItemInMainHand(detonator);
						}
					}
				}, 2);
				return;
		}
	}

	@EventHandler
	public void onInteractBlock(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		String playeruuid = player.getUniqueId().toString();
		File playerconfig = new File(r6s.getDataFolder(), playeruuid + ".yml");
		FileConfiguration playerdata = YamlConfiguration.loadConfiguration(playerconfig);
		Block block = event.getClickedBlock();
		Action action = event.getAction();
		ItemStack item = event.getItem();
		// BlockFace blockface = event.getBlockFace();
		// --prior block-clicking event.--
		if (block != null) {
			switch (block.getType().toString()) {
			case "SKULL":
				// player.sendMessage("it's skull yeah");
				break;
			case "WORKBENCH":
				// --CRAFTING TABLE--
				// event.setCancelled(true);
				return;
			default:
				break;
			}
			onClickItem(event, player, item, action, block);
			return;
		} else {
			onClickItem(event, player, item, action, null);
			return;
		}
	}

	@SuppressWarnings("deprecation")
	public static void onClickItem(PlayerInteractEvent event, Player player, ItemStack item, Action action,
			Block block) {

		final int fragfuse = 80;

		if (item != null) {
			if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
				Object args[] = {"reload"};
				Gun.redirectGun(player, item, args);
			} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
				Object args[] = {"trigger"};
				Gun.redirectGun(player, item, args);
			}

			R6SKit.selectKit(player, item);

			if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
				switch (item.getType().toString()) {
				case "SLIME_BALL":
					if (item.getItemMeta().getDisplayName() != null) {
						if (item.getItemMeta().getDisplayName().equals("Frag Grenade")) {
							event.setCancelled(true);
							if (item.getItemMeta().getLore() != null)
								return;
							ItemMeta itemmeta = item.getItemMeta();
							itemmeta.setDisplayName("[pin removed]");
							itemmeta.setLore(Arrays.asList(String.valueOf(Timing.getTimeString())));
							item.setItemMeta(itemmeta);
							item.addUnsafeEnchantment(Enchantment.LUCK, 4);
							ItemStack nbtitem = NBT.writeItemTag(item, "bound", "true", "string");
							player.getInventory().setItemInMainHand(nbtitem);
							player.updateInventory();
							for (int i = 0; i < fragfuse; i++) {
								final int l = i;
								Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
									@Override
									public void run() {
										if (player.getInventory().contains(nbtitem)) {
											fragdelay.put(item.getItemMeta().getLore().toString(), l);
											player.setExp((1F * (float) l / (float) fragfuse));
										} else {
											player.setExp(0);
										}
									}
								}, i);
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
								@Override
								public void run() {
									if (player.getInventory().contains(nbtitem)) {
										player.getWorld().createExplosion(player.getLocation(), 4F);
										player.getInventory().remove(nbtitem);
										player.setExp(0);
										fragdelay.remove(item.getItemMeta().getLore().toString());
									}
								}
							}, fragfuse);
						}
					}
				default:
					return;
				}
			}
			if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
				switch (item.getType().toString()) {
				case "LEVER":
					if (item.getItemMeta().getDisplayName() != null) {
						if (item.getItemMeta().getDisplayName().equals("Detonator")) {
							event.setCancelled(true);
							String key = item.getItemMeta().getLore().toString();
							if (!(c4location.containsKey(key)))
								return;
							Location c4loc = c4location.get(key);
							if (c4loc.getBlock().getType() != Material.AIR) {
								player.getWorld().createExplosion(c4loc, 4F);
							}
							c4location.remove(item.getItemMeta().getLore().toString());
							c4loc.getBlock().setType(Material.AIR);
							player.getInventory().removeItem(item);
						}
					}
				case "SLIME_BALL":
					if (item.getItemMeta().getDisplayName() != null) {
						// if (item.getItemMeta().getDisplayName().equals("Frag
						// Grenade")) event.setCancelled(true);
						if (item.getItemMeta().getDisplayName().equals("[pin removed]")) {
							// event.setCancelled(true);
							Item grenade = player.getWorld().dropItem(player.getEyeLocation(),
									new ItemStack(Material.SLIME_BALL));
							NBT.writeEntityTag(grenade, "item", "PickupDelay", 32767, "int");
							grenade.setVelocity(player.getLocation().getDirection().multiply(0.9D));
							Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
								@Override
								public void run() {
									grenade.getWorld().createExplosion(grenade.getLocation(), 4F);
								}
							}, fragfuse - fragdelay.get(item.getItemMeta().getLore().toString()));
							Bukkit.getScheduler().scheduleSyncDelayedTask(r6s, new Runnable() {
								@Override
								public void run() {
									fragdelay.remove(item.getItemMeta().getLore().toString());
								}
							}, fragfuse);
							// EnderPearl fraggrenade =
							// player.launchProjectile(EnderPearl.class);
							// fraggrenade.setShooter(null);
							// fraggrenade.setVelocity(fraggrenade.getVelocity().multiply(0.4));
							// fraggrenades.add(fraggrenade.getEntityId());
							if (item.getAmount() == 1) {
								player.getInventory().removeItem(item);
							} else {
								item.setAmount(item.getAmount() - 1);
							}
							player.updateInventory();
						}
					}
				case "ENDER_PEARL":
					if (item.getItemMeta().getDisplayName() != null) {
						if (item.getItemMeta().getDisplayName().equals("Impact Grenade")) {
							String igkey = "impactgrenade";
							int grect = 60;
							if (Metadata.getMetadata(player, igkey) != null) {
								if (Timing.getTimeDiff(Long.parseLong(Metadata.getMetadata(player, igkey).toString())) <= grect) {
									event.setCancelled(true);
									return;
								}
							}
							event.setCancelled(true);
							EnderPearl grenade = player.launchProjectile(EnderPearl.class);
							grenade.setShooter(null);
							grenades.add(grenade.getEntityId());
							if (item.getAmount() == 1) {
								player.getInventory().removeItem(item);
							} else {
								item.setAmount(item.getAmount() - 1);
							}
							player.updateInventory();
							Metadata.setMetadata(player, igkey, Timing.getTime());
						}
					}
				case "GOLD_AXE":
					if (item.getItemMeta().getDisplayName() != null) {
						if (item.getItemMeta().getDisplayName().equals("Mjolnir")) {
							player.getWorld()
									.strikeLightning(player.getTargetBlock((HashSet<Byte>) null, 20).getLocation());
							item.setDurability((short) (item.getDurability() + 2));
							if (item.getDurability() >= 32) {
								player.getInventory().remove(item);
								player.updateInventory();
							}
						}
					}
				case "BLAZE_ROD":
					if (item.getItemMeta().getDisplayName() != null) {
						if (item.getItemMeta().getDisplayName().equals("Cornucopia")) {
							for (int i = 0; i < 3; i++) {
								int maxchoice = 2;
								int randomchoice = (int) (Math.random() * maxchoice);
								switch (randomchoice) {
								case 0:
									double bottlespeed = 0.3;
									ThrownExpBottle expbottle = player.launchProjectile(ThrownExpBottle.class);
									expbottle.setVelocity(expbottle.getVelocity().multiply(bottlespeed));
								case 1:
									Firework firework = (Firework) player.getWorld().spawnEntity(
											player.getTargetBlock((HashSet<Byte>) null, 100).getLocation(),
											EntityType.FIREWORK);
									FireworkMeta fireworkmeta = firework.getFireworkMeta();
									int maxtype = 5;
									int randomtype = (int) (Math.random() * maxtype);
									Type type = convertNumberToType(randomtype);
									int randomcolor1 = (int) (Math.random() * 17);
									int randomcolor2 = (int) (Math.random() * 17);
									Color color1 = convertNumberToColor(randomcolor1);
									Color color2 = convertNumberToColor(randomcolor2);
									int randompower = (int) ((Math.random() * 3) + 1);
									Random random = new Random();
									FireworkEffect effect = FireworkEffect.builder().flicker(random.nextBoolean())
											.withColor(color1).withFade(color2).with(type).trail(random.nextBoolean())
											.build();
									fireworkmeta.addEffect(effect);
									fireworkmeta.setPower(randompower);
									firework.setFireworkMeta(fireworkmeta);
								}
							}
						}
					}
				default:
					return;
				}
			}
		}
	}

	@EventHandler
	public static void onEntityShootBow(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			Arrow eventarrow = (Arrow) event.getProjectile();
			if (event.getBow().getItemMeta().getDisplayName() == null
					|| event.getBow().getItemMeta().getDisplayName().length() == 0
					|| !(event.getBow().getItemMeta() != null))
				return;
			switch (event.getBow().getItemMeta().getDisplayName()) {
			case "Wither Shot":
				int withernumber = 10;
				if (event.getForce() == 1.0) {
					for (int count = 0; count < withernumber; count++) {
						player.launchProjectile(WitherSkull.class);
						eventarrow.remove();
					}
				}
				return;
			case "Shotbow":
				// if (event.getForce() == 1.0) {
				Double maxrandom = 0.15;
				int arrownumber = 6;
				// float arrowforce = event.getForce();
				boolean isarrowcritical = eventarrow.isCritical();
				int arrowfiretick = eventarrow.getFireTicks();
				int arrowpunch = eventarrow.getKnockbackStrength();
				double arrowforce = eventarrow.getVelocity().length();
				Vector aim = eventarrow.getVelocity().normalize();
				for (int count = 0; count < arrownumber; count++) {
					Vector aimclone = aim.clone();
					Arrow arrow = player.launchProjectile(Arrow.class);
					Vector random = getRandomVector();
					Vector scatter = random.multiply(maxrandom);
					Vector arrowvector = aimclone.add(scatter).normalize().multiply(arrowforce);
					arrow.setVelocity(arrowvector);
					arrow.setFireTicks(arrowfiretick);
					arrow.setKnockbackStrength(arrowpunch);
					if (isarrowcritical) {
						arrow.setCritical(true);
					}
					NBT.writeEntityTag(arrow, "arrow", "pickup", 0, "int");
				}
				// }
				return;
			case "Hunter":
				eventarrow.setMetadata("Homing", new FixedMetadataValue(r6s, true));
				return;
			case "Crossbow":
				if (event.getForce() <= 0.7)
					return;
				double velocitymultiplier = 3;
				eventarrow.setVelocity(eventarrow.getVelocity().multiply(velocitymultiplier));
				eventarrow.setCritical(eventarrow.isCritical());
				return;
			default:
				return;
			}
		}
	}

	@EventHandler
	public static void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity defender = (LivingEntity) event.getEntity();
			if (event.getDamager() instanceof Snowball) {
				Snowball bullet = (Snowball)event.getDamager();
				if (Metadata.getMetadata((Entity)bullet, "gunname") != null) {
					Gun.hitBullet(defender, bullet);
					event.setCancelled(true);
				}
			}
		}
	}

	public static Vector getRandomVector() {
		Vector random1 = Vector.getRandom();
		Vector random2 = Vector.getRandom();
		Vector random1clone = random1.clone();
		Vector random = random1clone.subtract(random2);
		return random;
	}

	public static void setArrowHoming(Arrow arrow, Entity target) {
		if (arrow.hasMetadata("Homing")) {
			double arrowpower = arrow.getVelocity().length();
			Location arrowlocation = arrow.getLocation();
			Location targetlocation = target.getLocation();
			Vector arrowvector = arrowlocation.toVector().subtract(targetlocation.toVector());
			arrow.setVelocity(arrowvector.normalize().multiply(arrowpower));
		}
	}

	public static Type convertNumberToType(int type) {
		switch (type) {
		case 0:
			return Type.BALL;
		case 1:
			return Type.BALL_LARGE;
		case 2:
			return Type.BURST;
		case 3:
			return Type.CREEPER;
		case 4:
			return Type.STAR;
		default:
			return Type.CREEPER;
		}
	}

	public static Color convertNumberToColor(int colornumber) {
		switch (colornumber) {
		case 0:
			return Color.AQUA;
		case 1:
			return Color.BLACK;
		case 2:
			return Color.BLUE;
		case 3:
			return Color.FUCHSIA;
		case 4:
			return Color.GRAY;
		case 5:
			return Color.GREEN;
		case 6:
			return Color.LIME;
		case 7:
			return Color.MAROON;
		case 8:
			return Color.NAVY;
		case 9:
			return Color.OLIVE;
		case 10:
			return Color.ORANGE;
		case 11:
			return Color.PURPLE;
		case 12:
			return Color.RED;
		case 13:
			return Color.SILVER;
		case 14:
			return Color.TEAL;
		case 15:
			return Color.WHITE;
		case 16:
			return Color.YELLOW;
		default:
			return Color.LIME;
		}
	}

}
