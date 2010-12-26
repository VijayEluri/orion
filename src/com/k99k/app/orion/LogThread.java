/**
 * 
 */
package com.k99k.app.orion;

import java.util.concurrent.CopyOnWriteArrayList;

import com.k99k.tools.StringUnit;

/**
 * 异步实现log
 * @author keel
 *
 */
public class LogThread implements Runnable{
	
	
	private static final int TAG_TXT = 1;
	private static final int TAG_MONGO = 2;
	private static final int TAG_SQLSERVER = 3;
	

	
	/**
	 * 处理日志的List
	 */
	private static CopyOnWriteArrayList<String> logList;

	@Override
	public void run() {
		
	}

	
	/**
	 * 初始化
	 * 将各个Log接口实现按TAG放入数组,并初始化各接口
	 * 
	 */
	private void init(){
		
	}
	
	
	/**
	 * 添加Log，同时将时间插入
	 * @param s Log字符串
	 */
	public static final void addLog(String s){
		logList.add(StringUnit.getNowTime()+" ## "+s);
	}
	
	/**
	 * 添加Log，同时将时间插入
	 * @param s Log字符串
	 * @param tag 日志存储标识
	 */
	public static final void addLog(String s,int tag){
		logList.add(StringUnit.getNowTime()+" ## "+s);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
}
