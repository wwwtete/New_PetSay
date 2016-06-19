package com.petsay.vo.forum;

import java.io.Serializable;
import java.util.List;

/**
 * 宠物的讨论
 */
public class TalkDTO implements Serializable {

	private static final long serialVersionUID = -6446644259287777187L;
	
	private String id;
	private String petId;
	private String petAvatar;//头像
	private String petNickName;
	private String content;
	private String petGender;
 	private String petStar;
	private PicDTO[] pictures;
	private List<CommentDTO> comments;
//	private CommentDTO[] comments;
	private int likeCount;
	private int commentCount;
	private boolean top;
	private boolean isLiked;
	private long createTime;
	
	//客户端定义 第一条出彩讨论为1，第一条全部讨论为2，其他均为0(默认为0)
	private int talkType=0;
	
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
	public String getPetAvatar() {
		return petAvatar;
	}
	public void setPetAvatar(String petAvatar) {
		this.petAvatar = petAvatar;
	}
	public String getPetNickName() {
		return petNickName;
	}
	public void setPetNickName(String petNickName) {
		this.petNickName = petNickName;
	}

 	public String getPetGender() {
 		return petGender;
 	}
 	public void setPetGender(String petGender) {
 		this.petGender = petGender;
 	}
 	public String getPetStar() {
 		return petStar;
 	}
 	public void setPetStar(String petStar) {
 		this.petStar = petStar;
 	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public PicDTO[] getPictures() {
		return pictures;
	}
	public void setPictures(PicDTO[] pictures) {
		this.pictures = pictures;
	}
//	public List<CommentDTO> getLastComments() {
//		return comments;
//	}
//	public void setLastComments(List<CommentDTO> lastComments) {
//		this.comments = lastComments;
//	}
	
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public boolean getTop() {
		return top;
	}
	public void setTop(boolean top) {
		this.top = top;
	}
	public boolean getIsLiked() {
		return isLiked;
	}
	public void setIsLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}
	
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getTalkType() {
		return talkType;
	}
	public void setTalkType(int talkType) {
		this.talkType = talkType;
	}

	public List<CommentDTO> getComments() {
		return comments;
	}
	public void setComments(List<CommentDTO> comments) {
		this.comments = comments;
	}
	
}
