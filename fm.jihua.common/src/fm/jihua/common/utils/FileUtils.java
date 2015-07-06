package fm.jihua.common.utils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

public class FileUtils {
  public static boolean copyFile(File source, File dest) {
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    
    try {
      bis = new BufferedInputStream(new FileInputStream(source));
      bos = new BufferedOutputStream(new FileOutputStream(dest, false));
      
      byte[] buf = new byte[1024];
      bis.read(buf);
      
      do {
        bos.write(buf);
      } while(bis.read(buf) != -1);
    } catch (IOException e) {
      return false;
    } finally {
      try {
        if (bis != null) bis.close();
        if (bos != null) bos.close();
      } catch (IOException e) {
        return false;
      }
    }
    
    return true;
  }
  
  public static boolean copyFile(InputStream is, File dest) {
	    BufferedInputStream bis = null;
	    BufferedOutputStream bos = null;
	    
	    try {
	      bis = new BufferedInputStream(is);
	      bos = new BufferedOutputStream(new FileOutputStream(dest, false));
	      
	      byte[] buf = new byte[1024];
	      bis.read(buf);
	      
	      do {
	        bos.write(buf);
	      } while(bis.read(buf) != -1);
	    } catch (IOException e) {
	      return false;
	    } finally {
	      try {
	        if (bis != null) bis.close();
	        if (bos != null) bos.close();
	      } catch (IOException e) {
	        return false;
	      }
	    }
	    
	    return true;
	  }
  
  // WARNING ! Inefficient if source and dest are on the same filesystem !
  public static boolean moveFile(File source, File dest) {
    return copyFile(source, dest) && source.delete();
  }
  
  // Returns true if the sdcard is mounted rw
  public static boolean isSDMounted() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }
}