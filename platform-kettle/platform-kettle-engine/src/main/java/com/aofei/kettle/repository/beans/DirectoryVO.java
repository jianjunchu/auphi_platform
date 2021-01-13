package com.aofei.kettle.repository.beans;

import org.pentaho.di.repository.RepositoryDirectoryInterface;

/**
 * 文件夹对象
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
public class DirectoryVO {
	/**
	 * id
	 */
	private String id;
	/**
	 * 文件夹名称
	 */
	private String name;
	/**
	 * 文件夹全路径
	 */
	private String path;
	/**
	 * 登录用户文件夹显示路径
	 */
	private String userPath;
	/**
	 * 类型 默认dir
	 */
	private String type = "dir";

	public DirectoryVO() {

	}

	public DirectoryVO(RepositoryDirectoryInterface rdi) {
		name = rdi.getName();
		path = rdi.getParent().getPath();

		String path2 = path.endsWith("/") ? path : path + '/';
		id = path2 + name;
	}

	public DirectoryVO(RepositoryDirectoryInterface rdi,String replace) {
		this(rdi);
		userPath = getPath().replace(replace,"/");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserPath() {
		return userPath;
	}

	public void setUserPath(String userPath) {
		this.userPath = userPath;
	}
}
