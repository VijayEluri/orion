package com.k99k.app.orion;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUnit {
	
	private static final DateFormat NORMAL_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat NORMAL_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 获取当前时间,格式为yyyy-MM-dd hh:mm:ss
	 * @return
	 */
	public static final String getNowTime(){
		return NORMAL_TIME_FORMAT.format(new Date());
	}
	
	/**
	 * 获取当前日期,格式为yyyyMMdd
	 * @return
	 */
	public static final String getNowDate(){
		return NORMAL_DATE_FORMAT.format(new Date());
	}
	
	/**
	 * 获取当前时间,指定时间格式
	 * @return
	 */
	public static final String getNowTime(String partern){
		return  new SimpleDateFormat(partern).format(new Date());
	}

	/**
	 * 获取指定时间指定格式
	 * @return
	 */
	public static final String getTime(String partern,Date date){
		return  new SimpleDateFormat(partern).format(date);
	}
}
