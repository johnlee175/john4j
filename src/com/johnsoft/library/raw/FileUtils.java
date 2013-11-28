package com.johnsoft.library.raw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class FileUtils
{ 
	/**
	 * use NIO to copy from channel to channel
	 */
    public static void forTransfer(File f1,File f2) throws Exception
    {
        int length=1048576;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
        while(true)
        {
            if(inC.position()==inC.size()){
                inC.close();
                outC.close();
                break;
            }
            if((inC.size()-inC.position())<10485760)
                length=(int)(inC.size()-inC.position());
            else
                length=10485760;
            inC.transferTo(inC.position(),length,outC);
            inC.position(inC.position()+length);
        }
        out.close();
        in.close();
    }
    
    public static boolean bakFile(String filePath , String toFilePath)   
    { 
    	boolean isSuc = false;
    	try{ 
        	File file = new File(filePath);
    		if(!file.exists()){
    			file.mkdirs();
    		}
    		File[] files = file.listFiles();
    		String[] fileName = file.list(); 
    		for(int i=0;i<files.length;i++){
    			File newFile = new File(toFilePath+"\\"+fileName[i]);
    			forTransfer(files[i], newFile);
    		}
    		isSuc = true; 
        }catch (Exception e) {
        	isSuc = false;
        	e.printStackTrace();
		}
    	return isSuc;
    } 
    
	public static boolean deleteFile(String filePath){
    	boolean isSuc = false;
    	try{
    		//get file address
        	File file = new File(filePath);
    		if(!file.exists()){
    			file.mkdirs();
    		}
    		//create file list
    		File[] files=file.listFiles();
    		//batch delete files
    		for(int i=0;i<files.length;i++){
    			files[i].delete();	
    		}
    		isSuc = true;
    	}catch (Exception e) {
    		isSuc = false;
    		e.printStackTrace();
		}
		return isSuc;
    }
	
} 
