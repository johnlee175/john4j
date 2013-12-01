package com.johnsoft.library.util.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 杂项辅助类
 * 
 * @author 李哲浩
 */
public class JohnDateHelper {
	private static SimpleDateFormat format;

	public static final SimpleDateFormat datetimeFormat = new FixedDateFormat(
			"yyyyMMddHHmm");
	public static final SimpleDateFormat HHiMMTimeFormat = new FixedDateFormat(
			"HH:mm");
	public static final SimpleDateFormat dateFormat = new FixedDateFormat(
			"yyyyMMdd");

	public static Long diffmm(String date1, String date2, SimpleDateFormat sdf) {
		try {
			long millsec1 = sdf.parse(date1).getTime();
			long millsec2 = sdf.parse(date2).getTime();
			return (millsec1 - millsec2) / (1000 * 60);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回指定格式的SimpleDateFormat
	 */
	public static SimpleDateFormat getDateFormat(String pattern) {
		if (format == null) {
			format = new SimpleDateFormat(pattern);
		} else if (!format.toPattern().equals(pattern)) {
			format.applyPattern(pattern);
		}
		return format;
	}

	/**
	 * @return 当天的Date
	 */
	public static Date getNow() {
		return new Date();
	}

	/**
	 * @return 当天的Calendar
	 */
	public static Calendar getToday() {
		return Calendar.getInstance();
	}

	/**
	 * @return 返回今天的格式化字符串
	 */
	public static String getNowString(String pattern) {
		return getDateFormat(pattern).format(getNow());
	}

	/**
	 * @return 今天日期的最大时间23:59:59的指定格式字符串
	 */
	public static String getTodayMaxTime(String pattern) {
		Calendar cal = getToday();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return getDateFormat(pattern).format(cal.getTime());
	}

	/**
	 * @return 今天日期的最小时间00:00:00的指定格式字符串
	 */
	public static String getTodayMinTime(String pattern) {
		Calendar cal = getToday();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return getDateFormat(pattern).format(cal.getTime());
	}

	/**
	 * @return 日期格式为yyyyMMddHHmm的字符串中的日期部分
	 */
	public static String getDate(String text) {
		if (JohnStringUtil.isNotEmpty(text)) {
			return text.substring(0, 8);
		}
		return "";
	}

	/**
	 * @return 日期格式为yyyyMMddHHmm的字符串中的时间部分
	 */
	public static String getTime(String text) {
		if (JohnStringUtil.isNotEmpty(text)) {
			return text.substring(8);
		}
		return "";
	}

	/**
	 * 
	 * @return text按","分割后取第i个字符串作为日期字符串获取其中的时间部分
	 */
	public static String getTimeWithSplit(String text, int i) {
		if (JohnStringUtil.isNotEmpty(text)) {
			return getTime(text.split(",")[i]);
		}
		return "";
	}

	/**
	 * @param othAdd
	 *            额外的增值
	 * @param othSub
	 *            额外的减值
	 * @return yyyyMMddHHmm格式的日期文本的时间差,分钟计,并加上额外的增值减掉额外的减值
	 */
	public static String getTimeDifference(String text1, String text2,
			int othAdd, int othSub) {
		if (JohnStringUtil.isNotEmpty(text1)
				&& JohnStringUtil.isNotEmpty(text2)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
			try {
				long t1 = format.parse(text1).getTime();
				long t2 = format.parse(text2).getTime();
				long ms = (t1 - t2) / (1000 * 60) + othAdd - othSub;
				if (ms <= 0) {
					return "";
				}
				return new Long(ms).toString();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * text是否是yyyyMMdd格式的今天日期
	 */
	public static boolean isToday(String text) {
		if (JohnStringUtil.isNotEmpty(text)) {
			if (text.startsWith(getNowString("yyyyMMdd"))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * text是否是指定格式的日期
	 */
	public static boolean isDate(String pattern, String text) {
		SimpleDateFormat sdf = getDateFormat(pattern);
		try {
			sdf.parse(text);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	/** 判断是否是相同日期 */
	public static boolean isSameDateTime(String pattern, String text1,
			String text2) {
		SimpleDateFormat sdf = getDateFormat(pattern);
		try {
			Date date1 = sdf.parse(text1);
			Date date2 = sdf.parse(text2);
			return date1.getTime() == date2.getTime() ? true : false;
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 将日期文本从一种格式转换到另一种格式
	 */
	public static String formatDateString(String text, String fromPattern,
			String toPattern) {
		if (JohnStringUtil.isNotEmpty(text)) {
			try {
				Date date = getDateFormat(fromPattern).parse(text);
				return getDateFormat(toPattern).format(date);
			} catch (ParseException e) {
				return "";
			}
		}
		return "";
	}

	/**
	 * Date实现,指定日期格式下两个字符串时间值的大小比较,前一个小返回true
	 */
	public static boolean isTimeOfFirstItemLower(String pattern, String first,
			String second) {
		SimpleDateFormat sdf = getDateFormat(pattern);
		try {
			Date date1 = sdf.parse(first);
			Date date2 = sdf.parse(second);
			if (date1.getTime() < date2.getTime()) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Number实现,指定日期格式下两个字符串时间值的大小比较,前一个小则返回true,后一个小在返回false,
	 * 如果发生解析异常或其他状况将返回defaults指定的值
	 */
	public static boolean isTimeOfFirstItemLower(String first, String second,
			boolean defaults) {
		try {
			long f = Long.parseLong(first);
			long s = Long.parseLong(second);
			if (f < s)
				return true;
			else
				return false;
		} catch (NumberFormatException e) {
		}
		return defaults;
	}

	private static class FixedDateFormat extends SimpleDateFormat {
		private static final long serialVersionUID = 1L;

		public FixedDateFormat(String pattern) {
			super(pattern);
		}

		public void applyLocalizedPattern(String str) {
		}

		public void applyPattern(String str) {
		}
	}

	private final static Locale chineseLocal = new Locale("zh", "CN");

	private final static DateFormat currentDateFormat = DateFormat
			.getDateInstance(DateFormat.MEDIUM, chineseLocal);

	private final static DateFormat currentTimeFormat = DateFormat
			.getTimeInstance(DateFormat.MEDIUM, chineseLocal);

	public static String date2LocalString(Date date) {
		String tmp = currentDateFormat.format(date) + " "
				+ currentTimeFormat.format(date);
		return tmp;
	}

	public static String date2String(Date date) {
		String time = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
			time = format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	// 判断字符是否是枚举的格式的日期，采用迭代法
	private static boolean parseDate(String text, String[] datePattern,
			int index) {
		if (index == datePattern.length) {
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat(datePattern[index]);
		format.setLenient(false);
		try {
			format.parse(text);
			return true;
		} catch (ParseException e) {
			return parseDate(text, datePattern, index + 1);
		}
	}

	/**
	 * 是否是符合格式的日期
	 * 
	 * @param datePattern
	 *            日期格式的枚举
	 */
	public static boolean isFormattedDate(String text, String... datePattern) {
		return parseDate(text, datePattern, 0);
	}
}
