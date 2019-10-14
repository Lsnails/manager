package com.base.modules.customizesys.upload.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * 得到当前时间的前N小时
	 */
	public static String getBeforeByHourTime(int ihour){
			String returnstr = "";
			Calendar calendar = Calendar.getInstance(); 
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - ihour);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			returnstr = df.format(calendar.getTime());
			return returnstr;
	}
	public static String getNowDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String returnstr = df.format(new Date());
		return returnstr;
}
	public static void main(String[] args) {
		System.out.println(DateUtil.getBeforeByHourTime(24));
		System.out.println(DateUtil.getNowDate());
	}
}
