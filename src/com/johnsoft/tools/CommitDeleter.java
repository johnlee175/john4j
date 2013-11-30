package com.johnsoft.tools;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class CommitDeleter
{
		public static void main(String[] args)
		{
			new CommitDeleter(new File("E:\\JComponent.java"), "utf-8");
		}
	
		public CommitDeleter(File file,String charset)
		{
			try
			{
				StringBuffer sb=new StringBuffer();
				FileInputStream fis=new FileInputStream(file);
				BufferedInputStream bis=new BufferedInputStream(fis);
				byte[] bytes=new byte[bis.available()];
				int len=0;
				while((len=bis.read(bytes))>0)
				{
					sb.append(new String(bytes,0,len,charset));
				}
				bis.close();
				String result=sb.toString();
				result=result.replaceAll("/\\*{1,2}[\\s\\S]*?\\*/", "\n");
				result=result.replaceAll("/{2,}.*?(\r|\n)", "");
				result=result.replaceAll("(\n|\r\n|\r)+", "\n");
				FileOutputStream fos=new FileOutputStream(new File(file.getParent()+"/new.java"));
				BufferedOutputStream bos=new BufferedOutputStream(fos);
				bos.write(result.getBytes(charset));
				bos.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
}
