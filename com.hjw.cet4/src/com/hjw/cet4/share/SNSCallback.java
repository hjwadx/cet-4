package com.hjw.cet4.share;



public interface SNSCallback {
	final int AUTH = 1;
	final int BIND = 2;
	final int UPLOAD = 3;
	final int GET_ALL_FRIENDS_INFO = 4;
	final int GET_USER_INFO = 5;

	public void onComplete(AuthHelper authHelper, Object data);
	public void onError(AuthHelper authHelper);
	public boolean getNeedAuthHelperProcessMessage();
}
