package com.johnsoft.library.util.common;

import java.math.BigDecimal;

public class JohnDegreeUtil {

	/**
	 * 根据输入的度时分数据（例：1104020 = 110°40'20"） 转换为 小数点的度数数据（114.1234） 便于转换为 弧度
	 * @param d 度时分格式数据 例：1104020 = 110°40'20"
	 * @return
	 */
	public static double toDegreeNum(String d){
		d = d.trim();
		double degreeNum = 0;
		String[] nums = new String[3];
		
		String ss = d.substring(d.length() - 2);
		String hh = d.substring(d.length() - 4, d.length() - 2);
		String dd = d.substring(0, d.length() - 4);
		
		nums[0] = dd;
		nums[1] = hh;
		nums[2] = ss;
		
		switch (nums.length) {
		case 3:
			
			degreeNum = Integer.parseInt(nums[0]) + Integer.parseInt(nums[1])/60.0 + Integer.parseInt(nums[2])/3600.0;
			
			break;

		case 2:
	
			degreeNum = Integer.parseInt(nums[0]) + Integer.parseInt(nums[1])/60.0 ;
			
			break;

		case 1:
			
			degreeNum = Integer.parseInt(nums[0]) ;
			
			break;
			
		default:
			break;
		}
		
		return degreeNum;
	}
	
	
	/**
	 * 将小数形式的经纬度转换为时分秒格式 例如：1142041
	 * @param d
	 * @return
	 */
	public static String toDHS(double d){
		//得到小数点前数字即
		double dd = Math.floor(d);
		
		double hh = Math.floor((d-dd) * 60);
		
		double ss = ((d-dd)-(hh/60.0) ) * 3600;//可能存在误差
		
		BigDecimal bB = new BigDecimal(ss);
		ss = bB.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue(); 
		
		//取整数
		String ddstr = String.valueOf((int)dd);
		String hhstr = String.valueOf((int)hh);
		String ssstr = String.valueOf((int)ss);
		
		
		if(ddstr.length()==1){
			ddstr = "00"+ddstr;
		}else if(ddstr.length()==2){
			ddstr = "0"+ddstr;
		}
		
		if(hhstr.length()==1){
			hhstr = "0"+hhstr;
		}
		
		if(ssstr.length()==1){
			ssstr = "0"+ssstr;
		}	
			
		return ddstr +""+hhstr+""+ssstr;
	}
}
