package com.johnsoft.library.util.data.ohn;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

public class JohnSimpleOhnWriter
{
	private int ohnInt=-19880127;
	private int step=2;
	
	public void iterateCreateOhn(File file)
	{
		if(file.isFile())
		{
			createOhn(file);
		}
		else
		{
			File[] files=file.listFiles();
			for(File f:files)
			{
				iterateCreateOhn(f);
			}
		}
	}
	
	public void createOhn(File file)
	{
		try
		{
			FileInputStream fis=new FileInputStream(file);
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
			File f=new File(file.getParent()+"\\"+file.getName().split("[.]")[0]+".ohn");
			long ranlong=new Random().nextLong();
			for(int i=0;i<bytes.length;i+=step)
			{
				newBytes[i]=(byte) (newBytes[i]^ohnInt^ranlong);
			}
			FileOutputStream fos=new FileOutputStream(f);
			BufferedOutputStream bos=new BufferedOutputStream(fos);
			DataOutputStream dos=new DataOutputStream(bos);
			dos.writeLong(ranlong);
			dos.flush();
			bos.write(newBytes);
			bos.flush();
			dos.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args)
//	{
//		JohnSimpleOhnWriter writer=new JohnSimpleOhnWriter();
//		writer.createOhn(new File("E:\\bin\\Ohn.jar"));
//	}

}
