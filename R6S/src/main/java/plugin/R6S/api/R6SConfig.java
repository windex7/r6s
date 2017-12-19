package plugin.R6S.api;

import org.bukkit.Location;

public class R6SConfig {
	static Location redspawn;
	static Location bluespawn;
	static Location specspawn;
	static Location stagepoint;

	public R6SConfig() {
		reload();
	}

	public static void reload() {
		redspawn = (Location) Config.getGameConfigData("spawnpoint.redspawn");
		bluespawn = (Location) Config.getGameConfigData("spawnpoint.bluespawn");
		specspawn = (Location) Config.getGameConfigData("spawnpoint.spectator");
		stagepoint = (Location) Config.getGameConfigData("stagepoint");
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
		case "stage":
			return stagepoint;
		default:
			return null;
		}
	}
}