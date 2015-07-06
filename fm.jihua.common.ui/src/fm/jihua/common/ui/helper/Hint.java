package fm.jihua.common.ui.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Hint {
	protected static final String TAG = "Hint";

	public static void debugAlert(Context cx, String msg) {
		Log.i(TAG, "Alert:" + msg);
		/*
		new AlertDialog.Builder(cx).setTitle("提示信息").setMessage(msg).setNegativeButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).create().show();
		*/
	}

	public static void showTipsShort(Context cx, String msg) {
//		App app = (App) cx.getApplicationContext();
//		if (app.getState() == Const.STATE_PAUSE)
//			return;
		Toast.makeText(cx, msg, Toast.LENGTH_SHORT).show();
	}

	public static void showTipsLong(Context cx, String msg) {
//		App app = (App) cx.getApplicationContext();
//		if (app.getState() == Const.STATE_PAUSE)
//			return;
		Toast.makeText(cx, msg, Toast.LENGTH_LONG).show();
	}
	
	public static void showTipsShort(Context cx, int res) {
//		App app = (App) cx.getApplicationContext();
//		if (app.getState() == Const.STATE_PAUSE)
//			return;
		Toast.makeText(cx, res, Toast.LENGTH_SHORT).show();
	}

	public static void showTipsLong(Context cx, int res) {
//		App app = (App) cx.getApplicationContext();
//		if (app.getState() == Const.STATE_PAUSE)
//			return;
		Toast.makeText(cx, res, Toast.LENGTH_LONG).show();
	}

//	public static void showNotify(Context cx, String msg) {
//		showNotify(cx, msg, Network.class);
//	}
//	
//	public static void showNotify(Context cx, String msg, Class<?> cls) {
//		App app = (App) cx.getApplicationContext();
//		if (app.getState() == Const.STATE_PAUSE || app.getAppState() == Const.STATE_SERVICE) {
//	        NotificationManager nm = (NotificationManager)cx.getSystemService(Context.NOTIFICATION_SERVICE);
//	        PendingIntent contentIntent = PendingIntent.getActivity(cx, 0, new Intent(cx, cls), PendingIntent.FLAG_ONE_SHOT);
//	        String message = "邻伴：" + msg;
//	        Notification notif = new Notification(R.drawable.icon, message, System.currentTimeMillis());
//	        notif.defaults = Notification.DEFAULT_SOUND;
//	        notif.setLatestEventInfo(cx, "邻伴", message, contentIntent);
//	        nm.notify(0, notif);			
//		} else {
//			showTipsLong(cx, msg);
//		}
//	}
}
