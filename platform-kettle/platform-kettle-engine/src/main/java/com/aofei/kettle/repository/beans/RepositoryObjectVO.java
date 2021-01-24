package com.aofei.kettle.repository.beans;

import org.pentaho.di.repository.RepositoryElementMetaInterface;

public class RepositoryObjectVO {

	private String id;
	private String name;
	private String type;
	private String path;
	private String userPath;
	private String modifiedUser;
	private String modifiedDate;

	public RepositoryObjectVO() {

	}

	public RepositoryObjectVO(RepositoryElementMetaInterface re) {
		String path2 = re.getRepositoryDirectory().getPath();
		path2 = path2.endsWith("/") ? path2 : path2 + '/';

		id = path2 + re.getName() + re.getObjectType().getExtension();
		name = re.getName();
		type = re.getObjectType().getExtension();
		path = re.getRepositoryDirectory().getPath();

	  	modifiedUser = re.getModifiedUser();
	  	modifiedDate = "-";
	  	if( re.getModifiedDate() != null) {
	  		modifiedDate = String.format("%1$tY-%1$tm-%1$td", re.getModifiedDate());
	  	}
	}
	public RepositoryObjectVO(RepositoryElementMetaInterface re,String replace) {

		this(re);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getModifiedUser() {
		return modifiedUser;
	}

	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getUserPath() {
		return userPath;
	}

	public void setUserPath(String userPath) {
		this.userPath = userPath;
	}
}
