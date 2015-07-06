package com.hjw.cet4.utils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimationUtils {
	private static AnimationUtils animationUtils;
	Random random = new Random();
	public static AtomicBoolean animating = new AtomicBoolean();

	public static AnimationUtils getInstance() {
		if (animationUtils == null)
			animationUtils = new AnimationUtils();
		return animationUtils;
	}

	public AnimationSet createDismissAnim() {
		AnimationSet set = new AnimationSet(true);
		int nextInt = random.nextInt(1500);
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_PARENT, 1.3f);
		translateAnimation.setDuration(1000);
		translateAnimation.setStartOffset(nextInt);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0);
		alphaAnimation.setStartOffset(500 + nextInt);
		alphaAnimation.setDuration(500);
		set.addAnimation(translateAnimation);
		set.addAnimation(alphaAnimation);
		return set;
	}

	public Animation createSharkAnim() {
		int numberDuration = random.nextInt(30);
		RotateAnimation rotateAnimation = new RotateAnimation(1.8f, -1.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(50 + numberDuration);
		rotateAnimation.setRepeatCount(1000);
		rotateAnimation.setRepeatMode(Animation.REVERSE);
		return rotateAnimation;
	}

	public AnimationSet createEnterAnimation() {
		AnimationSet set = new AnimationSet(true);
		set.setInterpolator(new LinearInterpolator());
		set.setStartOffset(random.nextInt(1000));
		ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.3f, 0.5f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(800);
		ScaleAnimation scale2Animation = new ScaleAnimation(1.3f, 1f, 1.3f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scale2Animation.setDuration(300);
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(500);
		set.addAnimation(scaleAnimation);
		set.addAnimation(scale2Animation);
		set.addAnimation(alphaAnimation);
		return set;
	}

	public AnimationSet createBoomAnimation() {
		AnimationSet set = new AnimationSet(true);
		set.setInterpolator(new LinearInterpolator());
		ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 1.8f, 1f, 1.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(500);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
		alphaAnimation.setDuration(500);
		set.addAnimation(scaleAnimation);
		set.addAnimation(alphaAnimation);
		return set;
	}

	public static void startAnimationIN(final ViewGroup viewGroup, int duration, final OnAnimationEndListener endListener) {
		if (animating.get()) {
			return;
		}
		viewGroup.setVisibility(View.VISIBLE);
//		for (int i = 0; i < viewGroup.getChildCount(); i++) {
//			viewGroup.getChildAt(i).setVisibility(View.VISIBLE);
//			viewGroup.getChildAt(i).setFocusable(true);
//			viewGroup.getChildAt(i).setClickable(true);
//		}

		Animation animation;
		animation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				animating.set(true);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				animating.set(false);
				viewGroup.setAnimation(null);
				if (endListener != null) {
					endListener.animEnd();
				}
			}
		});
		viewGroup.startAnimation(animation);

	}

	public static void startAnimationOUT(final ViewGroup viewGroup, int duration, int startOffSet, final OnAnimationEndListener endListener) {
		if (animating.get()) {
			return;
		}
		Animation animation;
		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		animation.setStartOffset(startOffSet);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				animating.set(true);
				viewGroup.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
//				for (int i = 0; i < viewGroup.getChildCount(); i++) {
//					viewGroup.getChildAt(i).setVisibility(View.GONE);
//					viewGroup.getChildAt(i).setFocusable(false);
//					viewGroup.getChildAt(i).setClickable(false);
//				}
				viewGroup.setVisibility(View.GONE);
				viewGroup.setAnimation(null);
				animating.set(false);
				if (endListener != null) {
					endListener.animEnd();
				}
			}
		});

		viewGroup.startAnimation(animation);
	}
	
	public static void startAnimationRotate(final View View, int duration, int startOffSet, final OnAnimationEndListener endListener, int rotate) {
		Animation animation;
		animation = new RotateAnimation(rotate, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(true);
		animation.setDuration(duration);
		animation.setStartOffset(startOffSet);
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				View.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
//				for (int i = 0; i < viewGroup.getChildCount(); i++) {
//					viewGroup.getChildAt(i).setVisibility(View.GONE);
//					viewGroup.getChildAt(i).setFocusable(false);
//					viewGroup.getChildAt(i).setClickable(false);
//				}
				View.setAnimation(null);
				if (endListener != null) {
					endListener.animEnd();
				}
			}
		});

		View.startAnimation(animation);
	}

	public Animation createFadeAnim() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
		alphaAnimation.setDuration(1300);
		alphaAnimation.setRepeatCount(1000);
		alphaAnimation.setRepeatMode(Animation.REVERSE);
		return alphaAnimation;
	}

	public Animation createTranslateAnim(float fromX, float toX, float fromY, float toY) {
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, fromX, Animation.RELATIVE_TO_SELF, toX, Animation.RELATIVE_TO_SELF, fromY, Animation.RELATIVE_TO_SELF, toY);
		translateAnimation.setDuration(3000);
		return translateAnimation;
	}

	public Animation createEnterFromBottomAnim(final AnimationListener listener) {

		AnimationSet set = new AnimationSet(true);
		set.setInterpolator(new LinearInterpolator());
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
		translateAnimation.setDuration(300);
//		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
//		alphaAnimation.setDuration(300);
		set.addAnimation(translateAnimation);
//		set.addAnimation(alphaAnimation);
		if(listener != null){
			translateAnimation.setAnimationListener(listener);
		}
		return set;
	}
	
	public Animation createExitFromBottomAnim(final AnimationListener listener) {

		AnimationSet set = new AnimationSet(true);
		set.setInterpolator(new LinearInterpolator());
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
		translateAnimation.setDuration(300);
//		AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
//		alphaAnimation.setDuration(300);
		set.addAnimation(translateAnimation);
//		set.addAnimation(alphaAnimation);
		if(listener != null){
			translateAnimation.setAnimationListener(listener);
		}
		return set;
	}

	public interface OnAnimationEndListener {
		public void animEnd();
	}

}
