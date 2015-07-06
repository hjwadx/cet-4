package fm.jihua.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build;
import android.provider.Contacts;
import android.view.Display;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class Compatibility {

	// private static final String THIS_FILE = "Compat";
	private static int currentApi = 0;

	public static int getApiLevel() {

		if (currentApi > 0) {
			return currentApi;
		}

		if (android.os.Build.VERSION.SDK.equalsIgnoreCase("3")) {
			currentApi = 3;
		} else {
			try {
				Field f = android.os.Build.VERSION.class
						.getDeclaredField("SDK_INT");
				currentApi = (Integer) f.get(null);
			} catch (Exception e) {
				return 0;
			}
		}
		return currentApi;
	}

	public static boolean isCompatible(int apiLevel) {
		return getApiLevel() >= apiLevel;
	}

	@SuppressWarnings("unused")
	private static boolean needPspWorkaround() {
		// New api for 2.3 does not work on Incredible S
		if (android.os.Build.DEVICE.equalsIgnoreCase("vivo")) {
			return true;
		}

		// New API for android 2.3 should be able to manage this but do only for
		// honeycomb cause seems not correctly supported by all yet
		if (isCompatible(11)) {
			return false;
		}

		// All htc except....
		if (android.os.Build.PRODUCT.toLowerCase().startsWith("htc")
				|| android.os.Build.BRAND.toLowerCase().startsWith("htc")
				|| android.os.Build.PRODUCT.toLowerCase().equalsIgnoreCase(
						"inc") /* For Incredible */
				|| android.os.Build.DEVICE.equalsIgnoreCase("passion") /* N1 */) {
			if (android.os.Build.DEVICE.equalsIgnoreCase("hero") /* HTC HERO */
					|| android.os.Build.DEVICE.equalsIgnoreCase("magic") /*
																		 * Magic
																		 * Aka
																		 * Dev
																		 * G2
																		 */
					|| android.os.Build.DEVICE.equalsIgnoreCase("tatoo") /* Tatoo */
					|| android.os.Build.DEVICE.equalsIgnoreCase("dream") /*
																		 * Dream
																		 * Aka
																		 * Dev
																		 * G1
																		 */
					|| android.os.Build.DEVICE.equalsIgnoreCase("legend") /* Legend */

			) {
				return false;
			}

			// Older than 2.3 has no chance to have the new full perf wifi mode
			// working since does not exists
			if (!isCompatible(9)) {
				return true;
			} else {
				// N1 is fine with that
				if (android.os.Build.DEVICE.equalsIgnoreCase("passion")) {
					return false;
				}
				return true;
			}

		}
		// Dell streak
		if (android.os.Build.BRAND.toLowerCase().startsWith("dell")
				&& android.os.Build.DEVICE.equalsIgnoreCase("streak")) {
			return true;
		}
		// Motorola milestone 1 and 2 & motorola droid & defy
		if (android.os.Build.DEVICE.toLowerCase().contains("milestone2")
				|| android.os.Build.BOARD.toLowerCase().contains("sholes")
				|| android.os.Build.PRODUCT.toLowerCase().contains("sholes")
				|| android.os.Build.DEVICE.equalsIgnoreCase("olympus")
				|| android.os.Build.DEVICE.toLowerCase()
						.contains("umts_jordan")) {
			return true;
		}

		return false;
	}

	public static boolean shouldReturnCropData() {
		// Motorola milestone 1 and 2 & motorola droid & defy
		if (android.os.Build.DEVICE.toLowerCase().contains("milestone2")
				|| android.os.Build.DEVICE.toLowerCase().contains("milestone3")
				|| android.os.Build.BOARD.toLowerCase().contains("sholes")
				|| android.os.Build.PRODUCT.toLowerCase().contains("sholes")
				|| android.os.Build.DEVICE.equalsIgnoreCase("olympus")
				|| android.os.Build.DEVICE.toLowerCase()
						.contains("umts_jordan")) {
			return true;
		}
		return false;
	}

	public static boolean scaleUpIfNeeded4Black() {
		if (android.os.Build.DEVICE.toLowerCase().contains("mx2")) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private static boolean needToneWorkaround() {
		if (android.os.Build.PRODUCT.toLowerCase().startsWith("gt-i5800")
				|| android.os.Build.PRODUCT.toLowerCase()
						.startsWith("gt-i5801")) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unused")
	private static boolean needSGSWorkaround() {
		if (isCompatible(9)) {
			return false;
		}
		if (android.os.Build.DEVICE.toUpperCase().startsWith("GT-I9000")
				|| android.os.Build.DEVICE.toUpperCase().startsWith("GT-P1000")) {
			return true;
		}
		return false;
	}

	public static boolean useFlipAnimation() {
		if (android.os.Build.BRAND.equalsIgnoreCase("archos")
				&& android.os.Build.DEVICE.equalsIgnoreCase("g7a")) {
			return false;
		}
		return true;
	}

	public static List<ResolveInfo> getPossibleActivities(Context ctxt, Intent i) {
		PackageManager pm = ctxt.getPackageManager();
		try {
			return pm.queryIntentActivities(i,
					PackageManager.MATCH_DEFAULT_ONLY
							| PackageManager.GET_RESOLVED_FILTER);
		} catch (NullPointerException e) {
			return new ArrayList<ResolveInfo>();
		}
	}

	public static Intent getPriviledgedIntent(String number) {
		Intent i = new Intent("android.intent.action.CALL_PRIVILEGED");
		Builder b = new Uri.Builder();
		b.scheme("tel").appendPath(number);
		i.setData(b.build());
		return i;
	}

	private static List<ResolveInfo> callIntents = null;

	public final static List<ResolveInfo> getIntentsForCall(Context ctxt) {
		if (callIntents == null) {
			callIntents = getPossibleActivities(ctxt,
					getPriviledgedIntent("123"));
		}
		return callIntents;
	}

	public static boolean canResolveIntent(Context context, final Intent intent) {
		final PackageManager packageManager = context.getPackageManager();
		// final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	// private static Boolean canMakeGSMCall = null;
	private static Boolean canMakeSkypeCall = null;

	public static boolean canMakeSkypeCall(Context context) {
		if (canMakeSkypeCall == null) {
			try {
				PackageInfo skype = context.getPackageManager().getPackageInfo(
						"com.skype.raider", 0);
				if (skype != null) {
					canMakeSkypeCall = true;
				}
			} catch (NameNotFoundException e) {
				canMakeSkypeCall = false;
			}
		}
		return canMakeSkypeCall;
	}

	public static Intent getContactPhoneIntent() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		/*
		 * intent.setAction(Intent.ACTION_GET_CONTENT);
		 * intent.setType(Contacts.Phones.CONTENT_ITEM_TYPE);
		 */
		if (!isCompatible(5)) {
			intent.setData(Contacts.People.CONTENT_URI);
		} else {
			intent.setData(Uri.parse("content://com.android.contacts/contacts"));
		}

		return intent;

	}

	public static boolean isTabletScreen(Context ctxt) {
		boolean isTablet = false;
		if (!isCompatible(4)) {
			return false;
		}
		Configuration cfg = ctxt.getResources().getConfiguration();
		int screenLayoutVal = 0;
		try {
			Field f = Configuration.class.getDeclaredField("screenLayout");
			screenLayoutVal = (Integer) f.get(cfg);
		} catch (Exception e) {
			return false;
		}
		int screenLayout = (screenLayoutVal & 0xF);
		// 0xF = SCREENLAYOUT_SIZE_MASK but avoid 1.5 incompat doing that
		if (screenLayout == 0x3 || screenLayout == 0x4) {
			// 0x3 = SCREENLAYOUT_SIZE_LARGE but avoid 1.5 incompat doing that
			// 0x4 = SCREENLAYOUT_SIZE_XLARGE but avoid 1.5 incompat doing that
			isTablet = true;
		}

		return isTablet;
	}

	@TargetApi(13)
	public static Point getSize(Display display) {
		Point size = new Point();
		if (isCompatible(13)) {
			display.getSize(size);
		} else {
			size.x = display.getWidth();
			size.y = display.getHeight();
		}
		return size;
	}

	@TargetApi(13)
	public static int getWidth(Display display) {
		if (isCompatible(13)) {
			Point size = new Point();
			display.getSize(size);
			return size.x;
		} else {
			return display.getWidth();
		}
	}

	@TargetApi(13)
	public static int getHeight(Display display) {
		if (isCompatible(13)) {
			Point size = new Point();
			display.getSize(size);
			return size.y;
		} else {
			return display.getHeight();
		}
	}
	
	@TargetApi(16)
	public static void setBackground(View v, Drawable drawable){
		if (isCompatible(16)) {
			v.setBackground(drawable);
		}else {
			v.setBackgroundDrawable(drawable);
		}
	}
	
	@TargetApi(17)
	public static void setAlpha(ImageView v, int alpha) {
		if (isCompatible(17)) {
			v.setImageAlpha(alpha);
		} else {
			if (v != null)
				v.setAlpha(alpha);
		}
	}
	
	@TargetApi(8)
	public static int getXVelocity(VelocityTracker velocityTracker, int id) {
		if (isCompatible(8)) {
			return (int) velocityTracker.getXVelocity(id);
		} else {
			return (int) velocityTracker.getXVelocity();
		}
	}
	
	@TargetApi(8)
	public static int getYVelocity(VelocityTracker velocityTracker, int id) {
		if (isCompatible(8)) {
			return (int) velocityTracker.getYVelocity(id);
		} else {
			return (int) velocityTracker.getYVelocity();
		}
	}

	/**
	 * @param context
	 *            used to check the device version and DownloadManager
	 *            information
	 * @return true if the download manager is available
	 */
	public static boolean isDownloadManagerAvailable(Context context) {
		try {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				return false;
			}
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setClassName("com.android.providers.downloads.ui",
					"com.android.providers.downloads.ui.DownloadList");
			List<ResolveInfo> list = context.getPackageManager()
					.queryIntentActivities(intent,
							PackageManager.MATCH_DEFAULT_ONLY);
			return list.size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

}
