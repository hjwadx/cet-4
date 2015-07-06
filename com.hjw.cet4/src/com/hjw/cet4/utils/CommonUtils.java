package com.hjw.cet4.utils;

import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import fm.jihua.common.utils.Compatibility;

public class CommonUtils extends fm.jihua.common.utils.CommonUtils{
	
	@TargetApi(8)
	public static void playSound(final Context context, int sound){
		SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 10);
        final int sourceid = soundPool.load(context, sound, 1);
        if (Compatibility.isCompatible(8)) {
        	
        	soundPool.setOnLoadCompleteListener(new android.media.SoundPool.OnLoadCompleteListener() {

				public void onLoadComplete(SoundPool soundPool,
						int sampleId, int status) {
					AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					soundPool.play(
							sourceid,
							mgr.getStreamVolume(AudioManager.STREAM_MUSIC),
							mgr.getStreamVolume(AudioManager.STREAM_MUSIC),
							1, 0, 1f);
				}
			});
        }else {
        	OnLoadCompleteListener onLoadCompleteListener = new OnLoadCompleteListener(soundPool) {

				public void onLoadComplete(SoundPool soundPool,
						int sampleId, int status) {
					AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
					soundPool.play(
							sourceid,
							mgr.getStreamVolume(AudioManager.STREAM_MUSIC),
							mgr.getStreamVolume(AudioManager.STREAM_MUSIC),
							1, 0, 1f);
				}
			};
			onLoadCompleteListener.addSound(sourceid);
		}
	}
	
	public static String join(List<String> list, String split){
		if (list.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        int i;
        for(i=0;i<list.size()-1;i++)
            sb.append(list.get(i)+split);
        return sb.toString()+list.get(i);
	}
	
	public static String replaceNullString(String original){
		String output = "";
		if(original != null && !"null".equals(original)){
			output = original;
		}
		return output;
	}
	
	public static boolean isNullString(String str){
		return str == null || "null".equals(str) || "".equals(str.trim());
	}
	
	public static boolean isWifi(final Context ctx) {
		ConnectivityManager connManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (mWifi != null && mWifi.isConnected()) {
			return true;
		}
		return false;
	}
}
