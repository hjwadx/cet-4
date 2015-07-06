package com.hjw.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends Button{

	
	public MyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e(getClass().getSimpleName(), "onTouchEvent");
		LogUtils.show(event, getClass().getSimpleName(), "onTouchEvent");
		return super.onTouchEvent(event);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		Log.e(getClass().getSimpleName(), "dispatchTouchEvent");
		LogUtils.show(ev, getClass().getSimpleName(), "dispatchTouchEvent");
		return super.dispatchTouchEvent(ev);
	}

}
