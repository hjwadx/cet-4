package com.hjw.cet4.share;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public interface AuthHelper {
	public void auth(SNSCallback callback);
	public void bind(SNSCallback callback);
	public void unBind();
	public boolean isAuthed();
//	public void setAuthCallback(AuthCallback callback);
	public void update(String status, SNSCallback callback);
	public void update(String title,  String message, String url, String imageUrl, String subtitle, String targetUrl, SNSCallback callback);
	public void upload(String file, String status, SNSCallback callback);
	public void uploadPictures(String title, ArrayList<HashMap<String, Object>> listItems, SNSCallback callback);
	
	public void getFriends(int page, int limit, SNSCallback callback);
	public void getAllFriends(SNSCallback callback);
	public int getType();
	public String getTypeName();
	public String getThirdPartId();
	public String getThirdPartToken();
	public void getUserInfo(SNSCallback callback);
	
	public static class CommonUser extends SimpleUser{
		public String largeAvatar;
		public int gender;
		public String token;
		public SchoolInfo[] schoolInfos;
	}
	
	public static class SchoolInfo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 509165095331008230L;
		public String school;
		public String department;
		public int year;
	}
}
