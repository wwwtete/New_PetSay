package com.petsay.vo.user;

import java.io.Serializable;

/**
 * 宠物计数器
 * 
 * @author G
 * 
 */
public class PetCounter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4405092527896689455L;
	private int comment;
	private long createTime;
	private int fans;
	private int favour;
	private int focus;
	private String id;
	private int issue;
	private String petId;
	private int relay;
	private int share;
	
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}
	public int getFavour() {
		return favour;
	}
	public void setFavour(int favour) {
		this.favour = favour;
	}
	public int getFocus() {
		return focus;
	}
	public void setFocus(int focus) {
		this.focus = focus;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getIssue() {
		return issue;
	}
	public void setIssue(int issue) {
		this.issue = issue;
	}
	public String getPetId() {
		return petId;
	}
	public void setPetId(String petId) {
		this.petId = petId;
	}
	public int getRelay() {
		return relay;
	}
	public void setRelay(int relay) {
		this.relay = relay;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
}
