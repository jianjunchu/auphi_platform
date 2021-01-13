package com.aofei.kettle.core.database;

/**
 * 数据库Node类型响应对象
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
public class DatabaseNodeType {
	/**
	 *
	 */
	private static final int SCHEMA = 0x0001;
	private static final int TABLE = 0x0002;
	private static final int VIEW = 0x0004;
	private static final int SYNONYM = 0x0008;

	public static boolean includeSchema(int type) {
		return (type & SCHEMA) > 0;
	}

	public static boolean includeTable(int type) {
		return (type & TABLE) > 0;
	}

	public static boolean includeView(int type) {
		return (type & VIEW) > 0;
	}

	public static boolean includeSynonym(int type) {
		return (type & SYNONYM) > 0;
	}
}
