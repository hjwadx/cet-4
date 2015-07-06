package com.hjw.cet4.entities;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.hjw.cet4.App;
import com.hjw.cet4.utils.CommonUtils;
import com.hjw.cet4.utils.Const;

import android.os.Environment;

public class Paper implements Serializable{
	
	public int id;
	public String name;
	public String voice_url;
	
//	public int price;
	public boolean isDownloading;
	
	public Paper(int id, String name, String voice_url) {
		super();
		this.id = id;
		this.name = name;
		this.voice_url = voice_url;
	}
	
	public String getVoiceFileName(){
		if(CommonUtils.isNullString(voice_url)){
			return "不存在";
		}
//		return voice_url.substring(voice_url.lastIndexOf("/") + 1, voice_url.lastIndexOf("?"));
		return "paper"+ id + "_" + voice_url.substring(voice_url.lastIndexOf("/") + 1, voice_url.length());
	}
	
	public boolean isVoiceDownloaded(){
		File file = new File(Environment.getExternalStorageDirectory() + Const.AUDIO_DIR + getVoiceFileName());
		if (file.exists()) {
			return true;
		}
		return false;
	}
	
	public boolean isDone(){
		List<String> lists = App.getInstance().getDonePaperList();
		if (lists.contains(String.valueOf(id))) {
			return true;
		}
		return false;
	}
	
	public void setDownloading(boolean is_downloading){
		isDownloading = is_downloading;
	}
	
	public boolean  isDownloading(){
		return isDownloading;
	}
	
	public String getDownloadUrlString(){
		return voice_url;
	}
}
