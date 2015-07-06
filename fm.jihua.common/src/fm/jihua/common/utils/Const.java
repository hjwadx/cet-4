package fm.jihua.common.utils;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.util.Log;

public class Const {
	public static final String TAG = "CET4";
	public static final int TIMEOUT_CONNECTION = 60000;
	public static final int TIMEOUT_SOCKET = 60000;
	public static final int MAX_RETRY_TIME = 3;

	public static String getChannelName(Context context) {
		String name = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			ApplicationInfo appInfo = pm.getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);

			name = appInfo.metaData.getString("UMENG_CHANNEL");
			if (name == null || name.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e(Const.TAG, "Exception", e);
		}
		return name;
	}

	public static void logHeap() {
		// Log.d(TAG, getHeapLog());
	}

	public static String getHeapLog() {
		Double allocated = new Double(Debug.getNativeHeapAllocatedSize())
				/ new Double((1048576));
		Double available = new Double(Debug.getNativeHeapSize()) / 1048576.0;
		Double free = new Double(Debug.getNativeHeapFreeSize()) / 1048576.0;
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);

		String message = "debug. =================================";
		message += "\ndebug.heap native: allocated " + df.format(allocated)
				+ "MB of " + df.format(available) + "MB (" + df.format(free)
				+ "MB free)";
		message += "\ndebug.memory: allocated: "
				+ df.format(new Double(
						Runtime.getRuntime().totalMemory() / 1048576))
				+ "MB of "
				+ df.format(new Double(
						Runtime.getRuntime().maxMemory() / 1048576))
				+ "MB ("
				+ df.format(new Double(
						Runtime.getRuntime().freeMemory() / 1048576))
				+ "MB free)";
		return message;
	}

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);

			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	public static int getAppVersionCode(Context context) {
		int versionCode = 0;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);

			versionCode = pi.versionCode;
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionCode;
	}
}
