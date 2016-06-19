package com.petsay.vo.forum;

import java.io.Serializable;

/**
 * 评论
 * 
 * @author xiaoyi
 *
 */
public class TopicCommentDTO implements Serializable {

	private static final long serialVersionUID = 5736831815482750412L;
	
	private String id;
	private String petId;
	private String petNickName;
	private String petAvatar;
	
	private String atPetId;
	private String atPetNickName;
	private String comment;
	private String createTime;
	
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
	public String getPetAvatar() {
		return petAvatar;
	}
	public void setPetAvatar(String petAvatar) {
		this.petAvatar = petAvatar;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	
}
