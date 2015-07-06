package fm.jihua.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeHelper {
	public static String getTime(long time, boolean showWeek) {
		long dif = System.currentTimeMillis() - time;
		if (dif < 0) {
			return getLaterTime(time, showWeek);
		} else {
			return getEarlyTime(time, showWeek);
		}
	}

	public static String getLaterTime(long time, boolean showWeek) {
		String strTime;
		long dif = time - System.currentTimeMillis();
		if (dif < 1000 * 60) {
			strTime = "马上";
		} else if (dif < 1000 * 60 * 60) {
			strTime = dif / 1000 / 60 + "分钟后";
		} else {
			Date dt = new Date(time);
			Date curDate = new Date();
			String timePattern = showWeek ? "(E) HH:mm" : " HH:mm";
			if (dt.getYear() == curDate.getYear()) {
				if (dt.getMonth() == curDate.getMonth()
						&& dt.getDate() == curDate.getDate()) {
					SimpleDateFormat sdf = new SimpleDateFormat("今天"
							+ timePattern);
					strTime = sdf.format(dt);
				} else if (dt.getMonth() == curDate.getMonth()
						&& dt.getDate() == curDate.getDate() + 1) {
					SimpleDateFormat sdf = new SimpleDateFormat("明天"
							+ timePattern);
					strTime = sdf.format(dt);
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日"
							+ timePattern);
					strTime = sdf.format(dt);
				}
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日"
						+ timePattern);
				strTime = sdf.format(dt);
			}
		}
		return strTime;
	}

	public static String getEarlyTime(long time) {
		return getEarlyTime(time, false);
	}

	public static String getEarlyTime(long time, boolean showWeek) {
		String strTime;
		long dif = System.currentTimeMillis() - time;
		if (dif < 1000 * 60) {
			strTime = "刚刚";
		} else if (dif < 1000 * 60 * 60) {
			strTime = dif / 1000 / 60 + "分钟前";
		} else {
			Date dt = new Date(time);
			Date curDate = new Date();
			String timePattern = showWeek ? " E HH:mm" : " HH:mm";
			if (dt.getYear() == curDate.getYear()) {
				if (dt.getMonth() == curDate.getMonth()
						&& dt.getDate() == curDate.getDate()) {
					SimpleDateFormat sdf = new SimpleDateFormat("今天"
							+ timePattern);
					strTime = sdf.format(dt);
				} else if (dt.getMonth() == curDate.getMonth()
						&& dt.getDate() == curDate.getDate() - 1) {
					SimpleDateFormat sdf = new SimpleDateFormat("昨天"
							+ timePattern);
					strTime = sdf.format(dt);
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日"
							+ timePattern);
					strTime = sdf.format(dt);
				}
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日"
						+ timePattern);
				strTime = sdf.format(dt);
			}
		}
		return strTime;
	}

	public static String getEarlyTimeWithOutMinute(long time, boolean showWeek) {
		String strTime;
		Date dt = new Date(time);
		Date curDate = new Date();
		String timePattern = showWeek ? " E HH:mm" : " HH:mm";
		if (dt.getYear() == curDate.getYear()) {
			if (dt.getMonth() == curDate.getMonth()
					&& dt.getDate() == curDate.getDate()) {
				SimpleDateFormat sdf = new SimpleDateFormat("今天" + timePattern);
				strTime = sdf.format(dt);
			} else if (dt.getMonth() == curDate.getMonth()
					&& dt.getDate() == curDate.getDate() - 1) {
				SimpleDateFormat sdf = new SimpleDateFormat("昨天" + timePattern);
				strTime = sdf.format(dt);
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日"
						+ timePattern);
				strTime = sdf.format(dt);
			}
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日"
					+ timePattern);
			strTime = sdf.format(dt);
		}
		return strTime;
	}

	public static String getWeekday(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("E", Locale.getDefault());
		return sdf.format(time);
	}
}
