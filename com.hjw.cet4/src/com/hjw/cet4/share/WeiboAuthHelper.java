package com.hjw.cet4.share;

//import java.sql.Date;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//
//import com.hjw.cet4.App;
//import com.hjw.cet4.R;
//import com.hjw.cet4.utils.Const;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//import com.sina.weibo.sdk.auth.WeiboAuth;
//import com.sina.weibo.sdk.auth.WeiboAuthListener;
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.net.RequestListener;
//import com.sina.weibo.sdk.openapi.StatusesAPI;
//import com.sina.weibo.sdk.openapi.UsersAPI;
//import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
//
//import fm.jihua.common.ui.helper.Hint;
//import fm.jihua.common.ui.helper.UIUtil;
//import fm.jihua.common.utils.AppLogger;
//
//public class WeiboAuthHelper implements AuthHelper {
//
//    /**
//     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
//     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
//     * 选择赋予应用的功能。
//     * 
//     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
//     * 使用权限，高级权限需要进行申请。
//     * 
//     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
//     * 
//     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
//     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
//     * "email,direct_messages_read,direct_messages_write, friendships_groups_read,friendships_groups_write,statuses_to_me_read, follow_app_official_microblog, invitation_write";
//     */
//    public static final String SCOPE = 
//            "email,direct_messages_read,direct_messages_write,"
//            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
//            + "invitation_write";
//    
//    public static final int REQUEST_CODE_SSO_AUTH = 32973;
//
//	/** 微博 Web 授权类，提供登陆等功能  */
//    private WeiboAuth mWeiboAuth;
//	long mBeginAuthTime;
//	long mLastCompleteTime;
//	String uid;
//	int cursor = 0;
//
//
//	Activity mContext;
//	SNSCallback mSNSCallback;
//	Oauth2AccessToken accessToken;
//	SsoHandler ssoHandler;
//	List<SimpleUser> friends = new ArrayList<SimpleUser>();
//
//	public WeiboAuthHelper(Activity context) {
//		this.mContext = context;
//		// 创建微博实例
//        mWeiboAuth = new WeiboAuth(context, Const.WEIBO_CONSUMER_KEY, "http://kechengbiao.me", SCOPE);
//		App app = App.getInstance();
//		if (app.getWeiboToken() != null) {
//			accessToken = new Oauth2AccessToken(app.getWeiboToken(), app.getWeiboExpires());
//			if (!accessToken.isSessionValid()) {
//				accessToken = null;
//			}
//		}
//	}
//
//	public String getThirdPartId(){
//		App app = App.getInstance();
//		return app.getWeiboId();
//	}
//	public String getThirdPartToken(){
//		App app = App.getInstance();
//		return app.getWeiboToken();
//	}
//
////	public void setSNSCallback(SNSCallback callback){
////		mSNSCallback = callback;
////	}
//
//	/**
//	 * 内部会对当前Activity进行block和unblock
//	 * 失败之后有基本的Toast提示，可以根据SNSCallback的getNeedAuthHelperProcessMessage()来控制是否显示
//	 */
//	public void auth(SNSCallback callback){
//		if (!isAuthed()) {
//			UIUtil.block(mContext);
//			ssoHandler =new SsoHandler(mContext,mWeiboAuth);
//			ssoHandler.authorize( new AuthDialogListener(callback));
//		}else {
//			callback.onComplete(WeiboAuthHelper.this, null);
//		}
//	}
//
//	public void unBind(){
//		App app = App.getInstance();
//		app.logoutWeibo();
//	}
//
//	@Override
//	public boolean isAuthed() {
//		App app = App.getInstance();
//		return accessToken != null && app.getWeiboId() != null;
//	}
//
//	/**
//	 * 内部不会对当前Activity进行block和unblock，需要自行处理
//	 */
//	public void update(String status, SNSCallback callback){
//		StatusesAPI api = new StatusesAPI(accessToken);
//		api.update(status, null, null, new MyRequestListener(SNSCallback.UPLOAD, callback));
//    }
//
//	/**
//	 * 内部不会对当前Activity进行block和unblock，需要自行处理
//	 */
//	public void upload(String file, String status, SNSCallback callback){
//		StatusesAPI api = new StatusesAPI(accessToken);
//        Bitmap bitmap = BitmapFactory.decodeFile(file);
//        api.upload(status, bitmap, null, null, new MyRequestListener(SNSCallback.UPLOAD, callback));
//    }
//
//	public int getType(){
//		return Const.WEIBO;
//	}
//
//	public String getTypeName(){
//		return "新浪微博";
//	}
//
//	/**
//	 * 内部不会对当前Activity进行block和unblock，需要自行处理
//	 */
//	public void getFriends(int cursor, int limit, SNSCallback callback) {
//		// TODO 暂时没有用到
//	}
//
//	/**
//	 * 内部不会对当前Activity进行block和unblock，需要自行处理
//	 * 失败之后有基本的Toast提示，可以根据SNSCallback的getNeedAuthHelperProcessMessage()来控制是否显示
//	 */
//	public void getAllFriends(SNSCallback callback) {
//		FriendshipsAPI api = new FriendshipsAPI(accessToken);
//		App app = App.getInstance();
//		api.friends(Long.parseLong(app.getWeiboId()), Const.WEIBO_COUNT_PER_REQUEST, cursor, true, new MyRequestListener(SNSCallback.GET_ALL_FRIENDS_INFO, callback));
//	}
//
//	public void followGezi(){
//		FriendshipsAPI api = new FriendshipsAPI(accessToken);
//		api.create(Long.parseLong(Const.WEIBO_GEZI_ID), null, null);
//	}
//
//
//	class AuthDialogListener implements WeiboAuthListener {
//		private SNSCallback mCallback;
//
//		public AuthDialogListener(SNSCallback callback) {
//			this.mCallback = callback;
//		}
//
//		@Override
//		public void onComplete(Bundle values) {
//
//
//			String token = values.getString("access_token");
//			uid = values.getString("uid");
//			App app = App.getInstance();
//			app.setWeiboToken(token);
//			app.setWeiboId(uid);
//			String expires_in = values.getString("expires_in");
//			app.setWeiboExpires(expires_in);
//			accessToken = new Oauth2AccessToken(token, expires_in);
//			accessToken.setExpiresIn(expires_in);
////			String forceFollow = MobclickAgent.getConfigParams(mContext, "force_follow_weibo");
////			if (forceFollow.equals("1")) {
////				followGezi();
////			}
//			UIUtil.unblock(mContext);
//			mCallback.onComplete(WeiboAuthHelper.this, null);
//		}
//
//		@Override
//		public void onCancel() {
//			UIUtil.unblock(mContext);
//			mCallback.onError(WeiboAuthHelper.this);
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			UIUtil.unblock(mContext);
//			mCallback.onError(WeiboAuthHelper.this);
//			if (mCallback.getNeedAuthHelperProcessMessage()) {
//				showTips(String.format(mContext.getString(R.string.weibo_auth_error), e.getMessage()));
//			}
//		}
//
//	}
//
////	class MyDataCallback implements DataCallback{
////		@Override
////		public void callback(Message msg) {
////			UIUtil.unblock(mContext);
////			if (msg.what == CommonDataAdapter.MESSAGE_UPDATE_OAUTH) {
////				BaseResult result = (BaseResult)msg.obj;
////				if (result != null) {
////					if (result.success) {
////						mSNSCallback.onComplete(WeiboAuthHelper.this, null);
////					}else {
////						mSNSCallback.onError(WeiboAuthHelper.this);
////						if(mSNSCallback.getNeedAuthHelperProcessMessage()){
////							showTips(result.notice);
////						}
////					}
////				}else {
////					mSNSCallback.onError(WeiboAuthHelper.this);
////					if(mSNSCallback.getNeedAuthHelperProcessMessage()){
////						showTips(R.string.weibo_bind_error);
////					}
////				}
////			}
////		}
////	}
//	
//	void showTips(final int res){
//		mContext.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				Hint.showTipsShort(mContext, res);
//			}
//		});
//	}
//
//	void showTips(final String tips){
//		mContext.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				Hint.showTipsShort(mContext, tips);
//			}
//		});
//	}
//
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        /**
//         * 下面两个注释掉的代码，仅当sdk支持sso时有效，
//         */
//        if(ssoHandler!=null){
//        	ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
//    }
//
//    /**
//	 * 内部不会对当前Activity进行block和unblock，需要自行处理
//	 */
//	@Override
//	public void getUserInfo(SNSCallback callback) {
//		App app = App.getInstance();
//		UsersAPI api = new UsersAPI(accessToken);
//		api.show(Long.parseLong(app.getWeiboId()), new MyRequestListener(SNSCallback.GET_USER_INFO, callback));
//	}
//
//	/**
//	 * 内部会对当前Activity进行block和unblock
//	 * 失败之后有基本的Toast提示，可以根据SNSCallback的getNeedAuthHelperProcessMessage()来控制是否显示
//	 */
//	@Override
//	public void bind(SNSCallback callback) {
//		UIUtil.block(mContext);
//		mSNSCallback = callback;
//		getUserInfo(new MyCallback(SNSCallback.GET_USER_INFO));
//	}
//
//	private class MyRequestListener implements RequestListener{
//		private SNSCallback mCallback;
//		private int mScope = 0;
//
//		public MyRequestListener(int scope ,SNSCallback callback) {
//			mCallback = callback;
//			mScope = scope;
//		}
//
//		@Override
//		public void onComplete(final String response) {
//			mContext.runOnUiThread(new Runnable() {
//
//				@Override
//				public void run() {
//					switch (mScope) {
//					case SNSCallback.UPLOAD:
//						mCallback.onComplete(WeiboAuthHelper.this, null);
//						break;
//					case SNSCallback.GET_ALL_FRIENDS_INFO:
//						try {
//							JSONObject json;
//							json = new JSONObject(response);
//							JSONArray array = json.getJSONArray("users");
//							if (array != null) {
//								for (int i = 0; i < array.length(); i++) {
//									SimpleUser user = new SimpleUser();
//									JSONObject jsonObject = new JSONObject(array.get(i).toString());
//									user.id = jsonObject.getString("id");
//									user.name = jsonObject.getString("screen_name");
//									user.avatar = jsonObject.getString("avatar_large");
//									// user.sex =
//									// jsonObject.getString("gender").equals("m")?1:0;
//									friends.add(user);
//								}
//							}
//							if (Integer.parseInt(json.getString("next_cursor")) != 0) {
//								cursor = Integer.parseInt(json.getString("next_cursor"));
//								getAllFriends(mCallback);
//							} else if (mCallback != null) {
//								mCallback.onComplete(WeiboAuthHelper.this, friends);
//							}
//						} catch (Exception e) {
//							AppLogger.printStackTrace(e);
//							mCallback.onError(WeiboAuthHelper.this);
//							if (mCallback.getNeedAuthHelperProcessMessage()) {
//								showTips(R.string.get_friends_info_error);
//							}
//						}
//						break;
//					case SNSCallback.GET_USER_INFO:
//						try {
//							JSONObject json;
//							json = new JSONObject(response);
//							CommonUser user = new CommonUser();
//							user.avatar = json.getString("profile_image_url");
//							user.largeAvatar = json.getString("avatar_large");
//							user.name = json.getString("name");
//							user.id = json.getString("id");
//							user.gender = json.getString("gender").equals("m") ? Const.MALE : Const.FEMALE;
//							user.token = App.getInstance().getWeiboToken();
//							// JSONArray education =
//							// json.getJSONArray("education");
//							// SchoolInfo[] schoolInfos = new SchoolInfo[0];
//							// for (int i=0; i< education.length(); i++) {
//							// JSONObject schoolJSONInfo =
//							// education.getJSONObject(i);
//							// SchoolInfo schoolInfo = new SchoolInfo();
//							// schoolInfo.school =
//							// schoolJSONInfo.getString("name");
//							// schoolInfo.department =
//							// schoolJSONInfo.getString("department");
//							// schoolInfo.year = schoolJSONInfo.getInt("year");
//							// schoolInfos[i] = schoolInfo;
//							// }
//							// user.schoolInfos = schoolInfos;
//							mCallback.onComplete(WeiboAuthHelper.this, user);
//						} catch (Exception e) {
//							AppLogger.printStackTrace(e);
//							mCallback.onError(WeiboAuthHelper.this);
//						}
//						break;
//					}
//				}
//			});
//
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			AppLogger.printStackTrace(e);
//			if (mCallback != null) {
//				mContext.runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						mCallback.onError(WeiboAuthHelper.this);
//					}
//				});
//			}
//			
//		}
//	}
//
//	private class MyCallback implements SNSCallback {
//
//		private int mScope = 0;
//
//		public MyCallback(int scope) {
//            mScope = scope;
//        }
//
//		@Override
//		public void onComplete(AuthHelper authHelper, Object data) {
//			switch (mScope) {
//			case SNSCallback.GET_USER_INFO:
//				CommonUser user = (CommonUser)data;
//				App app = App.getInstance();
//				Date expire = new Date(System.currentTimeMillis() + Long.parseLong(app.getWeiboExpires())*1000);
//				break;
//			}
//		}
//
//		@Override
//		public void onError(AuthHelper authHelper) {
//			switch (mScope) {
//			case SNSCallback.GET_USER_INFO:
//				App app = App.getInstance();
//				Date expire = new Date(System.currentTimeMillis() + Long.parseLong(app.getWeiboExpires())*1000);
//				break;
//			}
//		}
//
//		@Override
//		public boolean getNeedAuthHelperProcessMessage() {
//			return true;
//		}
//	}
//
//	/**
//	 * 内部不会对当前Activity进行block和unblock，需要自行处理
//	 */
//	@Override
//	public void update(String title, String message, String url,
//			String imageUrl, String subtitle, String targetUrl,
//			SNSCallback callback) {
//		// TODO 暂时没用到
//	}
//	
//	@Override
//	public void uploadPictures(String title, ArrayList<HashMap<String, Object>> listItems, SNSCallback callback) {
//		// TODO Auto-generated method stub
//		
//	}
//}
