package com.johnsoft.library.util.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JohnDebugUtil
{
	public static final boolean IS_DEBUG=true;
	public static final SimpleDateFormat FULL_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static final String DEBUG_FILE_PATH=System.getProperty("user.dir")+"/log/"+"debug.log";
	private static final File DEFAULT_FILE=new File(DEBUG_FILE_PATH);
	
	public static final void println(String message)
	{
		if(IS_DEBUG)
		{
			StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
			System.out.println("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message);
		}
	}
	
	public static final void perror(String message)
	{
		if(IS_DEBUG)
		{
			StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
			System.err.println("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message);
		}
	}
	
	public static void write_select(String message, JohnDebugSelectStruct struct)
	{
		if(IS_DEBUG)
		{
			long start=Long.valueOf(struct.start_millisecond);
			long end=Long.valueOf(struct.end_millisecond);
			long curr=System.currentTimeMillis();
			boolean timefit=start<end?(start<curr&&curr<end):(curr>start||curr<end);
			if (timefit)
			{
				if (struct.count==0)
				{
					StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
					try
					{
						BufferedWriter writer=new BufferedWriter(new FileWriter(DEFAULT_FILE, true));
						writer.write("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message+"\n");
						writer.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					struct.count=struct.full;
				}else{
					--struct.count;
				}
			}
		}
	}
	
	public static void write_select(String message, File file, JohnDebugSelectStruct struct)
	{
		if(IS_DEBUG)
		{
			long start=Long.valueOf(struct.start_millisecond);
			long end=Long.valueOf(struct.end_millisecond);
			long curr=System.currentTimeMillis();
			boolean timefit=start<end?(start<curr&&curr<end):(curr>start||curr<end);
			if (timefit)
			{
				if (struct.count==0)
				{
					StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
					try
					{
						BufferedWriter writer=new BufferedWriter(new FileWriter(file, true));
						writer.write("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message+"\n");
						writer.close();
					} catch (IOException e)
					{
						e.printStackTrace();
					}
					struct.count=struct.full;
				}else{
					--struct.count;
				}
			}
		}
	}
	
	public static final void write(String message)
	{
		if(IS_DEBUG)
		{
			StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
			try
			{
				BufferedWriter writer=new BufferedWriter(new FileWriter(DEFAULT_FILE, true));
				writer.write("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message+"\n");
				writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static final void write(String message, File file)
	{
		if(IS_DEBUG)
		{
			StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
			try
			{
				BufferedWriter writer=new BufferedWriter(new FileWriter(file, true));
				writer.write("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message+"\n");
				writer.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static final void clean(File file)
	{
		if(file!=null)
		{
			file.delete();
		}else{
			DEFAULT_FILE.delete();
		}
	}
	
	public static class JohnDebugSelectStruct
	{
		private int count;
		private int full;
		private long start_millisecond;
		private long end_millisecond;
		
		/**
		 * 用于write_select函数的数据类;<br/>
		 * 如果starttime小于endtime,则在此封闭区间执行,如果starttime大于endtime,则执行将仅仅绕过此封闭区间;<br/>
		 * 此类应在调用write_select前初始化一次,而不是每次调用write_select时现生成一个实例;<br/>
		 * @param starttime 从什么时间开始允许写入日志,格式为"yyyyMMddHHmm"
		 * @param endtime  从什么时间开始不允许继续写入日志,格式为"yyyyMMddHHmm"
		 * @param count 调用此方法多少次后写入日志
		 */
		public JohnDebugSelectStruct(String starttime, String endtime, int count)
		{
			 SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
			 try
			{
				this.start_millisecond=sdf.parse(starttime).getTime();
				this.end_millisecond=sdf.parse(endtime).getTime();
				this.count=this.full=count;
			} catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
	}
}
