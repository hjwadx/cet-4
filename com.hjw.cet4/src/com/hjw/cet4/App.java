package com.hjw.cet4;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.hjw.cet4.utils.BitmapCache;
import com.hjw.cet4.utils.CommonUtils;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.DatabaseHelper;
import com.hjw.cet4.utils.ExamDBHelper;
import com.hjw.cet4.utils.FileUtils;
import com.mozillaonline.providers.DownloadManager;

import fm.jihua.common.utils.Compatibility;

public class App extends fm.jihua.common.App{
	private List<Activity> activityList = new LinkedList<Activity>();
	private ExamDBHelper mExamDBHelper;
	private DatabaseHelper mDBHelper;
	private SQLiteDatabase mUserDB;
//
//	private ImageCache mImageCache;
//	private static final String IMAGE_CACHE_DIR = "thumbs";
//	private LruCache<String, Object> mMemoryCache;
//	private static final int DEFAULT_MEM_CACHE_SIZE = 100 * 1024 ; // 100K
	public static int mDisplayWidth;
	public static int mDisplayHeight;
	private ImageLoader mImageLoader;
	private RequestQueue mHttpQueue;
	private RequestQueue mImageHttpQueue;
	private DownloadManager mDownloadManager;
	
	protected SharedPreferences mSettings;
	PreferenceListener mPreferenceListener = getPreferenceListener();

	public App() {

	}

	public static App getInstance() {
		return (App) _app;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		if(Const.TEST){
			initTest();
		}
		initAtFirst();
		initFolders();
	}
	
	private void initTest() {
		if(Const.TEST){
			App.getInstance().setPayed(true);  //测试的时候打开
		}
	}

