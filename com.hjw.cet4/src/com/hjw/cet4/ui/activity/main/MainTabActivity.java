package com.hjw.cet4.ui.activity.main;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Product;
import com.hjw.cet4.ui.activity.BaseActivity;
import com.hjw.cet4.ui.activity.MyTabFactory;
import com.hjw.cet4.utils.CommonUtils;
import com.hjw.cet4.utils.Const;
import com.hjw.cet4.utils.PayUtils;
import com.hjw.cet4.utils.UpperLimitHelper;
import com.hjw.cet4.utils.PayUtils.RefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.NotificationType;
import com.umeng.fb.UMFeedbackService;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

import fm.jihua.common.ui.helper.Hint;

public class MainTabActivity extends BaseActivity implements RefreshListener{
	
	
	private static MainTabActivity mainTabActivity;
	private AlphaAnimation alphaAnimation;
	public TabHost mTabHost;
	int old_position;
	int toolbar_icon[] = { R.drawable.tabbar_icon_specialized, R.drawable.tabbar_icon_mock, R.drawable.tabbar_icon_strategy, R.drawable.tabbar_icon_more};
	int toolbar_icon_pressed[] = { R.drawable.tabbar_icon_specialized_selected, R.drawable.tabbar_icon_mock_selected, R.drawable.tabbar_icon_strategy_selected, R.drawable.tabbar_icon_more_selected};
	

	private FrameLayout content_container;
	private FragmentManager mFM = null;
	List<Fragment> fList = new ArrayList<Fragment>();
	PayUtils mPayUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainTabActivity = this;
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_maintab);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_back_button);
		old_position = 0;
		initUM();
		App app = (App)getApplication();
		app.addActivity(this);
		initTitlebar();
		init();
		setUpViews();
	    setUpListeners();
	    MobclickAgent.onEvent(this, "open_cet4");
	}
	
	private void initUM() {
		UmengUpdateAgent.setUpdateOnlyWifi(true);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.update(this);
		MobclickAgent.onError(this);
		MobclickAgent.updateOnlineConfig(this);
		UMFeedbackService.enableNewReplyNotification(this, NotificationType.AlertDialog);
		checkForNewVersion();
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
	}
	
	void checkForNewVersion(){
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				if (updateStatus == 2 && updateInfo != null) {
	        		if(updateInfo.hasUpdate){
		        		updateStatus = 0;
		        	}else {
						updateStatus = 1;
					}
				}
	            switch (updateStatus) {
	            case 0: // has update
	            	if (updateInfo != null) {
						App app = (App)getApplication();
						app.setSharedString(Const.PREFERENCE_NEWEST_VERSION_CODE, updateInfo.version);
						if (CommonUtils.isWifi(MainTabActivity.this) || (System.currentTimeMillis() - app.getLastTimeUpdateNotify() > Const.UPDATE_NOTIFY_INTERVAL)) {
							app.setLastTimeUpdateNotify(System.currentTimeMillis());
							UmengUpdateAgent.showUpdateDialog(MainTabActivity.this, updateInfo);
						}
					}
	                break;
	            }
			}
		});
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.update(this);
	}

	protected void onDestroy() {
		super.onDestroy();
		App app = (App)getApplication();
		app.removeActivity(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!App.getInstance().getPayed()){
			getKechengActionBar().setLeftText("" + UpperLimitHelper.getInstance().getRemaind());
		} else {
			getKechengActionBar().setLefttButtonGone();
		}
	}
	
	private void initTitlebar() {
		setTitle("");
		Drawable titleImg= getResources().getDrawable(R.drawable.titlebar_title);
		titleImg.setBounds(0, 0, titleImg.getMinimumWidth(), titleImg.getMinimumHeight());
		getKechengActionBar().getTitleView().setCompoundDrawables(titleImg, null, null, null);
		getKechengActionBar().setLefttButtonGone();
		getKechengActionBar().setRightButtonGone();
		if(App.getInstance().getPayed()){
			getKechengActionBar().setLefttButtonGone();
		} else {
			getKechengActionBar().setLeftText("" + UpperLimitHelper.getInstance().getRemaind());
			Drawable drawable= getResources().getDrawable(R.drawable.purchase_icon_locked);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			getKechengActionBar().getLeftTextButton().setCompoundDrawables(drawable, null, null, null);
			getKechengActionBar().getLeftTextButton().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mPayUtils == null){
						mPayUtils = new PayUtils(MainTabActivity.this, MainTabActivity.this);
					}
					mPayUtils.downloadOrPay(new Product("四级考霸", "四级考霸——单项", Const.PAY_PRICE));
				}
			});
		}
		
