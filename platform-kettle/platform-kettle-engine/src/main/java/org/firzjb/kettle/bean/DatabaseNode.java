package org.firzjb.kettle.bean;

/**
 * 数据库连接响应 Node对象
 * @auther 傲飞数据整合平台
 * @create 2018-09-15 20:07
 */
public class DatabaseNode extends Ext3Node {

	public DatabaseNode(String text) {
		super(text);
	}

	/**
	 * Node对象Id 数据库ID
	 */
	private String nodeId;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 *
	 */
	private String schema;

	/**
	 *
	 * @return
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 *
	 * @param schema
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public static DatabaseNode initNode(String text, String nodeId) {
		return initNode(text, nodeId, false);
	}

	/**
	 * 初始化一个DatabaseNode对象
	 * @param text
	 * @param nodeId
	 * @param iconCls
	 * @return
	 */
	public static DatabaseNode initNode(String text, String nodeId, String iconCls) {
		return initNode(text, iconCls, nodeId, true);
	}

	/**
	 * 初始化一个DatabaseNode对象
	 * @param text
	 * @param nodeId
	 * @param iconCls
	 * @param leaf
	 * @return
	 */
	public static DatabaseNode initNode(String text, String nodeId, String iconCls, boolean leaf) {
		return initNode(text, iconCls, nodeId, leaf, false);
	}

	/**
	 * 初始化一个DatabaseNode对象
	 * @param text
	 * @param nodeId
	 * @param expanded
	 * @return
	 */
	public static DatabaseNode initNode(String text, String nodeId, boolean expanded) {
		return initNode(text, null, nodeId, false, expanded);
	}

	/**
	 * 初始化一个DatabaseNode对象
	 * @param text 数据库名称
	 * @param iconCls 页面显示icon
	 * @param nodeId 数据库ID
	 * @param leaf 是否有子集
	 * @param expanded 是否展开
	 * @return
	 */
	public static DatabaseNode initNode(String text, String iconCls, String nodeId, boolean leaf, boolean expanded) {
		DatabaseNode node = new DatabaseNode(text);
		node.setLeaf(leaf);
		if(iconCls == null && !leaf)
			node.setIconCls("imageFolder");
		else
			node.setIconCls(iconCls);
		node.setExpanded(expanded);
		node.setNodeId(nodeId);
		return node;
	}

}
