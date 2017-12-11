package plugin.R6S.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Timing {
	public static long getTime() {
		return Calendar.getInstance().getTimeInMillis();
	}

	public static String getTimeString() {
		return new SimpleDateFormat("ddHHmmssSSS").format(getTime());
	}

	public static long getTimeDiff(long time) {
		long diff = getTime() - time;
		return diff;
	}
}
