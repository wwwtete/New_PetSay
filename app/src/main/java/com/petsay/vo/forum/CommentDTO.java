package com.petsay.vo.forum;

import java.io.Serializable;

public class CommentDTO implements Serializable  {

	
	private static final long serialVersionUID = -2282531198799702014L;
	
	private String id;
	private String petId;
	private String petNickName;
	private String petAvatar;
	private String atPetId;
	private String atPetNickName;
	private String comment;
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
	public String getPetNickName() {
		return petNickName;
	}
	public void setPetNickName(String petNickName) {
		this.petNickName = petNickName;
	}
	public String getAtPetId() {
		return atPetId;
	}
	public void setAtPetId(String atPetId) {
		this.atPetId = atPetId;
	}
	public String getAtPetNickName() {
		return atPetNickName;
	}
	public void setAtPetNickName(String atPetNickName) {
		this.atPetNickName = atPetNickName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getPetAvatar() {
		return petAvatar;
	}
	public void setPetAvatar(String petAvatar) {
		this.petAvatar = petAvatar;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
