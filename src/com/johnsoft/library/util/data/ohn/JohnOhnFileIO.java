package com.johnsoft.library.util.data.ohn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import com.johnsoft.library.util.common.JohnSystemUtil;

public class JohnOhnFileIO
{
	public static String readOhn(File file,String charset)
	{
  	if(charset==null||charset.trim().isEmpty())
  	{
  		return new String(readOhn(file));
  	}else{
  		try
			{
				return new String(readOhn(file),charset);
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
				return null;
			}
  	}
	}
	
	public static void writeOhn(File file,String content,String charset)
	{
		if(content==null||content.trim().isEmpty())
		{
			return;
		}
		if(charset==null||charset.trim().isEmpty())
  	{
			writeOhn(file, content.getBytes());
  	}
		else
		{
  		try
			{
  			writeOhn(file, content.getBytes(charset));
			} catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
  	}
	}
	
	public static byte[] readOhn(File file)
	{
		byte[] result=null;
		if(file==null||!file.exists()||file.isDirectory()||file.length()==0)
		{
			return null;
		}
		if(!file.getName().toLowerCase().endsWith(".ohn"))
		{
			throw new RuntimeException("the file type must be ohn");
		}
		try
		{
			FileInputStream fis=new FileInputStream(file);
			BufferedInputStream bis=new BufferedInputStream(fis);
			DataInputStream dis=new DataInputStream(bis);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			long ranlong=dis.readLong();
			byte[] bytes=new byte[bis.available()];
		  int len=0,ohnInt=0;
		  if(System.getProperty("os.name").toLowerCase().startsWith("windows"))
		  {
		  	byte[] ohnBytes=JohnSystemUtil.getMacAddressInWindows().getBytes();
				for(int i=0;i<ohnBytes.length;i++)
				{
					ohnInt^=ohnBytes[i];
				}
		  }
		  while((len=bis.read(bytes))>0)
		  {
		  	baos.write(bytes, 0, len);
		  }
		  byte[] newBytes=baos.toByteArray();
		  baos.close();
		  result=new byte[newBytes.length];
		  for(int i=0;i<newBytes.length;i++)
	  	{
		  	result[i]=(byte) (newBytes[i]^ohnInt^ranlong);
	  	}
		  dis.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public static void writeOhn(File file,byte[] bytes)
	{
		if(file==null||bytes==null||bytes.length<=0)
		{
			return;
		}
		if(!file.getName().toLowerCase().endsWith(".ohn"))
		{
			throw new RuntimeException("the file type is not ohn");
		}
		try
		{
			int ohnInt=0;
			if(System.getProperty("os.name").toLowerCase().startsWith("windows"))
		  {
				byte[] ohnBytes=JohnSystemUtil.getMacAddressInWindows().getBytes();
				for(int i=0;i<ohnBytes.length;i++)
				{
					ohnInt^=ohnBytes[i];
				}
		  }
			long ranlong=new Random().nextLong();
			byte[] newBytes=new byte[bytes.length];
			for(int i=0;i<bytes.length;i++)
			{
				newBytes[i]=(byte) (bytes[i]^ohnInt^ranlong);
			}
			FileOutputStream fos=new FileOutputStream(file);
			BufferedOutputStream bos=new BufferedOutputStream(fos);
			DataOutputStream dos=new DataOutputStream(bos);
			dos.writeLong(ranlong);
			dos.flush();
			bos.write(newBytes);
			bos.flush();
			dos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void createOhn(File sourceFile,File targetFile)
	{
		try
		{
			FileInputStream fis=new FileInputStream(sourceFile);
			BufferedInputStream bis=new BufferedInputStream(fis);
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			byte[] bytes=new byte[bis.available()];
			int len=0;
			while((len=bis.read(bytes))>0)
			{
				baos.write(bytes, 0, len);
			}
			byte[] newBytes=baos.toByteArray();
			baos.close();
			bis.close();
			writeOhn(targetFile, newBytes);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
