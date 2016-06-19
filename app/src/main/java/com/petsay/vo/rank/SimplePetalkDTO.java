package com.petsay.vo.rank;

import java.io.Serializable;

public class SimplePetalkDTO implements Serializable {

	private static final long serialVersionUID = -730678975503571121L;
	
	private String id;// Petalk主键
	private String photoUrl;// 照片资源标识


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
}