//		findViewById(android.R.id.tabhost).setBackgroundDrawable(App.getBaseBackground(getResources()));
	}
	
	public static MainTabActivity getInstance() {
		return mainTabActivity;
	}
	
	private void init() {
		content_container = (FrameLayout) findViewById(android.R.id.tabcontent);
		fList = getFragments();
		if (null == mFM)
			mFM = getSupportFragmentManager();
	}
	
	private List<Fragment> getFragments() {
		List<Fragment> fList = new ArrayList<Fragment>();

		UnitermFragment f1 = new UnitermFragment();
		PracticeFragment f2 = new PracticeFragment();
		StrategyFragment f3 = new StrategyFragment();
		SettingFragment f4 = new SettingFragment();
		fList.add(f1);
		fList.add(f2);
		fList.add(f3);
		fList.add(f4);

		return fList;
	}

	
	private void setUpViews() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		setIndicat("专项", R.drawable.tabbar_icon_specialized_selected);
		setIndicat("模考", R.drawable.tabbar_icon_mock);
		setIndicat("攻略", R.drawable.tabbar_icon_strategy);
		setIndicat("更多", R.drawable.tabbar_icon_more);
	}
	
	private void setIndicat(String paramString,int resourceId) {
		View localView = View.inflate(this, R.layout.view_tab, null);
		((TextView) localView.findViewById(R.id.tab_text)).setText(paramString);
		((ImageView) localView.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(resourceId));
		TabHost.TabSpec localTabSpec = this.mTabHost.newTabSpec(paramString)
				.setIndicator(localView).setContent(new MyTabFactory(this));
//				.setContent(new Intent(this, paramClass));
		
		this.mTabHost.addTab(localTabSpec);
	}
	
	private void setUpListeners() {
		this.alphaAnimation = new AlphaAnimation(0.0F, 1.0F);
		this.alphaAnimation.setDuration(1000L);
		this.mTabHost.startAnimation(this.alphaAnimation);
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				int position = mTabHost.getCurrentTab();
				changeFragment(position);
//				setTitle(tabId);
				if("专项".equals(tabId) && !App.getInstance().getPayed()){
					getKechengActionBar().getLeftTextButton().setVisibility(View.VISIBLE);
				} else {
					getKechengActionBar().setLefttButtonGone();
				}
				View mView = mTabHost.getTabWidget().getChildAt(position);
				((ImageView)mView.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(toolbar_icon_pressed[position]));			
				mView = mTabHost.getTabWidget().getChildAt(old_position);
				((ImageView)mView.findViewById(R.id.tab_image)).setImageDrawable(getResources().getDrawable(toolbar_icon[old_position]));
				old_position = position;
			}
		});
		changeFragment(0);
	}
	
	private void changeFragment(int index){
		if (null == mFM)
			mFM = getSupportFragmentManager();
		FragmentTransaction ft = mFM.beginTransaction();
		ft.replace(android.R.id.tabcontent, fList.get(index));
		ft.commit();
	}

	@Override
	public void onRefresh(boolean succeed) {
		if(succeed){
			getKechengActionBar().setLefttButtonGone();
			App.getInstance().setPayed(true);
			Hint.showTipsShort(this, "支付成功");
		} else {
			Hint.showTipsShort(this, "支付失败");
		}
	}

}