	private void initAtFirst() {
		mDisplayWidth = Compatibility.getWidth(((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay());
		mDisplayHeight = Compatibility.getHeight(((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay());
		
		readDatabase();
		// 没有必要分开不同的文件夹存储cache
		mHttpQueue = Volley.newRequestQueue(this);
		mImageHttpQueue = Volley.newRequestQueue(this);
		mImageLoader = new ImageLoader(mImageHttpQueue, new BitmapCache());
		mDownloadManager = new DownloadManager(getContentResolver(), getPackageName());
		
		mSettings.registerOnSharedPreferenceChangeListener(mPreferenceListener);
	}
	
	public SharedPreferences getDefaultPreferences(){
		return mSettings;
	}
	
	
	
	private void initFolders(){
		String path = Environment.getExternalStorageDirectory() + Const.AUDIO_DIR;
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String nomedia = Environment.getExternalStorageDirectory() + Const.ROOT_DIR + ".nomedia/";
		File nomedia_dir = new File(nomedia);
		if (!nomedia_dir.exists()) {
			nomedia_dir.mkdirs();
		}
	}

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
	
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
	
	private void readDatabase() {
		try {
			mDBHelper = new DatabaseHelper(this);
			mUserDB = mDBHelper.getWritableDatabase();
			mExamDBHelper = new ExamDBHelper(this);
			mExamDBHelper.getReadableDatabase(); // For copy database firsttime;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "App readDatabse Exception:" + e.getMessage());
		}
	}
	
	public ExamDBHelper getExamDBHelper() {
		return mExamDBHelper;
	}
	
	public SQLiteDatabase getUserDB() {
		return mUserDB;
	}
	
	public DatabaseHelper getDBHelper() {
		return mDBHelper;
	}
	
//	public RequestQueue getHttpQueue(){
//		return mHttpQueue;
//	}
	
//	static SoftReference<Bitmap> baseBgReference;
//	public static BitmapDrawable getBaseBackground(Resources resources){
//		Bitmap bmpBaseBg = BitmapFactory.decodeResource(resources, R.drawable.bg_repeat);
//		BitmapDrawable bd = new BitmapDrawable(resources, bmpBaseBg);
//		bd.setTileModeXY(TileMode.REPEAT , TileMode.REPEAT );
//		bd.setDither(true);
//		return bd;
//	}
	
	public void setCurrentValue(String configParam, int value){
		Editor editor = mSettings.edit();
		editor.putInt(configParam, value);
		commitEditor(editor);
	}
	
	public int getCurrentValue(String configParam){
		return mSettings.getInt(configParam, 0);
	}
	
	public void setSharedString(String configParam, String value){
		Editor editor = mSettings.edit();
		editor.putString(configParam, value);
		commitEditor(editor);
	}
	
	public String getSharedString(String configParam, String default_value){
		return mSettings.getString(configParam, default_value != null ? default_value : "");
	}
	
	public long getLastTimeUpdateNotify() {
		return mSettings.getLong(Const.PREFERENCE_LAST_TIME_NOTIFY, 0);
	}

	public void setLastTimeUpdateNotify(long time) {
		Editor editor = mSettings.edit();
		editor.putLong(Const.PREFERENCE_LAST_TIME_NOTIFY, time);
		commitEditor(editor);
	}
	
	public void addDonePaper(int id) {
		String donePaperString = getSharedString(Const.DONE_PAPER_LIST, null);
		if (!CommonUtils.isNullString(donePaperString)) {
			donePaperString += ";" + id;
		} else {
			donePaperString = "" + id;
		}
		setSharedString(Const.DONE_PAPER_LIST, donePaperString);
	}
	
	public List<String> getDonePaperList() {
		List<String> result = new ArrayList<String>();
		String donePaperString = getSharedString(Const.DONE_PAPER_LIST, null);
		if (!CommonUtils.isNullString(donePaperString)) {
			result.addAll(Arrays.asList(donePaperString.split(";")));
		}
		return result;
	}
	
	public void setSharedBoolean(String configParam, boolean value){
		Editor editor = mSettings.edit();
		editor.putBoolean(configParam, value);
		commitEditor(editor);
	}
	
	public boolean getSharedBoolean(String configParam){
		return mSettings.getBoolean(configParam, false);
	}
	
	public void setPayed(boolean payed){
		Editor editor = mSettings.edit();
		editor.putBoolean(Const.UPPER_LIMIT, payed);
		commitEditor(editor);
	}
	
	public boolean getPayed(){
		return mSettings.getBoolean(Const.UPPER_LIMIT, false);
	}
	
	public String getWeiboToken() {
		return mSettings.getString(Const.PREFERENCE_WEIBO_TOKEN, null);
	}
	
	public void setWeiboToken(String token) {
		Editor editor = mSettings.edit();
		editor.putString(Const.PREFERENCE_WEIBO_TOKEN, token);
		commitEditor(editor);
	}
	
	public String getWeiboExpires() {
		return mSettings.getString(Const.PREFERENCE_WEIBO_EXPIRES, null);
	}
	
	public void setWeiboExpires(String expires) {
		Editor editor = mSettings.edit();
		editor.putString(Const.PREFERENCE_WEIBO_EXPIRES, expires);
		commitEditor(editor);
	}
	
	public void logoutWeibo(){
		Editor editor = mSettings.edit();
		editor.remove(Const.PREFERENCE_WEIBO_TOKEN);
		editor.remove(Const.PREFERENCE_WEIBO_ID);
		editor.remove(Const.PREFERENCE_WEIBO_EXPIRES);
		commitEditor(editor);
	}
	
	public String getWeiboId() {
		return mSettings.getString(Const.PREFERENCE_WEIBO_ID, null);
	}

	public void setWeiboId(String weiboId) {
		Editor editor = mSettings.edit();
		editor.putString(Const.PREFERENCE_WEIBO_ID, weiboId);
		commitEditor(editor);
	}
	
	public void clearCache(){
		mHttpQueue.getCache().clear();
		mImageHttpQueue.getCache().clear();
	}
	
	public boolean clearSDcardFile(){
		String path = Environment.getExternalStorageDirectory() + Const.AUDIO_DIR;
		return FileUtils.getInstance().deleteFolder(path);
	}
	
	public RequestQueue getHttpQueue(){
		return mHttpQueue;
	}
	
	public ImageLoader getImageLoader(){
		return mImageLoader;
	}
	 
	public DownloadManager getDownloadManager(){
		return mDownloadManager;
	}
	
//	public ImageCache getImageCache() {
//		return mImageCache;
//	}
	
//	public LruCache<String, Object> getMemCache() {
//		return mMemoryCache;
//	}
	
	protected PreferenceListener getPreferenceListener(){
    	return new PreferenceListener();
    }
    
    /**
     * A listener for all the change in the preference file. It is used to maintain the global state of the application.
     */
    public class PreferenceListener implements SharedPreferences.OnSharedPreferenceChangeListener {

	/**
	 * Constructor.
	 */
	public PreferenceListener() {
	}

	@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
//			if (ACCOUNT_PASSWORD_KEY.equals(key)) {
//				mIsAccountConfigured = getMyUserId() != 0;
//			}
		}
	}
    
    
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void commitEditor(Editor editor){
		if (Compatibility.isCompatible(9)) {
			editor.apply();
		}else {
			editor.commit();
		}
	}

}
