package com.johnsoft.library.raw;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeUtil
{
	public static final SimpleDateFormat datetimeFormat=new FixedDateFormat("yyyyMMddHHmm");
	public static final SimpleDateFormat HHiMMTimeFormat=new FixedDateFormat("HH:mm");
	public static final SimpleDateFormat dateFormat=new FixedDateFormat("yyyyMMdd");
	
	public static Long diffmm(String date1,String date2,SimpleDateFormat sdf)
	{
		try
		{
			long millsec1=sdf.parse(date1).getTime();
			long millsec2=sdf.parse(date2).getTime();
			return (millsec1-millsec2)/(1000*60);  
		} catch (ParseException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	private static class FixedDateFormat extends SimpleDateFormat
	{
		private static final long serialVersionUID = 1L;
		
		public FixedDateFormat(String pattern)
		{
			super(pattern);
		}
		
		public void applyLocalizedPattern(String str) {}
		public void applyPattern(String str) {}
	}
}
