package fm.jihua.common.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class ImageHlp extends Activity {

	/**
	 * 将图片转换成圆角图片
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		// canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
		canvas.drawARGB(0, 0, 0, 0);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// final float roundPx = 10;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
//	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
//                .getHeight(), Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
//        final float roundPx = pixels;
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//
//        return output;
//    }

	private static Bitmap getShadowBitmap(Bitmap bitmap, int nX, int nY) {
		if(bitmap == null){
			return null;
		}
		BlurMaskFilter blurFilter = new BlurMaskFilter(5, BlurMaskFilter.Blur.OUTER);
		Paint shadowPaint = new Paint();
		shadowPaint.setMaskFilter(blurFilter);
		int[] offsetXY = new int[2];
		offsetXY[0] = nX;
		offsetXY[1] = nY;
		Bitmap shadowImage = bitmap.extractAlpha(shadowPaint, offsetXY);
		Bitmap newb = Bitmap.createBitmap(bitmap.getWidth() + nX * 2, bitmap.getHeight() + nY * 2, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas canvas = new Canvas(newb);
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(shadowImage, -nX, -nY, null);
		canvas.drawBitmap(bitmap, 0, 0, null);
		return newb;
	}
	
	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		//TODO   bitmap recycle
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/** 
     * 柔化效果(高斯模糊)(优化后比上面快三倍) 
     * @param bmp 
     * @return 
     */  
    public static Bitmap blurImageAmeliorate(Bitmap bmp)  
    {  
        long start = System.currentTimeMillis();  
        // 高斯矩阵  
        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };  
          
        int width = bmp.getWidth();  
        int height = bmp.getHeight();  
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
          
        int pixR = 0;  
        int pixG = 0;  
        int pixB = 0;  
          
        int pixColor = 0;  
          
        int newR = 0;  
        int newG = 0;  
        int newB = 0;  
          
        int delta = 16; // 值越小图片会越亮，越大则越暗  
          
        int idx = 0;  
        int[] pixels = new int[width * height];  
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);  
        for (int i = 1, length = height - 1; i < length; i++)  
        {  
            for (int k = 1, len = width - 1; k < len; k++)  
            {  
                idx = 0;  
                for (int m = -1; m <= 1; m++)  
                {  
                    for (int n = -1; n <= 1; n++)  
                    {  
                        pixColor = pixels[(i + m) * width + k + n];  
                        pixR = Color.red(pixColor);  
                        pixG = Color.green(pixColor);  
                        pixB = Color.blue(pixColor);  
                          
                        newR = newR + (int) (pixR * gauss[idx]);  
                        newG = newG + (int) (pixG * gauss[idx]);  
                        newB = newB + (int) (pixB * gauss[idx]);  
                        idx++;  
                    }  
                }  
                  
                newR /= delta;  
                newG /= delta;  
                newB /= delta;  
                  
                newR = Math.min(255, Math.max(0, newR));  
                newG = Math.min(255, Math.max(0, newG));  
                newB = Math.min(255, Math.max(0, newB));  
                  
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);  
                  
                newR = 0;  
                newG = 0;  
                newB = 0;  
            }  
        }  
          
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);  
        long end = System.currentTimeMillis();  
        Log.d("may", "used time="+(end - start));  
        return bitmap;  
    }  
    
    /* 设置图片模糊 */
    public static Bitmap setBlur(Bitmap bmpSource, int blur)  //源位图，模糊强度
    {
    	int pixels[] = new int[bmpSource.getWidth() * bmpSource.getHeight()];  //颜色数组，一个像素对应一个元素
    	int pixelsRawSource[] = new int[bmpSource.getWidth() * bmpSource.getHeight() * 3];  //三原色数组，作为元数据，在每一层模糊强度的时候不可更改
    	int pixelsRawNew[] = new int[bmpSource.getWidth() * bmpSource.getHeight() * 3];  //三原色数组，接受计算过的三原色值
    	bmpSource.getPixels(pixels, 0, bmpSource.getWidth(), 0, 0, bmpSource.getWidth(), bmpSource.getHeight());  //获取像素点

    	//模糊强度，每循环一次强度增加一次
    	for (int k = 1; k <= blur; k++)
    	{
    		//从图片中获取每个像素三原色的值
    		for (int i = 0; i < pixels.length; i++)
    		{
    			pixelsRawSource[i * 3 + 0] = Color.red(pixels[i]);
    			pixelsRawSource[i * 3 + 1] = Color.green(pixels[i]);
    			pixelsRawSource[i * 3 + 2] = Color.blue(pixels[i]);
    		}

    		//取每个点上下左右点的平均值作自己的值
    		int CurrentPixel = bmpSource.getWidth() * 3 + 3; // 当前处理的像素点，从点(2,2)开始
    		for (int i = 0; i < bmpSource.getHeight() - 3; i++) // 高度循环
    		{
    			for (int j = 0; j < bmpSource.getWidth() * 3; j++) // 宽度循环
    			{
    				CurrentPixel += 1;
    				// 取上下左右，取平均值
    				int sumColor = 0; // 颜色和
    				sumColor = pixelsRawSource[CurrentPixel - bmpSource.getWidth() * 3]; // 上一点
    				sumColor = sumColor + pixelsRawSource[CurrentPixel - 3]; // 左一点
    				sumColor = sumColor + pixelsRawSource[CurrentPixel + 3]; // 右一点
    				sumColor = sumColor + pixelsRawSource[CurrentPixel + bmpSource.getWidth() * 3]; // 下一点
    				pixelsRawNew[CurrentPixel] = Math.round(sumColor / 4); // 设置像素点
    			}
    		}

    		//将新三原色组合成像素颜色
    		for (int i = 0; i < pixels.length; i++)
    		{
    			pixels[i] = Color.rgb(pixelsRawNew[i * 3 + 0], pixelsRawNew[i * 3 + 1], pixelsRawNew[i * 3 + 2]);
    		}
    	}

    	//应用到图像
    	Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(), bmpSource.getHeight(), Config.ARGB_8888);
    	bmpReturn.setPixels(pixels, 0, bmpSource.getWidth(), 0, 0, bmpSource.getWidth(), bmpSource.getHeight());  //必须新建位图然后填充，不能直接填充源图像，否则内存报错

    	return bmpReturn;
    }


	public static Bitmap getRoundedCornerAndShadowBitmap(Bitmap bitmap) {
		return getRoundedCornerAndShadowBitmap(bitmap, false);
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		//TODO   bitmap recycle
		Bitmap bm = getRoundedCornerBitmap(bitmap, 10);
		return bm;
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, boolean isGraved) {
		//TODO   bitmap recycle
		Bitmap bm = getRoundedCornerBitmap(isGraved ? toGrayscale(bitmap) : bitmap);
		return bm;
	}
	
	public static Bitmap getRoundedCornerAndShadowBitmap(Bitmap bitmap, boolean isGraved) {
		Bitmap bm = getRoundedCornerBitmap(isGraved ? toGrayscale(bitmap) : bitmap, 10);
		bm = getShadowBitmap(bm, 5, 5);
		Bitmap output = getRoundedCornerBitmap(bm, 10);
		return output;
	}
	
	public static Bitmap decodeStream(InputStream stream) {
		Bitmap b = null;
		try {
			b = BitmapFactory.decodeStream(stream);
			
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeStream OutOfMemoryError:" + err.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeStream Exception:" + e.getMessage());
		}
		return b;
	}
	
	public static Bitmap decodeURIStream(ContentResolver cr, Uri uri) {
		Bitmap b = null;
		try {
			b = BitmapFactory.decodeStream(cr.openInputStream(uri));
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeURIStream OutOfMemoryError:" + err.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeURIStream Exception:" + e.getMessage());
		}
		return b;
	}
	
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
			b = BitmapFactory.decodeFile(fname);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile OutOfMemoryError:" + err.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile Exception:" + e.getMessage());
		}
		return b;
	}
	
