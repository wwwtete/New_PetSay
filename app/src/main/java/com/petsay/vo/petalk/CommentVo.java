package com.petsay.vo.petalk;

import java.io.Serializable;

public class CommentVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7219968638132341379L;
	private String id;
	private String type;
	private String petalkId;
	private String petId;
	private String petHeadPortrait;
	private String aimPetId;
	private String petNickName;
	private String aimPetHeadPortrait;
	private String aimPetNickName;
	private String comment;
	private String commentAudioUrl;
	private String commentAudioSecond;
	private long createTime;
	
	private String rs;
	private String z;
	
	
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAimPetHeadPortrait() {
		return aimPetHeadPortrait;
	}
	public void setAimPetHeadPortrait(String aimPetHeadPortrait) {
		this.aimPetHeadPortrait = aimPetHeadPortrait;
	}
	public String getAimPetId() {
		return aimPetId;
	}
	public void setAimPetId(String aimPetId) {
		this.aimPetId = aimPetId;
	}
	public String getAimPetNickName() {
		return aimPetNickName;
	}
	public void setAimPetNickName(String aimPetNickName) {
		this.aimPetNickName = aimPetNickName;
	}
	public String getComment() {
		return comment.trim().replaceAll(" ", "");
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentAudioSecond() {
		return commentAudioSecond;
	}
	public void setCommentAudioSecond(String commentAudioSecond) {
		this.commentAudioSecond = commentAudioSecond;
	}
	public String getCommentAudioUrl() {
		return commentAudioUrl;
	}
	public void setCommentAudioUrl(String commentAudioUrl) {
		this.commentAudioUrl = commentAudioUrl;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getPetHeadPortrait() {
		return petHeadPortrait;
	}
	public void setPetHeadPortrait(String petHeadPortrait) {
		this.petHeadPortrait = petHeadPortrait;
	}
	public String getPetId() {
		return petId;
	}
	public void setPetId(String petId) {
		this.petId = petId;
	}
	public String getPetNickName() {
		return petNickName;
	}
	public void setPetNickName(String petNickName) {
		this.petNickName = petNickName;
	}
	public String getPetalkId() {
		return petalkId;
	}
	public void setPetalkId(String petalkId) {
		this.petalkId = petalkId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getRs() {
		return rs;
	}
	public void setRs(String rs) {
		this.rs = rs;
	}
	public String getZ() {
		return z;
	}
	public void setZ(String z) {
		this.z = z;
	}
	
}
