package com.johnsoft.tools;

import java.io.IOException;
import java.io.InputStream;

public class Runtimeout
{
	public static void main(String[] args)
	{
		Runtime runtime=Runtime.getRuntime();
		try
		{
			Process process=runtime.exec("ipconfig /all");//查看mac地址
			InputStream is=process.getInputStream();
			byte[] bytes=new byte[1024];
			int length=is.read(bytes);
			String str=new String(bytes,0,length);
			System.out.println(str);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
