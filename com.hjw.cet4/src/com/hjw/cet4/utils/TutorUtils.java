package com.hjw.cet4.utils;

import com.hjw.cet4.App;
import com.hjw.cet4.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class TutorUtils {
	
	private static TutorUtils tutorUtils;

	public static final TutorUtils getInstance() {
		if (tutorUtils == null) {
			tutorUtils = new TutorUtils();
		}
		return tutorUtils;
	}
	
	public void showSlideTutor(Activity context){
		final ViewGroup parent = (ViewGroup)context.getWindow().getDecorView();
		
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.coachmark);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((ImageView)v).setImageResource(0);
				App.getInstance().setSharedBoolean(Const.SLIDE_TUTOR, true);
				parent.removeView(v);
			}
		});
		
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		parent.addView(imageView, lp);
	}

}
