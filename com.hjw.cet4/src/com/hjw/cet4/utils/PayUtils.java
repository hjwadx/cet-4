package com.hjw.cet4.utils;

import com.hjw.cet4.entities.Product;
import com.hjw.cet4.pay.AlipayTool;
import com.hjw.cet4.rest.contract.DataCallback;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Message;
import fm.jihua.common.ui.helper.UIUtil;

public class PayUtils {
	Product waitingToPayProduct;
	Product downloadProduct;
	Activity activity;
	public final static int MAX_WAIT_ALIPAY_TIME = 10000;
	long startTime;
	RefreshListener refreshListener;
	
	public enum CATEGORY{
		STICKER, THEME
	}
	
	public PayUtils(Activity activity, RefreshListener refreshListener) {
		this.activity = activity;
		this.refreshListener = refreshListener;
	}
	
	public interface RefreshListener{
		void onRefresh(boolean succeed);
	}
	
	class PayDataCallback implements DataCallback{

		@Override
		public void callback(Message msg) {
			boolean result = (Boolean) msg.obj;
			if (result) {
//				startTime = System.currentTimeMillis();
//				if (waitingToPayProduct != null) {
//					UIUtil.block(activity);
//				}
				refreshListener.onRefresh(true);
				MobclickAgent.onEvent(activity, "event_pay_succeed");
			}else {
				UIUtil.unblock(activity);
				refreshListener.onRefresh(false);
			}
		}
		
	}
	
	void saveMyProducts(){
		
	}
	
	public void downloadOrPay(Product product){
		MobclickAgent.onEvent(activity, "action_click_pay");
		downloadProduct = product;
		waitingToPayProduct = product;

		AlipayTool alipayTool = new AlipayTool(activity, new PayDataCallback());
		alipayTool.pay(downloadProduct, "", "");
	}
}
