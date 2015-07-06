package com.hjw.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class MainActivity extends Activity {
	
	MyButton mMyButton;
	MyImageview mMyImageview;
	MyLinearLayout mMyLinearLayout;
	MyLinearLayout2 mMyLinearLayout2;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		initView();
	}

	private void findView() {
		mMyButton = (MyButton) findViewById(R.id.button);
		mMyImageview = (MyImageview) findViewById(R.id.imageview);
		mMyLinearLayout = (MyLinearLayout) findViewById(R.id.parent_1);
		mMyLinearLayout2 = (MyLinearLayout2) findViewById(R.id.parent_2);
	}

	private void initView() {
		mMyButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("MyButton", "onTouch");
				return false;
			}
		});
		mMyImageview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("MyImageview", "onTouch");
				return false;
			}
		});
		mMyLinearLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("MyLinearLayout", "onTouch");
				return false;
			}
		});
		mMyLinearLayout2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("mMyLinearLayout2", "onTouch");
				return false;
			}
		});
		mMyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("MyButton", "onClick");
			}
		});
		mMyImageview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("MyImageview", "onClick");
			}
		});
		mMyLinearLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("MyLinearLayout", "onClick");
			}
		});
		mMyLinearLayout2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.e("mMyLinearLayout2", "onClick");
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
