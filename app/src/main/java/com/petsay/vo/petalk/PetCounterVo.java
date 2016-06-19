package com.petsay.vo.petalk;

import java.io.Serializable;

public class PetCounterVo implements Serializable {

	private static final long serialVersionUID = -929547832322215645L;

	private String id;// ID
	private String petId;// 说说ID
	private int comment;// 评论
	private int favour;// 赞
	private int relay;// 转发
	private int issue;// 发布
	private int fans;// 粉丝
	private int focus;// 关注
	private int share;// 分享
	private long createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPetId() {
		return petId;
	}

	public void setPetId(String petId) {
		this.petId = petId;
	}

	public int getComment() {
		return comment;
	}

	public void setComment(int comment) {
		this.comment = comment;
	}

	public int getFavour() {
		return favour;
	}

	public void setFavour(int favour) {
		this.favour = favour;
	}

	public int getRelay() {
		return relay;
	}

	public void setRelay(int relay) {
		this.relay = relay;
	}

	public int getIssue() {
		return issue;
	}

	public void setIssue(int issue) {
		this.issue = issue;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public int getFocus() {
		return focus;
	}

	public void setFocus(int focus) {
		this.focus = focus;
	}

	public int getShare() {
		return share;
	}

	public void setShare(int share) {
		this.share = share;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

}
