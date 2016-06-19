package com.petsay.vo.decoration;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangw
 * 饰品的根节点
 */
public class DecorationRoot implements Serializable {
	private static final long serialVersionUID = 4188757555855386714L;
	private String createTime;
	private String[] decorations;
	private String deleted;
	private String id;
	private String leaf;
	private String name;
	private String parentId;
	private List<DecorationTitleBean> children;
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String[] getDecorations() {
		return decorations;
	}
	public void setDecorations(String[] decorations) {
		this.decorations = decorations;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLeaf() {
		return leaf;
	}
	public void setLeaf(String leaf) {
		this.leaf = leaf;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public List<DecorationTitleBean> getChildren() {
		return children;
	}
	public void setChildren(List<DecorationTitleBean> children) {
		this.children = children;
	}
	
	
	
}
