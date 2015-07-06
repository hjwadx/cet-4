package com.hjw.cet4.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomFileNameFilter implements FilenameFilter{
	
	List<String> types;  
	  
    /** 
     * 构造一个Mp3FileNameFilter对象，其指定文件类型为空。 
     */  
    protected CustomFileNameFilter() {  
        types = new ArrayList<String>();  
    }  
  
    /** 
     * 构造一个Mp3FileNameFilter对象，具有指定的文件类型。 
     * @param types 
     */  
    protected CustomFileNameFilter(List<String> types) {  
        super();  
        this.types = types;  
    }  
  
    @Override  
    public boolean accept(File dir, String filename) {  
        // TODO Auto-generated method stub  
        for (Iterator<String> iterator = types.iterator(); iterator.hasNext();) {  
            String type = (String) iterator.next();  
            if (filename.endsWith(type)) {  
                return true;  
            }  
        }  
        return false;  
    }  
      
    /** 
     * 添加指定类型的文件。 
     *  
     * @param type  将添加的文件类型，如".mp3"。 
     */  
    public void addType(String type) {  
        types.add(type);  
    }  
}
