package com.petsay.vo.petalk;

import java.io.Serializable;

public class PetFansDTO implements Serializable {


	private static final long serialVersionUID = -4172156364198669174L;
	private String id;// 主键
	private String petId;// 宠物ID
	private String petFansId;// 粉丝ID
	private boolean bothway;// 相互关注
	private boolean deleted;// 是否注销
	private long createTime;// 创建时间

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

	public String getPetFansId() {
		return petFansId;
	}

	public void setPetFansId(String petFansId) {
		this.petFansId = petFansId;
	}

	public boolean getBothway() {
		return bothway;
	}

	public void setBothway(boolean bothway) {
		this.bothway = bothway;
	}

	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


}
