package com.hjw.cet4.utils;


import java.io.IOException;
import java.io.InputStream;

import com.hjw.cet4.App;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

public class ImageHlp extends fm.jihua.common.utils.ImageHlp {
	
//	public static Bitmap decodeURL(String url){
//		HttpClientAvatarRetriever retriever = new HttpClientAvatarRetriever(url);
//		try {
//			byte[] data = retriever.getAvatar();
//			if (data != null) {
//				InputStream in = new ByteArrayInputStream(data);
//				return BitmapFactory.decodeStream(in);
//			}
//		} catch (OutOfMemoryError e) {
//			e.printStackTrace();
//		}catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * @param fname
	 * 注意，调用decodeFile之后，需要调用bitmap.recycle()进行释放。 
	 */
	public static Bitmap decodeFile(String fname) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fname, o);  
			int maxWidth = Math.max(App.mDisplayWidth*2, Const.FILE_IMAGE_MAX_WIDTH);
			if(o.outWidth > maxWidth || o.outHeight > maxWidth){
				o.inSampleSize = computeSampleSize(o, -1, maxWidth*maxWidth);
				o.inJustDecodeBounds = false;
				b = BitmapFactory.decodeFile(fname, o);
			}else{
				b = BitmapFactory.decodeFile(fname);
			}
			
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile OutOfMemoryError:" + err.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile Exception:" + e.getMessage());
		}
		return b;
	}
	
	public static Bitmap decodeResource(Resources resources, int id) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(resources, id, o);  
			int maxWidth = Math.max(App.mDisplayWidth*2, Const.FILE_IMAGE_MAX_WIDTH);
			if(o.outWidth > maxWidth || o.outHeight > maxWidth){
				o.inSampleSize = computeSampleSize(o, -1, maxWidth*maxWidth);
				o.inJustDecodeBounds = false;
				b = BitmapFactory.decodeResource(resources, id, o);
			}else{
				b = BitmapFactory.decodeResource(resources, id);
			}
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile OutOfMemoryError:" + err.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile Exception:" + e.getMessage());
		}
		return b;
	}
	
	public static Bitmap getRoundedCornerBitmapAuto(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Bitmap input = bitmap;
		int diameter;
		if(Math.abs(bitmap.getWidth() - bitmap.getHeight()) > 5){
			diameter = Math.min(bitmap.getHeight(), bitmap.getWidth());
			input = Bitmap.createBitmap(bitmap, Math.max(0, bitmap.getWidth()/2 - bitmap.getHeight()/2), Math.max(0, bitmap.getHeight()/2 - bitmap.getWidth()/2), diameter, diameter);
			output = Bitmap.createBitmap(diameter, diameter, Config.ARGB_8888);
		}
		Canvas canvas = new Canvas(output);
		// canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		canvas.drawARGB(0, 0, 0, 0);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, input.getWidth(), input.getHeight());
		final RectF rectF = new RectF(rect);
		// final float roundPx = 10;
		paint.setAntiAlias(true);
		canvas.drawRoundRect(rectF, input.getWidth()/2, input.getHeight()/2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(input, rect, rect, paint);
		return output;
	}
	
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    
	public static Bitmap getImageFromAssetsFile(Resources resources, String fileName) {
		Bitmap image = null;
		AssetManager am = resources.getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public static BitmapDrawable getRepeatShadowBitmap(Context context, int res) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
		@SuppressWarnings("deprecation")
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		drawable.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		drawable.setDither(true);
		return drawable;
	}

	public static Bitmap combine(View view) {
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		view.draw(canvas);
		return bitmap;
	}
	
//	public static BitmapDrawable getImageFromFile(Context context, String filePath) {
//		InputStream input;
//		BitmapDrawable bitmapDrawable = null;
//		try {
//			input = new FileInputStream(filePath);
//			bitmapDrawable = new BitmapDrawable(context.getResources(), input);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		return bitmapDrawable;
//	}
	
	public static BitmapDrawable getImageFromFileShrinked(Context context, String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSizeJustByWidth(options, App.mDisplayWidth);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return new BitmapDrawable(context.getResources(), BitmapFactory.decodeFile(filePath, options));
	}
	
	//计算图片的缩放值
	public static int calculateInSampleSizeJustByWidth(BitmapFactory.Options options, int reqWidth) {
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (width > reqWidth) {
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = widthRatio;
		}
		return inSampleSize;
	}
	
	//按屏幕的预期比例和实际比例的比来生成图片，ratio为在不考虑宽度的情况下对图片的直接压缩比例
	public static BitmapDrawable getImageFromFileShrinkedByWidthWithRatio(Context context, String filePath, int idealWidth, int ratio) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSizeJustByWidthWithRatio(options, App.mDisplayWidth, idealWidth, ratio);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		options.inScaled = false;
		return new BitmapDrawable(context.getResources(), BitmapFactory.decodeFile(filePath, options));
	}
	
	public static int calculateInSampleSizeJustByWidthWithRatio(BitmapFactory.Options options, int reqWidth, int idealWidth, int ratio) {
		int inSampleSize = 1;

		if (idealWidth > reqWidth) {
			int widthRatio = Math.round((float) idealWidth / (float) reqWidth);
			if(ratio > 0){
				widthRatio = widthRatio * ratio;
			}
			inSampleSize = widthRatio > 1 ? widthRatio : 1;
		}
		return inSampleSize;
	}
}
