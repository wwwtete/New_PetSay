package com.petsay.vo.rank;

import java.io.Serializable;
import java.util.Date;

/**
 * 宠物成长经历
 */
public class PetRankEveryMomentDTO implements Serializable {

	private static final long serialVersionUID = -5998656245016542256L;
	
	private String id;// ID
	private String petId;// ID
	private String friendCount;// ID
	private String followerCount;// ID
	private String petalkCount;// ID
	private String likedCount;// ID
	private String rkNum;// ID
	private Date joinTime;
	
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
	public String getFriendCount() {
		return friendCount;
	}
	public void setFriendCount(String friendCount) {
		this.friendCount = friendCount;
	}
	public String getFollowerCount() {
		return followerCount;
	}
	public void setFollowerCount(String followerCount) {
		this.followerCount = followerCount;
	}
	public String getPetalkCount() {
		return petalkCount;
	}
	public void setPetalkCount(String petalkCount) {
		this.petalkCount = petalkCount;
	}
	public String getLikedCount() {
		return likedCount;
	}
	public void setLikedCount(String likedCount) {
		this.likedCount = likedCount;
	}
	public String getRkNum() {
		return rkNum;
	}
	public void setRkNum(String rkNum) {
		this.rkNum = rkNum;
	}
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	} 
	
}
