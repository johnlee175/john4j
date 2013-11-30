package com.johnsoft.tools;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class FileCopyer
{
	Random rdm=new Random();
	int count=0;
	String inputFolder="D:\\temp\\dk";
	String outputFolder="D:\\temp\\dk";
	String outputFolder1="D:\\temp\\bdk";
	public FileCopyer()
	{
		File file = new File(inputFolder);
	//	iterate(file);
	//	splitMonoFile(file);
		delete(file);
		System.out.println("count:"+count);
	}
	
	void iterate(File file)
	{
		if(file != null)
		{
			if(file.isFile())
			{
				if(file.getName().toLowerCase().endsWith(".ttf"))
				{
					count ++;
					if(count%1000==0)
					{
						System.out.println(count);
					}
					renameAndCopyAndPaste(file);
				}
			}else{
				File[] fs = file.listFiles();
				if(fs != null&&fs.length > 0)
				{
					for(File f : fs)
					{
						iterate(f);
					}
				}
			}
		}
	}
	
	void renameAndCopyAndPaste(File file)
	{
//		String ap=file.getAbsolutePath();
//		ap=ap.substring(0, ap.length()-4);
//		file.renameTo(new File(ap+".ttf"));
		
		String name=file.getName();
		name=name.substring(0, name.length()-4);
		String path=outputFolder+"\\"+name+".ttf";
		File f=new File(path);
		if(f.exists())
		{
			path=outputFolder+"\\"+name+rdm.nextLong()+".ttf";
		}
		 try
		{
			 FileInputStream fis=new FileInputStream(file);
			 BufferedInputStream bis=new BufferedInputStream(fis);
			 byte[] bytes=new byte[bis.available()];
			 int len=0;
			 FileOutputStream fos=new FileOutputStream(path);
			 BufferedOutputStream bos=new BufferedOutputStream(fos);
			 while((len=bis.read(bytes))>0)
			 {
				 bos.write(bytes, 0, len);
			 }
			 bos.flush();
			 bos.close();
			 bis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	void splitMonoFile(File file)
	{
		File[] fs=file.listFiles();
		if(fs!=null)
		{
			for(File f:fs)
			{
				count ++;
				if(count%1000==0)
				{
					System.out.println(count);
				}
				Font ft=createNewFont(f.getAbsolutePath(), Font.PLAIN, 12);
				String path=(isMonospacedFont(ft)?outputFolder:outputFolder1)+"\\"+ft.getName()+".ttf";
				if(new File(path).exists())
				{
					path=(isMonospacedFont(ft)?outputFolder:outputFolder1)+"\\"+ft.getName()+rdm.nextLong()+".ttf";
				}
				FileInputStream fis=null;
				try
				{
					 fis=new FileInputStream(f);
					 BufferedInputStream bis=new BufferedInputStream(fis);
					 byte[] bytes=new byte[bis.available()];
					 int len=0;
					 FileOutputStream fos=new FileOutputStream(path);
					 BufferedOutputStream bos=new BufferedOutputStream(fos);
					 while((len=bis.read(bytes))>0)
					 {
						 bos.write(bytes, 0, len);
					 }
					 bos.flush();
					 bos.close();
					 bis.close();
				} catch (Exception e)
				{
					System.out.println("---------------------------");
					System.out.println(f.getAbsolutePath());
					System.out.println(path);
					if(fis!=null)
					{
						try
						{
							fis.close();
						} catch (IOException e1)
						{
							e1.printStackTrace();
						}
					}
					continue;
				}
			}
		}
	}
	
	public static boolean isMonospacedFont(Font f)
	{
		FontRenderContext frc=new FontRenderContext(null, true, false);
		double M=f.getStringBounds("M", frc).getWidth();
		double l=f.getStringBounds("l", frc).getWidth();
		return M==l;
	}
	
	public static Font createNewFont(String ttfFilePath,int fontStyle,int fontSize)
	{
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try
		{
			Font f=Font.createFont(Font.TRUETYPE_FONT, new File(ttfFilePath));
			f=f.deriveFont(fontStyle,fontSize);
//			ge.registerFont(f);
			return f;
		} catch (Exception e)
		{
			System.out.println(ttfFilePath);
			e.printStackTrace();
			return null;
		} 
	}
	
	void delete(File file)
	{
		File[] fs=file.listFiles();
		if(fs!=null)
		{
			for(File f:fs)
			{
				 String name=f.getName();
//				 for(int i=0;i<name.length();i++)
//				 {
//					 if(((int)name.charAt(i))>127)
//					 {
//						 count++;
//						 f.delete();
//						 break;
//					 }
//				 }
//				 if(name.contains(""))
//				 {
//					  count++;
//					  // System.out.println(name);
//					   f.delete();
//				 }
			}
		}
	}
	
	public static void main(String[] args)
	{
		new FileCopyer();
	}
}
