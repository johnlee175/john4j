package com.johnsoft.library.util.common;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class JohnSystemUtil
{
	public static String getMacAddressInWindows()
	{
		String result=null;
		try
		{
			Runtime run=Runtime.getRuntime();
			Process pro=run.exec("cmd.exe /c ipconfig/all");
			InputStream is=pro.getInputStream();
		  BufferedInputStream bis=new BufferedInputStream(is);
			pro.waitFor();
			byte[] bytes=new byte[bis.available()];
			int len=0;
			StringBuffer sb=new StringBuffer();
			while((len=bis.read(bytes))>0)
			{
				sb.append(new String(bytes,0,len,"GBK"));
			}
			bis.close();
			is.close();
			int x1=sb.indexOf("Ethernet");
			String str=sb.substring(x1);
			int x2=str.indexOf("Physical Address");
			int x3=str.indexOf("\n", x2);
			String str2=str.substring(x2, x3);
			result=str2.split(":")[1].trim();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
