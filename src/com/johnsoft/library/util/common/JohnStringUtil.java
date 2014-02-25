package com.johnsoft.library.util.common;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JohnStringUtil
{
	/**将集合元素按某个字串拼接*/
	public static String join(Collection<String> c,String joinSymbol)
	{
		StringBuffer sb=new StringBuffer("");
		if(c != null)
		{
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
		}
		return sb.toString();
	}
	
	/**
	 * 不为空即"text!=null&&!text.trim().isEmpty()"
	 */
	public static boolean isNotEmpty(String text)
	{
		return (text!=null&&!text.trim().isEmpty());
	}
	
	/**
	 * 为空即"text==null||text.trim().isEmpty()"
	 */
	public static boolean isEmpty(String text)
	{
		return (text==null||text.trim().isEmpty());
	}
	
	/**
	 * 判断text为String类型实例且不为空
	 */
	public static boolean isNotEmptyStr(Object text)
	{
		if(text==null||!(text instanceof String))
		{
			return false;
		}
		String txt=text.toString();
		return isNotEmpty(txt);
	}
	
	/**
	 * 判断text为非String类型实例或为空
	 */
	public static boolean isEmptyStr(Object text)
	{
		if(text==null||!(text instanceof String))
		{
			return true;
		}
		String txt=text.toString();
		return isEmpty(txt);
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
	 * 保证数组没有一个为null时返回true,否则返回false,采用短路求值
	 */
	public static boolean isNoOneNull(Object...texts)
	{
		for(Object text:texts)
		{
			if(text==null) return false;
		}
		return true;
	}
	
	/**
	 * 保证字符串数组全为null或""时返回true,否则返回false,采用短路求值
	 */
	public static boolean isAnyOneEmpty(String...texts)
	{
		for(String text:texts)
		{
			if(isNotEmpty(text)) return false;
		}
		return true;
	}
	
	/**
	 * 保证数组全为null时返回true,否则返回false,采用短路求值
	 */
	public static boolean isAnyOneNull(Object...texts)
	{
		for(Object text:texts)
		{
			if(text!=null) return false;
		}
		return true;
	}
	
	/**
	 * 当字符串数组中有一个为null或""时返回true,否则返回false,采用短路求值
	 */
	public static boolean isSomeOneEmpty(String...texts)
	{
		for(String text:texts)
		{
			if(isEmpty(text)) return true;
		}
		return false;
	}
	
	/**
	 * 当数组中有一个为null时返回true,否则返回false,采用短路求值
	 */
	public static boolean isSomeOneNull(Object...texts)
	{
		for(Object text:texts)
		{
			if(text==null) return true;
		}
		return false;
	}
	
	/**
	 * 当字符串数组中有一个不为null或""时返回true,否则返回false,采用短路求值
	 */
	public static boolean isSomeOneNotEmpty(String...texts)
	{
		for(String text:texts)
		{
			if(isNotEmpty(text)) return true;
		}
		return false;
	}
	
	/**
	 * 当数组中有一个不为null时返回true,否则返回false,采用短路求值
	 */
	public static boolean isSomeOneNotNull(Object...texts)
	{
		for(Object text:texts)
		{
			if(text!=null) return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串不为空且不等于数组里的任意字符串,采用短路求值
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
	 * 判断字符串不为空且等于数组中的某个字符串,采用短路求值
	 */
	public static boolean isEqualAny(String text,String... txts)
	{
		if(isNotEmpty(text))
		{
			for(String txt:txts)
			{
				if(text.equals(txt))
				{
					return true;
				}
			}
			return false;
		}
		return false;
	}
	
	/**
	 * 判定是否是"IP地址:端口"
	 */
	public static boolean isIPAndPort(String text)
	{
		if(isEmpty(text)) return false;
		String[] texts=text.split(":");
		if(texts.length!=2) return false;
		Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2([0-4][0-9]|5[0-5]))\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2([0-4][0-9]|5[0-5]))$");
		Matcher matcher = pattern.matcher(texts[0]);
		if(matcher.find())
		{
			return texts[1].matches("^(0|[1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-5][0-5][0-3][0-5])$");
		}
		return false;
	}
	
	/**
	 *\w匹配,正则判定法
	 */
	public static boolean isAscii(String text)
	{
		return text!=null&&text.matches("[a-zA-Z_0-9]+");
	}
	
	/**
	 * 是否是英文字母,正则判定法
	 */
	public static boolean isEnglish(String text)
	{
		return text!=null&&text.matches("[a-zA-Z]+");
	}
	
	/**
	 * 是否是数字,正则判定法
	 */
	public static boolean isDigit(String text)
	{
		return text!=null&&text.matches("[0-9]+");
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
	 * 是否是数字,正则判定法
	 */
	public static boolean isDigit(char c)
	{
		String text=new String(new char[]{c});
		return text.matches("[0-9]+");
	}
	
	/**
	 * 是否是中文,正则判定法
	 */
	public static boolean isChinese(String text)
	{
		 return text!=null&&text.matches("[\u4e00-\u9fa5]+");
	}
	
	/**
	 * 是否是数值,正则判定法
	 */
	public static boolean isNumeric(String text)
	{
		return text!=null&&text.matches("[+-]?(0|[1-9]+[0-9]*)(\\.[0-9]+)?");
	}
	
	/**
	 *\w匹配,单字符判断法
	 */
	@Deprecated
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
	@Deprecated
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
	 * 是否是数字,单字符判断法
	 */
	@Deprecated
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
	 * 是否是正整数，Long.parseLong + 异常判断法，不含零
	 */
	public static boolean isPositive(String text)
	{
		try
		{	if(isEmpty(text))
				return false;
			else if(text.startsWith("-"))
				return false;
			else if(text.startsWith("+"))
				text=text.substring(1);
			if(Long.parseLong(text)>0)
				return true;
			else
				return false;
		} catch (NumberFormatException e)
		{
			return false;
		}
	}
	
	/**
	 * 是否是负整数，Long.parseLong + 异常判断法，不含零
	 */
	public static boolean isNegative(String text)
	{
		try
		{
			if(isEmpty(text))
				return false;
			else if(!text.startsWith("-"))
				return false;
			if(Long.parseLong(text)<0)
				return true;
			else
				return false;
		} catch (NumberFormatException e)
		{
			return false;
		}
	}
}
