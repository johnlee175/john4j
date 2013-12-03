package com.johnsoft.tools;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ChangEncode
{
	public ChangEncode(String srcpath,String trgtdrive)
	{
		 gbkToUtf8(new File(srcpath),trgtdrive);
	}
	
	private void gbkToUtf8(File file,String drive)
	{
		if(file.isDirectory())
		{
			new File(drive+file.getAbsolutePath().substring(1)).mkdirs();
			for(File f:file.listFiles())
			{
				gbkToUtf8(f,drive);
			}
		}else{
			try
			{
				BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
				byte[] sz=new byte[br.available()];
				BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(drive+file.getAbsolutePath().substring(1)));
				int len=0;
				StringBuffer sb=new StringBuffer();
				while((len=br.read(sz))>0)
				{
					sb.append(new String(sz,0,len,"gbk"));
				}
				String result=sb.toString();
				bw.write(result.getBytes("utf-8"));
				bw.close();
				br.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		new ChangEncode("F:\\src","G");
	}
}
