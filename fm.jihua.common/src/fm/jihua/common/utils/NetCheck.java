package fm.jihua.common.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class NetCheck {
	public static final Uri APN_URI = Uri.parse("content://telephony/carriers");
	public static final Uri CURRENT_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");

	public static String getCurrentAPNFromSetting(ContentResolver resolver) {
		Cursor cursor = null;
		try {
			cursor = resolver.query(CURRENT_APN_URI, null, null, null, null);
			String curApnId = null;
			String apnName1 = null;
			if (cursor != null && cursor.moveToFirst()) {
				curApnId = cursor.getString(cursor.getColumnIndex("_id"));
				apnName1 = cursor.getString(cursor.getColumnIndex("apn"));
			}
			cursor.close();
			Log.i(Const.TAG, "curApnId:" + curApnId + " apnName1:" + apnName1);
			// find apn name from apn list
			if (curApnId != null) {
				cursor = resolver.query(APN_URI, null, " _id = ?",
						new String[] { curApnId }, null);
				if (cursor != null && cursor.moveToFirst()) {
					String apnName = cursor.getString(cursor
							.getColumnIndex("apn"));
					return apnName;
				}
			}
		} catch (Exception e) {
			Log.e(Const.TAG, "NetCheck getCurrentAPNFromSetting Exception:"
					+ e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return null;
	}

	public static int updateCurrentAPN(ContentResolver resolver, String newAPN) {
		Cursor cursor = null;
		try {
			// get new apn id from list
			cursor = resolver.query(APN_URI, null, " apn = ? and current = 1",
					new String[] { newAPN.toLowerCase() }, null);
			String apnId = null;
			if (cursor != null && cursor.moveToFirst()) {
				apnId = cursor.getString(cursor.getColumnIndex("_id"));
			}
			cursor.close();
			Log.e("NetCheck updateCurrentAPN", "apnId:" + apnId);
			// set new apn id as chosen one
			if (apnId != null) {
				ContentValues values = new ContentValues();
				values.put("apn_id", apnId);
				resolver.update(CURRENT_APN_URI, values, null, null);
			} else {
				// apn id not found, return 0.
				return 0;
			}
		} catch (SQLException e) {
			Log.e("NetCheck updateCurrentAPN", e.getMessage());
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// update success
		return 1;
	}
	
	private static int addCmnetAPN(Context context) {
		ContentResolver cr = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put("name", "中国移动GPRS");
		cv.put("apn", "cmnet");
		cv.put("numeric", "46000");
		cv.put("mcc", "460");
		cv.put("mnc", "00");
		cv.put("mmsc", "");
		cv.put("current", 1);

		Cursor c = null;
		try {
			Uri newRow = cr.insert(APN_URI, cv);
			if (newRow != null) {
				c = cr.query(newRow, null, null, null, null);
				c.moveToFirst();
				String id = c.getString(c.getColumnIndex("_id"));
				return Integer.parseInt(id);// 返回新创建的cmwap接入点的id
			}
		} catch (SQLException e) {
			Log.d(Const.TAG, e.getMessage());
		}

		if (c != null)
			c.close();
		return 0;
	}


	public static boolean checkNetworkInfo(Context c) {
		boolean ret = false;
		ConnectivityManager conManager = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		boolean internet = conManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		String oldAPN = info.getExtraInfo();
		if (oldAPN == null) {
			oldAPN = "";
		}
		String oldSQLAPN = getCurrentAPNFromSetting(c.getContentResolver());
		if (oldSQLAPN == null) {
			oldSQLAPN = "";
		}
		Log.i(Const.TAG, "oldAPN:" + oldAPN + " oldSQLAPN:" + oldSQLAPN);
		if (internet == false || oldAPN.toLowerCase().contains("wap")
				|| oldSQLAPN.toLowerCase().contains("wap")) {
			String netAPN = "";
			if (oldAPN.toLowerCase().contains("wap")) {
				netAPN = oldAPN.toLowerCase().replace("wap", "net");
			}else if (oldSQLAPN.toLowerCase().contains("wap")) {
				netAPN = oldSQLAPN.toLowerCase().replace("wap", "net");
			}
			if (oldAPN.toLowerCase().contains("wap")
					&& oldSQLAPN.toLowerCase().contains("net")) {
				updateCurrentAPN(c.getContentResolver(), oldAPN.toLowerCase());
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (updateCurrentAPN(c.getContentResolver(), netAPN) == 0) {
				if (netAPN.equals("cmnet")) {
					addCmnetAPN(c);
					updateCurrentAPN(c.getContentResolver(), netAPN);
				}
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ret = true;
		}
		return ret;

	}
}