/**
 * 
 */
package com.k99k.app.orion;

/**
 * Log接口
 * @author keel
 *
 */
public interface LogInterface {

	/**
	 * 保存日志
	 */
	public void saveLog();
	
	/**
	 * 获取标识
	 * @return
	 */
	public int getTag();
	
	/**
	 * 初始化
	 * @return
	 */
	public boolean init();
	
}
