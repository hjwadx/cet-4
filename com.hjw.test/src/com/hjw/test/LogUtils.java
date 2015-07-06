package com.hjw.test;

import android.util.Log;
import android.view.MotionEvent;

public class LogUtils {
	
	public static void show(MotionEvent ev, String className, String function) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			Log.e(className, function + "ACTION_UP");
			break;
		case MotionEvent.ACTION_DOWN:
			Log.e(className, function + "ACTION_DOWN");
			break;
		case MotionEvent.ACTION_CANCEL:
			Log.e(className, function + "ACTION_CANCEL");
			break;
		case MotionEvent.ACTION_MOVE:
			Log.e(className, function + "ACTION_MOVE");
			break;
		}
	}
	
}
	
	

