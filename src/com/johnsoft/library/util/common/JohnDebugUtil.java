package com.johnsoft.library.util.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 调试辅助类,比起System.out.println,强制增加了文件名和行号以便可以删除;对比log,其配置项往往是全局的,该类可以提供更灵活的选项,比如过滤多少次调用造成打印,什么时间段内打印等等;
 * @author john
 */
public class JohnDebugUtil
{
	private static boolean IS_DEBUG=true;
	private static SimpleDateFormat FULL_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static String DEBUG_FILE_PATH=System.getProperty("user.dir")+"/log/"+"debug.log";
	private static final File DEFAULT_FILE=new File(DEBUG_FILE_PATH);
	
	/**带过滤的println和perror的结合*/
	public static final void print_select(String message, boolean isStdErr, JohnDebugSelectStruct struct)
	{
		if(IS_DEBUG)
		{
			long start=Long.valueOf(struct.start_millisecond);
			long end=Long.valueOf(struct.end_millisecond);
			long curr=System.currentTimeMillis();
			boolean timefit=start<end?(start<curr&&curr<end):(curr>start||curr<end);
			if (timefit)
			{
				if (struct.count==1)
				{
					StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
					(isStdErr?System.err:System.out).println("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message);
					struct.count=struct.full;
				}else{
					--struct.count;
				}
			}
		}
	}
	
	/**替代System.out.println*/
	public static final void println(String message)
	{
		if(IS_DEBUG)
		{
			StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
			System.out.println("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message);
		}
	}
	
	/**替代System.err.println,标准错误的特点是不等待缓冲区刷新*/
	public static final void perror(String message)
	{
		if(IS_DEBUG)
		{
			StackTraceElement ste=Thread.currentThread().getStackTrace()[2];
			System.err.println("FROM "+FULL_DATE_FORMAT.format(new Date())+" "+ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+") - Message: "+message);
		}
	}
	
	/**可选择的过滤方式完成write(String)功能,参考JohnDebugSelectStruct*/
	public static final void write_select(String message, JohnDebugSelectStruct struct)
	{
		if(IS_DEBUG)
		{
			long start=Long.valueOf(struct.start_millisecond);
			long end=Long.valueOf(struct.end_millisecond);
			long curr=System.currentTimeMillis();
			boolean timefit=start<end?(start<curr&&curr<end):(curr>start||curr<end);
			if (timefit)
			{
				if (struct.count==1)
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
	
	/**可选择的过滤方式完成write(String, File)功能,参考JohnDebugSelectStruct*/
	public static final void write_select(String message, File file, JohnDebugSelectStruct struct)
	{
		if(IS_DEBUG)
		{
			long start=Long.valueOf(struct.start_millisecond);
			long end=Long.valueOf(struct.end_millisecond);
			long curr=System.currentTimeMillis();
			boolean timefit=start<end?(start<curr&&curr<end):(curr>start||curr<end);
			if (timefit)
			{
				if (struct.count==1)
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
	
	/** 向自动分配的日志文件中写入日志消息message*/
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
	
	/** 向file文件中追加写入日志消息message*/
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
	
	/** 删除日志文件,如果传入null则删除自动分配的日志文件;不应频繁调用*/
	public static final void clean(File file)
	{
		if(file!=null)
		{
			file.delete();
		}else{
			DEFAULT_FILE.delete();
		}
	}
	
	/** 用于write_select,print_select函数的数据类 */
	public static class JohnDebugSelectStruct
	{
		private int count;
		private long start_millisecond;
		private long end_millisecond;
		
		private int full;
		
		/**
		 * 如果starttime小于endtime,则在此封闭区间执行,如果starttime大于endtime,则执行将仅仅绕过此封闭区间;<br/>
		 * 此类应在调用write_select,print_select前初始化一次,而不是每次调用write_select,print_select时现生成一个实例;<br/>
		 * @param starttime 从什么时间开始允许写入或打印日志,格式为"yyyyMMddHHmm",如果为null,将是当前日期的起始点
		 * @param endtime  从什么时间开始不允许继续写入或打印日志,格式为"yyyyMMddHHmm",如果为null,将是当前日期的终止点
		 * @param count 调用此方法多少次后写入或打印日志,值应大于0,默认值是1
		 */
		public JohnDebugSelectStruct(String starttime, String endtime, int count)
		{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
			try
			{
				Calendar cal=Calendar.getInstance();
				if(starttime!=null)
				{
					this.start_millisecond=sdf.parse(starttime).getTime();
				}else{
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					this.start_millisecond=cal.getTimeInMillis();
				}
				if(endtime!=null)
				{
					this.end_millisecond=sdf.parse(endtime).getTime();
				}else{
					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 59);
					cal.set(Calendar.SECOND, 59);
					this.end_millisecond=cal.getTimeInMillis();
				}
				this.count=this.full=count<1?1:count;
			} catch (ParseException e)
			{
				e.printStackTrace();
			}
		}
	}
}
