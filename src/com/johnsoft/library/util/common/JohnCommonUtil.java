package com.johnsoft.library.util.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;

/**
 * 可复用的通用Object的通用操作工具类
 * 
 * @author 李哲浩
 */
public class JohnCommonUtil
{
	/** 极简单封装Thread.sleep(),仅去掉异常 */
	public static void sleepThread(long millSecd)
	{
		try
		{
			Thread.sleep(millSecd);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 深度拷贝任意对象的所有属性和操作
	 * 
	 * @param obj
	 *            要拷贝的对象
	 * @return 对象的拷贝
	 */
	public static Object deepCopy(Object obj)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.flush();
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object objx = ois.readObject();
			ois.close();
			bais.close();
			oos.close();
			baos.close();
			return objx;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return 获取PID
	 */
	public static int getProcessId()
	{
		RuntimeMXBean driver = ManagementFactory.getRuntimeMXBean();
		String name = driver.getName();
		return Integer.parseInt(name.split("@")[0]);
	}

	/** 获取分级别的北京时间,由于查询服务器,可能在网络传输中丢失秒级别的精度 */
	public static Date getBeijingDateTime()
	{
		try
		{
			URL url = new URL("http://www.c168.com/bzsj/");
			URLConnection uc = url.openConnection();
			uc.connect();
			long millisecond = uc.getDate();
			return new Date(millisecond);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void writeByEncoding(String sourcePath, String binaryPath, int magicNumber)
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
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static ByteArrayInputStream readByDecoding(String path, int magicNumber)
	{
		ByteArrayInputStream bais = null;
		try
		{
			DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(
					path)));
			if (dis.readInt() != magicNumber)
			{
				dis.close();
				throw new RuntimeException("Unable to identify the file!");
			}
			byte[] bytes = new byte[dis.available()];
			int len = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = dis.read(bytes)) > 0)
			{
				baos.write(bytes, 0, len);
			}
			byte[] bs = baos.toByteArray();
			baos.close();
			dis.close();
			for (int i = 0; i < bs.length; i++)
			{
				bs[i] = (byte) (bs[i] ^ magicNumber);
			}
			bais = new ByteArrayInputStream(bs);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bais;
	}

	public static Boolean hasInstance()
	{
		try
		{
			final File file = new File("singleton.lock");
			// Check if the lock exist
			if (file.exists())
			{// if exist try to delete it
				file.delete();
			}
			// Try to get the lock
			@SuppressWarnings("resource")
			final FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
			final FileLock lock = channel.tryLock();
			if (lock == null)
			{
				// File is lock by other application
				channel.close();
				return true;
			}
			// Add shutdown hook to release lock when application shutdown
			Runtime.getRuntime().addShutdownHook(new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						if (lock != null)
						{
							lock.release();
							channel.close();
							file.delete();
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			});
			Thread.sleep(500);
			return false;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static double max(double... ds)
	{
		double maxv = ds[0];
		for (int i = 1; i < ds.length; i++)
		{
			maxv = Math.max(ds[i], maxv);
		}
		return maxv;
	}

	public static double min(double... ds)
	{
		double minv = ds[0];
		for (int i = 1; i < ds.length; i++)
		{
			minv = Math.min(ds[i], minv);
		}
		return minv;
	}

	/**
	 * 检查是否发生了不可捕获的异常,这里的不可捕获异常仅只异常在实现端已经捕获,在客户端调用时无法知道是否发生了异常的情况,注意:
	 * 实现端在捕获异常的时候必须有printStackTrace等相关动作.
	 * 
	 * @param exceptionClassName
	 *            异常类的全名,比如:"java.net.SocketException"
	 * @param runnable
	 *            携带已经捕获exceptionClassName异常的方法的代码段,注意,这里的run方法不会被新线程调用,
	 *            仅仅是作为代码段的载体
	 */
	public static boolean happenUncatchableException(String exceptionClassName, Runnable runnable)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = System.err;
		System.setErr(new PrintStream(baos));
		runnable.run();
		String str = new String(baos.toByteArray());
		System.setErr(ps);
		return str.startsWith(exceptionClassName);
	}

	/** ASCII码转BCD码 */
	public static byte[] asc2bcd(byte[] ascii, int asc_len)
	{
		byte[] bcd = new byte[asc_len / 2];
		int j = 0;
		for (int i = 0; i < (asc_len + 1) / 2; i++)
		{
			bcd[i] = asc2bcd(ascii[j++]);
			bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc2bcd(ascii[j++])) + (bcd[i] << 4));
		}
		return bcd;
	}

	/** ASCII码转BCD码 */
	public static byte asc2bcd(byte asc)
	{
		byte bcd;
		if ((asc >= '0') && (asc <= '9'))
			bcd = (byte) (asc - '0');
		else if ((asc >= 'A') && (asc <= 'F'))
			bcd = (byte) (asc - 'A' + 10);
		else if ((asc >= 'a') && (asc <= 'f'))
			bcd = (byte) (asc - 'a' + 10);
		else
			bcd = (byte) (asc - 48);
		return bcd;
	}

	/**
	 * BCD转字符串
	 */
	public static String bcd2str(byte[] bytes)
	{
		char temp[] = new char[bytes.length * 2], val;
		for (int i = 0; i < bytes.length; i++)
		{
			val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
			temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
			val = (char) (bytes[i] & 0x0f);
			temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
		}
		return new String(temp);
	}

}
