package plugin.R6S.api;

import org.bukkit.Location;

public class R6SConfig {
	static Location hub;
	static Location stage1redspawn;
	static Location stage1bluespawn;
	static Location stage1specspawn;
	static Location stage1lobby;
	static Location stage1point;

	public R6SConfig() {
		reload();
	}

	public static void reload() {
		hub = (Location) Config.getGameConfigData("loc.hub");
		stage1redspawn = (Location) Config.getGameConfigData("spawnpoint.redspawn");
		stage1bluespawn = (Location) Config.getGameConfigData("spawnpoint.bluespawn");
		stage1specspawn = (Location) Config.getGameConfigData("spawnpoint.spectator");
		stage1lobby = (Location) Config.getGameConfigData("loc.stage1.lobby");
		stage1point = (Location) Config.getGameConfigData("loc.stage1.stagepoint");
	}

	//public static Location getSpawnpoint(String name) {
	//	switch (name) {
	//	case "red":
	//		return stage1redspawn;
	//	case "blue":
	//		return stage1bluespawn;
	//	case "spec":
	//	case "spectate":
	//	case "spectator":
	//		return stage1specspawn;
	//	default:
	//		return null;
	//	}
	//}

	public static Location getSpawnpoint(String point) {
		String stage = R6SGame.getStage();
		return getWaypoint(stage, point);
	}
	public static Location getWaypoint(String name, String waypoint) {
		switch (name) {
		case "hub":
			return hub;
		case "stage1":
			switch (waypoint) {
			case "stage":
				return stage1point;
			case "lobby":
				return stage1lobby;
			case "red":
				return stage1redspawn;
			case "blue":
				return stage1bluespawn;
			case "spec":
			case "spectate":
			case "spectator":
				return stage1specspawn;
			default:
				return null;
			}

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