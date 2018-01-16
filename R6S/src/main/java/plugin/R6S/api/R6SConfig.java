package plugin.R6S.api;

import org.bukkit.Location;

public class R6SConfig {
	static Location redspawn;
	static Location bluespawn;
	static Location specspawn;
	static Location stage1point;

	public R6SConfig() {
		reload();
	}

	public static void reload() {
		redspawn = (Location) Config.getGameConfigData("spawnpoint.redspawn");
		bluespawn = (Location) Config.getGameConfigData("spawnpoint.bluespawn");
		specspawn = (Location) Config.getGameConfigData("spawnpoint.spectator");
		stage1point = (Location) Config.getGameConfigData("stagepoint.stage1");
	}

	public static Location getSpawnpoint(String name) {
		switch (name) {
		case "red":
			return redspawn;
		case "blue":
			return bluespawn;
		case "spec":
		case "spectate":
		case "spectator":
			return specspawn;
		default:
			return null;
		}
	}

	public static Location getWaypoint(String name) {
		switch (name) {
		case "stage1":
			return stage1point;
		default:
			return null;
		}
	}

	public static String getTag(String str) {
		switch (str) {
		case "offhand":
			return "offhand";
		default:
			return str;
		}
	}
}