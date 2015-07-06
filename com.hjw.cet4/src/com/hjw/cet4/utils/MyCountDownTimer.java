package com.hjw.cet4.utils;

import android.os.CountDownTimer;
import fm.jihua.common.utils.AppLogger;

public class MyCountDownTimer extends CountDownTimer{
	
	public MyCountDownTimer(long millisInFuture, long countDownInterval, TimeChangeListener timeChangeListener) {     
        super(millisInFuture, countDownInterval); 
        this.mTimeChangeListener = timeChangeListener;
    }     
    @Override     
    public void onFinish() {     
    	this.cancel();
    	if(mTimeChangeListener != null){
    		mTimeChangeListener.onFinish();
    	}
    }     
    @Override     
    public void onTick(long millisUntilFinished) {     
		// int ms = (int) (millisUntilFinished/1000);
		// int day = ms/86400;
		// int hour = ms%86400/3600;
		// int min = ms%86400%3600/60;
		// int sec = ms%86400%3600%60;
    	AppLogger.v( "" + millisUntilFinished);
    	if(mTimeChangeListener != null){
    		mTimeChangeListener.onTick(millisUntilFinished);
    	}
    }
    
    public interface TimeChangeListener {
		public void onTick(long millisUntilFinished);
		public void onFinish();
	}

	private TimeChangeListener mTimeChangeListener;

	public void setOnGetBitmapClickListener(TimeChangeListener timeChangeListener) {
		this.mTimeChangeListener = timeChangeListener;
	}

}
