package com.petsay.vo.award;

import java.io.Serializable;

/**
 * 参与人信息
 * 
 * @author xiaoyi
 *
 */
public class JoinerDTO implements Serializable {

	private static final long serialVersionUID = 3131222335480824908L;
	
	/**  petId  */
	private String id;
	/**  宠物的头像url  */
	private String avatarUrl;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

}
