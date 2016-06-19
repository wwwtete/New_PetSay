package com.petsay.vo.sign;

import java.io.Serializable;

public class ActivityPartakeVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7857291773984982416L;
	private String code;// 活动编码
	private String name;// 活动名称
	private String icon;// 图标
	private int state;// 参与状态
	private String description;// 活动描述

	// private String progress; // 进度
	// private String expireTime; // 过期时间
	// private String partakeTime; // 完成时间

	/**当前宠物petID，此属性仅适合签到使用*/
	private String mCurrentPetID;

	public boolean ismSigned() {
		return mSigned;
	}

	public void setmSigned(boolean mSigned) {
		this.mSigned = mSigned;
	}

	public String getmCurrentPetID() {
		return mCurrentPetID;
	}

	public void setmCurrentPetID(String mCurrentPetID) {
		this.mCurrentPetID = mCurrentPetID;
	}

	/**是否已签到*/
	private boolean mSigned;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// public String getProgress() {
	// return progress;
	// }
	//
	// public void setProgress(String progress) {
	// this.progress = progress;
	// }
	//
	// public String getExpireTime() {
	// return expireTime;
	// }
	//
	// public void setExpireTime(String expireTime) {
	// this.expireTime = expireTime;
	// }
	//
	// public String getPartakeTime() {
	// return partakeTime;
	// }
	//
	// public void setPartakeTime(String partakeTime) {
	// this.partakeTime = partakeTime;
	// }

}
