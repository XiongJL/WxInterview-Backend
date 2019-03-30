package com.liwinon.interview.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
	public static Date strToDateTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		try {
			Date date  =  sdf.parse(str);
			return date ;
		} catch (ParseException e) {
			System.out.println("转换日期失败");
			e.printStackTrace();
		}
		return null;
	}
	/* 
     *	将时间转换为时间戳
     */    
    public static String dateToStamp(String s) {
        String res;
        Date date = strToDateTime(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
    
    public static Date nowDate() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date now = new Date();
    	Date result;
		try {
			result = sdf.parse(sdf.format(now));
			return result;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return null;
    }
}
