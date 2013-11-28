package com.johnsoft.library.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import com.johnsoft.library.util.data.fn.JohnOvoidIvoidFn;
/**
 * 可复用的通用Object的通用操作工具类
 * @author 李哲浩
 */
public class JohnCommonUtil
{
	/**极简单封装Thread.sleep(),仅去掉异常*/
	public static void sleepThread(long millSecd)
	{
		try
		{
			Thread.sleep(millSecd);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 计划在执行超时时弹窗提醒
	 * @param obj  监视的对象
	 * @param field 此对象的属性,超时执行的依据是此对象为null
	 * @param thread 监视是否超时执行的所在线程
	 * @param delay 以多少毫秒为超时
	 * @param errorTip 超时时杀掉超时执行线程,并提示什么
	 */
	public static void catchTimeout(final Object obj,final String field,final long delay,final Thread thread,final String errorTip,final JohnOvoidIvoidFn fn)
	{
		new Timer().schedule(new TimerTask()
		{
			public void run()
			{
				try
				{
					Field f=obj.getClass().getDeclaredField(field);
					f.setAccessible(true);
					Object attr=f.get(obj);
					if(attr==null)
					{
						if(thread!=null)
							thread.interrupt();
						if(errorTip!=null)
							JOptionPane.showMessageDialog(null, errorTip, "超时", JOptionPane.ERROR_MESSAGE);
						if(fn!=null)
						    fn.function();
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				} 
			}
		}, delay);
	}
	
	/**
	 * 深度拷贝任意对象的所有属性和操作
	 * @param obj 要拷贝的对象
	 * @return 对象的拷贝
	 */
	public static Object deepCopy(Object obj)
	{
		try
		{
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.flush();
			ByteArrayInputStream bais=new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois=new ObjectInputStream(bais);
			Object objx=ois.readObject();
			ois.close();
			bais.close();
			oos.close();
			baos.close();
			return objx;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**将任何类型转换成boolean类型以便用于if或while等的条件判断中,以企图实现类cpp风格*/
	/*public static boolean bool(Object obj)
	{
		if(obj==null)
		{
			return false;
		}
		if(obj instanceof Boolean)
		{
			return (Boolean)obj;
		}
		if(obj instanceof Number)
		{
			return ((Number)obj).doubleValue()==0;
		}
		if(obj instanceof Character)
		{
			return ((Character)obj).charValue()==0;
		}
		return true;
	}*/
	
	/**
	 * @return 获取PID
	 */
	public static int getProcessId()
	{
		RuntimeMXBean driver=ManagementFactory.getRuntimeMXBean();
		String name=driver.getName();
	    return Integer.parseInt(name.split("@")[0]);  
	}
	
	/**objs中有一个为null,即为false*/
	public static boolean isNoOneNull(Object...objs)
	{
		for(Object obj:objs)
		{
			if(obj==null) return false;
		}
		return true;
	}
	
	/**将集合元素按某个字串拼接*/
	public static String join(Collection<String> c,String joinSymbol)
	{
		StringBuffer sb=new StringBuffer("");
		for(String text:c)
		{
			sb.append(text).append(joinSymbol);
		}
		int sblen=sb.length();
		int symlen=joinSymbol.length();
		if(sblen>symlen)
		{
			sb.delete(sblen-symlen, sblen);
		}
		return sb.toString();
	}
	
	/**获取分级别的北京时间,由于查询服务器,可能在网络传输中丢失秒级别的精度*/
	public static Date getBeijingDateTime()
	{
		try
		{
			URL url=new URL("http://www.c168.com/bzsj/");
			URLConnection uc=url.openConnection();
			uc.connect(); 
			long  millisecond = uc.getDate();
			return new Date(millisecond); 
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		} 
	}
	
	public static void writeByEncoding(String sourcePath,String binaryPath,int magicNumber)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(binaryPath);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(magicNumber);
			dos.flush();
			FileInputStream fis = new FileInputStream(sourcePath);
			byte[] bytes = new byte[fis.available()];
			int len = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = fis.read(bytes)) > 0)
			{
				baos.write(bytes, 0, len);
			}
			byte[] bs = baos.toByteArray();
			baos.close();
			for (int i = 0; i < bs.length; i++)
			{
				bs[i] = (byte) (bs[i] ^ magicNumber);
			}
			dos.write(bs);
			dos.flush();
			fis.close();
			dos.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ByteArrayInputStream readByDecoding(String path,int magicNumber)
	{
		ByteArrayInputStream bais=null;
		try
		{
			DataInputStream dis = new DataInputStream(new BufferedInputStream(
					new FileInputStream(path)));
			if(dis.readInt()!=magicNumber)
			{
				dis.close();
				throw new RuntimeException("Unable to identify the file!");
			}
			byte[] bytes=new byte[dis.available()];
			int len=0;
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			while((len=dis.read(bytes))>0)
			{
				baos.write(bytes, 0, len);
			}
			byte[] bs=baos.toByteArray();
			baos.close();
			dis.close();
			for(int i=0;i<bs.length;i++)
			{
				bs[i]=(byte) (bs[i]^magicNumber);
			}
			bais = new ByteArrayInputStream(bs);
		} catch (Exception e)
		{
			e.printStackTrace();
		} 
		return bais;
	}
	
	public static String encryptMD5(String message)
	{
		try 
		{
			MessageDigest md=MessageDigest.getInstance("MD5");
			md.update(message.getBytes());
			MessageDigest mdc = (MessageDigest) md.clone();
		    byte[] digest = mdc.digest();
		    return byte2hex(digest);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String encryptSHA1(String message)
	{
		try
		{
			MessageDigest md=MessageDigest.getInstance("SHA-1");
			md.update(message.getBytes());
		    byte[] digest = md.digest();
		    return byte2hex(digest);
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String encryptSHA256(String message)
	{
		try
		{
		    return byte2hex(MessageDigest.getInstance("SHA-256").digest(message.getBytes()));
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String byte2hex(byte[] bytes)
	{
		String hs="";
		String stmp="";
		for(int i=0;i<bytes.length;i++)
		{
			stmp=Integer.toHexString(bytes[i]&0XFF);
			if(stmp.length()==1) 
				hs=hs+"0"+stmp;
			else
				hs=hs+stmp;
		}
		return hs.toUpperCase();
	}
}
