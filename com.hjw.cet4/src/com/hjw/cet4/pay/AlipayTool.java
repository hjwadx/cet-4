package com.hjw.cet4.pay;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alipay.android.appDemo4.AlixId;
import com.alipay.android.appDemo4.BaseHelper;
import com.alipay.android.appDemo4.MobileSecurePayHelper;
import com.alipay.android.appDemo4.MobileSecurePayer;
import com.alipay.android.appDemo4.ResultChecker;
import com.alipay.android.appDemo4.Rsa;
import com.hjw.cet4.R;
import com.hjw.cet4.entities.Product;
import com.hjw.cet4.rest.contract.DataCallback;

import fm.jihua.common.utils.AppLogger;

public class AlipayTool {
	private Activity context;
	private DataCallback dataCallback;
	private String TAG = "tag";
	private ProgressDialog mProgress = null;

	public AlipayTool(Activity context, DataCallback dbCallback) {

		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(context);
//		mspHelper.detectMobile_sp();
		this.context = context;
		this.dataCallback = dbCallback;
	}

	public void pay(Product product, String orderId, String notifyUrl) {
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(context);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist)
			return;

//		if (!checkInfo()) {
//			BaseHelper.showDialog(context, "提示", "缺少partner或者seller，请在src/com/alipay/android/appDemo4/PartnerConfig.java中增加。", R.drawable.infoicon);
//			return;
//		}
		try {
//			String orderInfo = getOrderInfo(product, orderId, URLEncoder.encode(notifyUrl));
			String orderInfo = getOrderInfo(product, getOutTradeNo(), "http://notify.java.jpxx.org/index.jsp");
			
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			Log.e("strsign", strsign + "++++++++" + orderInfo + "+++++++++" + signType);
			strsign = URLEncoder.encode(strsign);
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&" + getSignType();
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, context);
			if (bRet) {
				// show the progress bar to indicate that we have
				// started
				// paying.
				// 显示“正在支付”进度条
				closeProgress();
				mProgress = BaseHelper.showProgress(context, null, "正在支付", false, true);
			}
		} catch (Exception e) {
			Toast.makeText(context, R.string.remote_call_failed, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 * @return
	 */
	String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt();
		strKey = strKey.substring(0, 15);
		return strKey;
	}

	/**
	 * check some info.the partner,seller etc. 检测配置信息
	 * partnerid商户id，seller收款帐号不能为空
	 * 
	 * @return
	 */
//	private boolean checkInfo() {
//		String partner = PartnerConfig.PARTNER;
//		String seller = PartnerConfig.SELLER;
//		if (partner == null || partner.length() <= 0 || seller == null || seller.length() <= 0)
//			return false;
//
//		return true;
//	}

	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				Log.e(TAG, strRet); // strRet范例：resultStatus={9000};memo={};result={partner="2088201564809153"&seller="2088201564809153"&out_trade_no="050917083121576"&subject="123456"&body="2010新款NIKE 耐克902第三代板鞋 耐克男女鞋 386201 白红"&total_fee="0.01"&notify_url="http://notify.java.jpxx.org/index.jsp"&success="true"&sign_type="RSA"&sign="d9pdkfy75G997NiPS1yZoYNCmtRbdOP0usZIMmKCCMVqbSG1P44ohvqMYRztrB6ErgEecIiPj9UldV5nSy9CrBVjV54rBGoT6VSUF/ufjJeCSuL510JwaRpHtRPeURS1LXnSrbwtdkDOktXubQKnIMg2W0PreT1mRXDSaeEECzc="}
				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();

					BaseHelper.log(TAG, strRet);

					// 处理交易结果
					try {
						// 获取交易状态码，具体状态代码请参看文档
						String tradeStatus = "resultStatus={";
						int imemoStart = strRet.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = strRet.indexOf("};memo=");
						tradeStatus = strRet.substring(imemoStart, imemoEnd);

						// 先验签通知
						ResultChecker resultChecker = new ResultChecker(strRet);
						int retVal = resultChecker.checkSign();
						// 验签失败
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							// new AlipayTask(context).execute();
						} else {// 验签成功。验签成功后再判断交易状态码
							if (tradeStatus.equals("9000")) {// 判断交易状态码，只有9000表示交易成功
								// new AlipayTask(context).execute();
								dataCallback.callback(Message.obtain(null, 0, true));
							} else {
								mProgress.cancel();
								dataCallback.callback(Message.obtain(null, 0, false));
							}
						}
					} catch (Exception e) {
						AppLogger.printStackTrace(e);
						mProgress.cancel();
						dataCallback.callback(Message.obtain(null, 0, false));
					}
				}
					break;
				}
				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	String sign(String signType, String content) {
		Log.e("PartnerConfig.RSA_PRIVATE", PartnerConfig.RSA_PRIVATE + "++++++++++++");
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	String getOrderInfo(Product product, String orderId, String notifyUrl) {
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + orderId + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + product.subject + "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + product.body + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\"" + product.price + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\"" + notifyUrl + "\"";
		return strOrderInfo;
	}

	private void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * the OnCancelListener for lephone platform. lephone系统使用到的取消dialog监听
	 */
	public static class AlixOnCancelListener implements DialogInterface.OnCancelListener {
		Activity mcontext;

		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	/**
	 * 返回键监听事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BaseHelper.log(TAG, "onKeyDown back");
			context.finish();
			return true;
		}
		return false;
	}

}
