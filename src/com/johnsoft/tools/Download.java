package com.johnsoft.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class Download
{
	public static void main(String[] args) throws Exception
	{
		String target="D:\\workspace\\Meimei\\";
		int i=0;
		while(i<8)
		{
			String url1="http://m4.auto.itc.cn/car/800/0"+i+"/26/";
			String url2="Img253260"+i+"_800.jpg";
			download(url1+url2,target+url2);
			i++;
		}
	}
	
	public static void download(String urlStr,String target) throws Exception
	{
		URL url=new URL(urlStr);
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(target));
		BufferedInputStream bis=new BufferedInputStream(url.openStream());
		byte[] bytes=new byte[1024];
		int len;
		while((len=bis.read(bytes))>0)
		{
			bos.write(bytes,0,len);
		}
		bos.close();
		bis.close();
	}
}
