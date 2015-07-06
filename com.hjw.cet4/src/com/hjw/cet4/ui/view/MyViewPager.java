package com.hjw.cet4.ui.view;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class MyViewPager extends ViewPager{
	
	public static final int NORMAL = 0;
	public static final int RIGHT = 1;
	public static final int LEFT = 2;
	
	
	
	int mState;
	float mLastX;
	float mLastY;
	int mActivePointer;
	int mTouchSlop1;
	boolean isBeingDragged;
	
	public MyViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setState(int state){
		mState = state;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		mTouchSlop1 = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		switch (ev.getAction() & MotionEventCompat.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			mLastX = ev.getX();
			mLastY = ev.getY();
			isBeingDragged = false;
			mActivePointer = MotionEventCompat.getPointerId(ev, 0);
		}
			break;
		case MotionEvent.ACTION_MOVE: {
			final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointer);
//			if(!isBeingDragged){
//				final float x = ev.getX();
//				final float xDiff = Math.abs(x - mLastX);
//				final float y = ev.getY();
//				final float yDiff = Math.abs(y - mLastY);
//				if (xDiff > mTouchSlop1 && xDiff > yDiff) {
//					if(mState == RIGHT && (x - mLastX) > 0){
//						return true;
//					}
//					if(mState == LEFT && (x - mLastX) < 0){
//						return true;
//					}
//					mLastX = ev.getX();
////					isBeingDragged = true;
//				}
//			}
			if(mState == NORMAL){
				break;
			}
			if(!isBeingDragged){
				final float x = ev.getX();
//				final float xDiff = Math.abs(x - mLastX);
				if (mState == RIGHT && (x - mLastX) > 0) {
					return true;
				}
				if (mState == LEFT && (x - mLastX) < 0) {
					return true;
				}
				mLastX = ev.getX();
			}
		}
			break;
		}
		return super.onTouchEvent(ev);
	}

}
