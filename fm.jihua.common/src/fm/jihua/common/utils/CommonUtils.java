package fm.jihua.common.utils;

import java.lang.reflect.Field;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

public class CommonUtils {
	public static int parseInt(String text){
		try {
			return (text == null || text.length() == 0) ? 0 :Integer.parseInt(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// only for course
	public static boolean notStrictEquals(String a, String b){
		if (a != null && b != null) {
			return a.equals(b);
		}else {
			if (a == b) {
				return true;
			}else if ((a == null && b != null && b.equals("")) || (a != null && a.equals("") && b == null)) {
				return true;
			}
			return false;
		}
	}
	
	public static int getObjectId(Object object){
		try {
			Field field = object.getClass().getField("id");
			return (Integer) field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getObjectValue(Object object, String param){
		try {
			Field field = object.getClass().getField(param);
			return (Integer) field.get(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int find(Object[] objects, Object obj){
		if (objects != null) {
			for (int i=0; i<objects.length; i++) {
				Object object = objects[i];
				if (ObjectUtils.nullSafeEquals(obj, object)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public static Object findById(List<?> objects, int id){
		if (objects != null) {
			for (int i=0; i<objects.size(); i++) {
				Object object = objects.get(i);
				if (ObjectUtils.nullSafeEquals(getObjectId(object), id)) {
					return object;
				}
			}
		}
		return null;
	}
	
	public static Object findByParam(List<?> objects, String paramName, Object value){
		if (objects != null) {
			for (int i=0; i<objects.size(); i++) {
				Object object = objects.get(i);
				if (ObjectUtils.nullSafeEquals(getObjectValue(object, paramName), value)) {
					return object;
				}
			}
		}
		return null;
	}
	
	public static int indexOf(Object[] array, Object element) {
		if (array == null) {
			return -1;
		}
		for (int i = 0; i < array.length; i++) {
			Object arrayEle = array[i];
			if (ObjectUtils.nullSafeEquals(arrayEle, element)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int indexOf(List<?> array, Object element) {
		if (array == null) {
			return -1;
		}
		for (int i = 0; i < array.size(); i++) {
			Object arrayEle = array.get(i);
			if (ObjectUtils.nullSafeEquals(arrayEle, element)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int indexOfById(List<?> objects, int id){
		if (objects != null) {
			for (int i=0; i<objects.size(); i++) {
				Object object = objects.get(i);
				if (ObjectUtils.nullSafeEquals(getObjectId(object), id)) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public static void createShortcut(Context context, Class<?> cls, int icon){
    	Intent shortcutIntent = new Intent(context, cls);
//    	shortcutIntent.setClassName("fm.jihua.kecheng", ".ui.activity.SplashActivity");
    	shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    	shortcutIntent.addFlags(Intent.Flag)
    	shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    	shortcutIntent.setAction(Intent.ACTION_MAIN);
    	
    	Intent addIntent = new Intent();
    	addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    	addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "课程格子");
//    	addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, ImageHlp.decodeResource(context.getResources(), R.drawable.ic_launcher));
    	addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, icon));
    	addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    	addIntent.putExtra("duplicate", false);  
    	context.sendBroadcast(addIntent);
    }
	
	public static boolean isShortcutAdded(Context context){  
		boolean isInstallShortcut = false;
		try {
			// 获取当前应用名称
		    String title = null;
		    try {
		        final PackageManager pm = context.getPackageManager();
		        title = pm.getApplicationLabel(
		                pm.getApplicationInfo(context.getPackageName(),
		                        PackageManager.GET_META_DATA)).toString();
		    } catch (Exception e) {
		    }
			final ContentResolver cr = context.getContentResolver();
			// 本人的2.2系统是”com.android.launcher2.settings”,网上见其他的为"com.android.launcher.settings"
			final String AUTHORITY = "com.android.launcher2.settings";
			final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
					+ "/favorites?notify=true");
			Cursor c = cr.query(CONTENT_URI,
					new String[] { "title", "iconResource" }, "title=?",
					new String[] { title }, null);// XXX表示应用名称。
			if (c != null && c.getCount() > 0) {
				isInstallShortcut = true;
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isInstallShortcut;
	}  
}
