package com.hjw.cet4.utils;

import static android.os.Environment.MEDIA_MOUNTED;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hjw.cet4.App;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import fm.jihua.common.utils.Compatibility;

/**
 * @date 2013-7-20
 * @introduce 文件操作工具类
 */
public class FileUtils {

	private static FileUtils fileUtils;

	public static final FileUtils getInstance() {
		if (fileUtils == null) {
			fileUtils = new FileUtils();
		}
		return fileUtils;
	}

	public static final String CACHE_PATH_NAME = File.separator + "kecheng" + File.separator;
	public static final String HANDDORAWNMAP_NAME_DOWNLOAD = "hand_map.tmp";
	public static final String HANDDORAWNMAP_NAME = "hand_map.png";

	// 获得kecheng文件夹路径
	public String getValidDownloadPath(Context context) {
		String cacheDir = "";
		File file = null;
		if (Environment.getExternalStorageState().equals(MEDIA_MOUNTED)) {
			file = Environment.getExternalStorageDirectory();
			cacheDir = file.getPath() + CACHE_PATH_NAME;
		} else
			cacheDir = context.getCacheDir().getAbsolutePath() + CACHE_PATH_NAME;

		File cacheDirFile = new File(cacheDir);
		if (!cacheDirFile.exists()) {
			synchronized (cacheDirFile) {
				if (!cacheDirFile.exists())
					cacheDirFile.mkdirs();
			}
		}
		return cacheDir;
	}

	public String getHandDrawnMapDownloadPath(Context context, String fileName) {
		File file = new File(getValidDownloadPath(context) + fileName);
		if (file.exists() && file.isDirectory()) {
			file.delete();
		}
		return file.getAbsolutePath();
	}

	public String readStringFromFile(InputStream inputStream) {
		StringBuffer stringBuffer = new StringBuffer();
		Reader reader = null;
		try {
			// 一次读多个字符
			char[] tempchars = new char[30];
			int charread = 0;
			reader = new InputStreamReader(inputStream);
			// 读入多个字符到字符数组中，charread为一次读取字符数
			while ((charread = reader.read(tempchars)) != -1) {
				// 同样屏蔽掉\r不显示
				if ((charread == tempchars.length) && (tempchars[tempchars.length - 1] != '\r')) {
					stringBuffer.append(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == '\r') {
							continue;
						} else {
							stringBuffer.append(tempchars[i]);
						}
					}
				}
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return stringBuffer.toString();
	}

	public Bitmap getCacheBitmap(Context context, String userHandMapString) {

		String handDrawnMapDownloadPath = FileUtils.getInstance().getHandDrawnMapDownloadPath(context, userHandMapString);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(handDrawnMapDownloadPath, options);
		final int height = options.outHeight;
		final int width = options.outWidth;
		options.inSampleSize = 1;
		int w = App.mDisplayWidth > 0 ? App.mDisplayWidth : Compatibility.getWidth(((Activity)context).getWindow().getWindowManager().getDefaultDisplay());
		int h = App.mDisplayHeight > 0 ? App.mDisplayHeight : Compatibility.getHeight(((Activity)context).getWindow().getWindowManager().getDefaultDisplay());;
		h = w * height / width;// 计算出宽高等比率
		int a = options.outWidth / w;
		int b = options.outHeight / h;
		options.inSampleSize = Math.max(a, b);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(handDrawnMapDownloadPath, options);
		return bitmap;
	}

	// 根据文件类型得到目录及子目录下的符合条件的全部文件
	public List<File> getAllFileBySuffix(String path, String suffix) {
		List<File> result = new ArrayList<File>();
		File file = new File(path);
		if(!file.exists()){
			return result;
		}
		if (file.isDirectory()) {
			CustomFileNameFilter filenameFilter = new CustomFileNameFilter();
			filenameFilter.addType(suffix);
			File[] files = file.listFiles(filenameFilter);
			result.addAll(Arrays.asList(files));
			for (File file1 : file.listFiles()) {
				if (file1.isDirectory()) {
					result.addAll(getAllFileBySuffix(file1.getPath(), suffix));
				}
			}
		}
		return result;
	}

	public String byte2String(long byteNumber) {
		DecimalFormat df = new DecimalFormat("#.00");

		if (byteNumber <= Math.pow(1024, 1)) {
			return "0K";
		} else if (byteNumber > Math.pow(1024, 1) && byteNumber < Math.pow(1024, 2)) {
			return df.format(byteNumber / Math.pow(1024, 1)) + "K";
		} else {
			return df.format(byteNumber / Math.pow(1024, 2)) + "M";
		}
	}
	
	public boolean isSDAvailable(){
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	public boolean deleteFolder(String filePath){
		File fileDir = new File(filePath);
		if(fileDir.exists() && fileDir.isDirectory()){
			File[] listFiles = fileDir.listFiles();
			for (File file : listFiles) {
				file.delete();
			}
		}
		return fileDir.delete();
	}
	
	public String addPngSuffix(String filePath) {
		if (filePath.lastIndexOf(".png") != -1 && filePath.lastIndexOf(".png") == filePath.length() - 4) {
			return filePath;
		} else {
			return filePath + ".png";
		}
	}
	
////	Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
//    !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() :
//        context.getCacheDir().getPath();
}
