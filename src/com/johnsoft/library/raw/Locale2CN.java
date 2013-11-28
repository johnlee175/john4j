package com.johnsoft.library.raw;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Locale2CN {

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

	public static String date2String(Date date){
		String time = null;
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
			time = format.format(date);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
}