//	TODO this function is wrong
	public static Bitmap decodeFile(String fname, int width, int height) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fname, o);  
			o.inSampleSize = computeSampleSize(o, -1, width*height);
			o.inJustDecodeBounds = false;
			b = BitmapFactory.decodeFile(fname, o);
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
			b = BitmapFactory.decodeResource(resources, id);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile OutOfMemoryError:" + err.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile Exception:" + e.getMessage());
		}
		return b;
	}
	
	public static Bitmap decodeResource(Resources resources, int id, int width, int height) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(resources, id, o);  
			o.inSampleSize = computeSampleSize(o, -1, width*height);
			o.inJustDecodeBounds = false;
			b = BitmapFactory.decodeResource(resources, id, o);
		} catch (OutOfMemoryError err) {
			err.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile OutOfMemoryError:" + err.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(Const.TAG, "ImageHlp decodeFile Exception:" + e.getMessage());
		}
		return b;
	}
	
	public static BitmapDrawable createFromPath(Resources resources, String pathName){
		if (pathName == null) {
			return null;
		}
		
		Bitmap bm = BitmapFactory.decodeFile(pathName);
		if (bm != null) {
			return new BitmapDrawable(resources, bm);
		}
		return null;
	}

	public static Bitmap getFace(Resources res, int rid) {
		BitmapDrawable bmpDraw = (BitmapDrawable) res.getDrawable(rid);
		Bitmap bmp = bmpDraw.getBitmap();
		return bmp;
	}

	public static Drawable drawable_from_url(String url, String src_name) throws java.net.MalformedURLException,
			java.io.IOException {
		return Drawable.createFromStream(((java.io.InputStream) new java.net.URL(url).getContent()), src_name);
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap
    {
//        int width = drawable.getIntrinsicWidth();   // 取drawable的长宽
//        int height = drawable.getIntrinsicHeight();
//        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;         // 取drawable的颜色格式
//        Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应bitmap
//        Canvas canvas = new Canvas(bitmap);         // 建立对应bitmap的画布
//        drawable.setBounds(0, 0, width, height);
//        drawable.draw(canvas);      // 把drawable内容画到画布中
//        return bitmap;
		return ((BitmapDrawable)drawable).getBitmap();
    }
	
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		if (maxNumOfPixels == 0) {
			return 1;
		}
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
				.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
	public static int changeToSystemUnitFromDP(Context context, double size){
		int newSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				(float) size, context.getResources().getDisplayMetrics());
		return newSize;
	}
	
	public static int changeToSystemUnitFromPX(Context context, double size){
		int newSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
				(float) size, context.getResources().getDisplayMetrics());
		return newSize;
	}
	
	public static Bitmap snap(View view){
		Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);
		return bitmap;
	}
	
	public static void saveBitmap(Bitmap bmp, String filePath){
		FileOutputStream fo;
		try {
			File f = new File(filePath);
			f.createNewFile();
			fo = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fo);
			fo.flush();
			fo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取图片占用字节的大小
	 * 
	 * @param value
	 * @return
	 */
	public static int getBytesSize(Bitmap value) {
		if (value != null && value.getConfig() != null) {
			int perPixel = 0;
			switch (value.getConfig()) {
			case ALPHA_8:
				perPixel = 1;
				break;
			case ARGB_4444:
			case RGB_565:
				perPixel = 2;
				break;
			case ARGB_8888:
				perPixel = 4;
				break;
			}
			return value.getWidth() * value.getHeight() * perPixel;
		}
		return 0;
	}
	
	/**
	 * 获取图片占用字节的大小
	 * 
	 * @param value
	 * @return
	 */
	public static int getBytesSize(String path) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, o);
		int perPixel = 4;
		return o.outWidth * o.outHeight * perPixel;
	}
	
	public static byte[] getBitmapByte(Bitmap bitmap){   
	    ByteArrayOutputStream out = new ByteArrayOutputStream();   
	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   
	    try {   
	        out.flush();   
	        out.close();   
	    } catch (IOException e) {   
	        e.printStackTrace();   
	    }   
	    return out.toByteArray();   
	}  
}
