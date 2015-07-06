package fm.jihua.common;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

public class App extends Application {
	private List<Activity> activityList = new LinkedList<Activity>();
	protected static App _app = null; 
	
	public static App getInstance(){
		return _app;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		_app = this;
	}
	
	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	
	public void removeActivity(Activity activity){
		activityList.remove(activity);
	}
	
	public int getActivityCount(){
		return activityList.size();
	}
	
	public Activity getTopActivity(){
		if (activityList.size() > 0) {
			return activityList.get(activityList.size() - 1);
		}
		return null;
	}
	
	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}

}
