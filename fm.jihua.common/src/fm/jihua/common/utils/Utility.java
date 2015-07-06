package fm.jihua.common.utils;


import fm.jihua.common.App;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

public class Utility {
	public static int getScreenWidth(){
		Activity activity = App.getInstance().getTopActivity();
        if (activity != null) {
        	Display display = activity.getWindowManager().getDefaultDisplay();
        	DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }

        return 480;
	}
	
	public static int getScreenHeight() {
        Activity activity = App.getInstance().getTopActivity();
        if (activity != null) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 800;
    }
}
