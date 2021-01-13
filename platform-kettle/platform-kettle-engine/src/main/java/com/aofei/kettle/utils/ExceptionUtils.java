package com.aofei.kettle.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
public class ExceptionUtils {

	/**
	 * 异常转String字符串
	 * @param e
	 * @return
	 */
	public static String toString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

}
