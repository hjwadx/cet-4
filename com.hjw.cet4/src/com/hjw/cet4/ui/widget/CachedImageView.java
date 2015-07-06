package com.hjw.cet4.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.hjw.cet4.App;
import com.hjw.cet4.R;
import com.hjw.cet4.utils.ImageHlp;


public class CachedImageView extends NetworkImageView {

	// private final String TAG = getClass().getSimpleName();
	private boolean mIsSquare;
	private boolean mIsRounded;
	private int mCornerSize;

	public CachedImageView(Context context) {
		super(context);
	}

	public CachedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CachedImageView);
		mIsSquare = a.getBoolean(R.styleable.CachedImageView_isSquare, false);
		this.mLoadBitmapImmediate = true;
	}
	
	
	public void setFadeIn(boolean fadeIn){
//		mImageFetcher.setImageFadeIn(fadeIn);
	}
	
	public void setCorner(boolean corner){
		this.mIsRounded = corner;
	}
	
	public void setCornerSize(int px){
		this.mCornerSize = px;
	}
	
	public void setLoadingBitmap(Bitmap bmp){
//		mImageFetcher.setLoadingImage(bmp);
	}
	
	public void setSquare(boolean isSquare){
		mIsSquare = isSquare;
	}

	@Override
	public void setImageURI(final Uri uri) {
		if (uri != null && "http".equals(uri.getScheme())) {
			setImageUrl(uri.toString(), App.getInstance().getImageLoader());
			return;
		}
		setImageUrl(null, App.getInstance().getImageLoader());
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		if(getBitmapCallbackListener != null){
			getBitmapCallbackListener.onClick(bm.getWidth(), bm.getHeight());
		}
		if (mIsRounded && bm != null && !bm.isRecycled()) {
			if(mCornerSize > 0){
				super.setImageBitmap(ImageHlp.getRoundedCornerBitmap(bm, mCornerSize));
			} else {
				super.setImageBitmap(ImageHlp.getRoundedCornerBitmapAuto(bm));
			}
//			bm.recycle();
		}else {
			super.setImageBitmap(bm);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(mIsSquare){
			int width = MeasureSpec.getSize(widthMeasureSpec);
			setMeasuredDimension(width, width);
		}else {
			super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		}
	}
	
	public interface GetBitmapCallbackListener {
		public void onClick(int width, int height);
	}

	private GetBitmapCallbackListener getBitmapCallbackListener;

	public void setOnGetBitmapClickListener(GetBitmapCallbackListener getBitmapCallbackListener) {
		this.getBitmapCallbackListener = getBitmapCallbackListener;
	}
}
