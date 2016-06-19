package com.petsay.vo.petalk;

import java.io.Serializable;

/**
 * @author wangw
 * 宠物说计数器
 */
public class PetalkCounterVo implements Serializable {

	private static final long serialVersionUID = -2614973072855523631L;

	private String id;
	private String petalkId;// 说说ID

	private int comment;// 评论
	private int favour;// 赞
	private int relay;// 转发
	private int share;// 分享
	private long play;// 浏览
	private long createTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPetalkId() {
		return petalkId;
	}
	public void setPetalkId(String petalkId) {
		this.petalkId = petalkId;
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
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public long getPlay() {
		return play;
	}
	public void setPlay(long play) {
		this.play = play;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
