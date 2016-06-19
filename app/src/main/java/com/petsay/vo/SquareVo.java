package com.petsay.vo;

import java.io.Serializable;

public class SquareVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 675964001519458477L;
	
	private String description;
	private int displayType;
	private int handleType;
	private String iconUrl;
	private String key;
	//排序 暂时无用
	private int seqNum;
	private String title;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDisplayType() {
		return displayType;
	}
	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}
	public int getHandleType() {
		return handleType;
	}
	public void setHandleType(int handleType) {
		this.handleType = handleType;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
