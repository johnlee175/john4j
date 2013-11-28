package com.johnsoft.library.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JohnStringUtil
{
	/**
	 * 不为空即！=null&&!equals("")
	 */
	public static boolean isNotEmpty(String text)
	{
		return (text!=null&&!text.trim().isEmpty());
	}
	
	/**
	 * 保证字符串数组没有一个为null或""时返回true,否则返回false,采用短路求值
	 */
	public static boolean isNoOneEmpty(String...texts)
	{
		for(String text:texts)
		{
			if(isEmpty(text)) return false;
		}
		return true;
	}
	
	/**
	 * 保证字符串数组没有一个为null时返回true,否则返回false,采用短路求值
	 */
	public static boolean isNoOneNull(String...texts)
	{
		for(String text:texts)
		{
			if(text==null) return false;
		}
		return true;
	}
	
	/**
	 * 判断text为String类型实例且不为空
	 */
	public static boolean isNotEmpty(Object text)
	{
		if(text==null||!(text instanceof String))
		{
			return false;
		}
		String txt=text.toString();
		return isNotEmpty(txt);
	}
	
	/**
	 * 为空即!isNotEmpty(text)
	 */
	public static boolean isEmpty(String text)
	{
		return !isNotEmpty(text);
	}
	
	/**
	 * 判断text为非String类型实例或为空
	 */
	public static boolean isEmpty(Object text)
	{
		if(text==null||!(text instanceof String))
		{
			return true;
		}
		String txt=text.toString();
		return isEmpty(txt);
	}
	
	/**
	 * 判断不为空且不等于数组里的字符串
	 */
	public static boolean isUnemptyAndUnEqual(String text,String... txts)
	{
		if(isNotEmpty(text))
		{
			for(String txt:txts)
			{
				if(text.equals(txt))
				{
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 是否是IP加端口
	 */
	public static boolean isIPAndPort(String text)
	{
		String[] texts=text.split(":");
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(texts[0]);
		if(matcher.find())
		{
			if(isUint(texts[1]))
			{
				int x=Integer.parseInt(texts[1]);
				if(x<65535&&x>255)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 *\w匹配,正则判定法
	 */
	public static boolean isAscii(String text)
	{
		return text.matches("[a-zA-Z_0-9]+");
	}
	
	/**
	 * 是否是英文字母,正则判定法
	 */
	public static boolean isEnglish(String text)
	{
		return text.matches("[a-zA-Z]+");
	}
	
	/**
	 *\w匹配,正则判定法
	 */
	public static boolean isAscii(char c)
	{
		String text=new String(new char[]{c});
		return text.matches("[a-zA-Z_0-9]+");
	}
	
	/**
	 * 是否是英文字母,正则判定法
	 */
	public static boolean isEnglish(char c)
	{
		String text=new String(new char[]{c});
		return text.matches("[a-zA-Z]+");
	}
	
	/**
	 * 是否是中文,正则判定法
	 */
	public static boolean isChinese(String text)
	{
		 return text.matches("[\u4e00-\u9fa5]+");
	}
	
	/**
	 *\w匹配,单字符判断法
	 */
	public static boolean isLetterOrDigit(String text)
	{
		if(text==null||text.isEmpty())
		{
			return false;
		}
		for(int i=0;i<text.length();i++)
		{
			if(!Character.isLetterOrDigit(text.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 是否是英文字母,单字符判断法
	 */
	public static boolean isLetter(String text)
	{
		if(text==null||text.isEmpty())
		{
			return false;
		}
		for(int i=0;i<text.length();i++)
		{
			if(!Character.isLetter(text.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
   *是否是数值,正则判定法
	 */
	public static boolean isNumeric(String text)
	{
		return text.matches("[+-]?(0|[1-9]+[0-9]*)(\\.[0-9]+)?");
	}
	
	/**
	 * 是否是数值,单字符判断法
	 */
	public static boolean isNumber(String text)
	{
		if(text==null||text.isEmpty())
		{
			return false;
		}
		for(int i=0;i<text.length();i++)
		{
			if(!Character.isDigit(text.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 是否是正整数，异常判断法，不含零
	 */
	public static boolean isUint(String text)
	{
		try
		{
			int x=Integer.parseInt(text);
			if(text.startsWith("-")||x<=0)
			{
				return false;
			}else{
				return true;
			}
		} catch (NumberFormatException e)
		{
			return false;
		}
	}
	//判断字符是否是枚举的格式的日期，采用迭代法
	private static boolean parseDate(String text,String[] datePattern,int index)
	{
		  if(index==datePattern.length)
		  {
		  	return false;
		  }
			SimpleDateFormat format=new SimpleDateFormat(datePattern[index]);
			format.setLenient(false);
			try
			{
				format.parse(text);
				return true;
			} catch (ParseException e)
			{
				return	parseDate(text,datePattern,index+1);
			}
	}
	/**
	 * 是否是符合格式的日期
	 * @param datePattern 日期格式的枚举
	 */
	public static boolean isFormattedDate(String text,String... datePattern)
	{
		return parseDate(text,datePattern,0);
	}

}
