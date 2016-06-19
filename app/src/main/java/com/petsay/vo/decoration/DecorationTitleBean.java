package com.petsay.vo.decoration;

import java.io.Serializable;
import java.util.List;


/**
 * @author wangw
 *	装饰标题
 */
public class DecorationTitleBean implements Serializable {
	private static final long serialVersionUID = -596070109021373710L;
	private String id;
	private String createTime;
	private String name;
	private String leaf;
	private String deleted;
	private String parentId;
	private String type;
	private List<DecorationBean> decorations;
	private List<DecorationTitleBean> children;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public List<DecorationBean> getDecorations() {
		return decorations;
	}
	public void setDecorations(List<DecorationBean> decorations) {
		this.decorations = decorations;
	}
	public List<DecorationTitleBean> getChildren() {
		return children;
	}
	public void setChildren(List<DecorationTitleBean> children) {
		this.children = children;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
