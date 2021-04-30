package org.firzjb.kettle.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 前端EXTJS 对象
 * @auther 制证数据实时汇聚系统
 * @create 2018-09-15 20:07
 */
public class Ext3CheckableNode extends Ext3Node {

	private boolean checked = false;
	private List children = new ArrayList(0);

	public Ext3CheckableNode(String text) {
		super(text);
	}

	public Ext3CheckableNode(String id, String text) {
		super(id, text);
	}

	public boolean getChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

}
