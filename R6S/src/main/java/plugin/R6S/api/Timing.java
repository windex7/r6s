package plugin.R6S.api;

import java.util.Calendar;

public class Timing {
	static long currenttick;

	public static void resetTick() {
		currenttick = Calendar.getInstance().getTimeInMillis();
	}

	public static void tick() {
		currenttick++;
	}
	public static long getTime() {
		// return Calendar.getInstance().getTimeInMillis();
		return currenttick;
	}

	public static String getTimeString() {
		// return new SimpleDateFormat("ddHHmmssSSS").format(getTime());
		return String.valueOf(currenttick);
	}

	public static long getTimeDiff(long time) {
		long diff = getTime() - time;
		return diff;
	}
}
