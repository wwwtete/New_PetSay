package com.petsay.vo.sign;

import java.io.Serializable;

public class ActivityPartakeDetailVo implements Serializable {

	private static final long serialVersionUID = -719331153982542151L;

	private String id;
	private String partakeCode;
	private String taskCode;
	private String award;
	private int state;
	private long createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPartakeCode() {
		return partakeCode;
	}

	public void setPartakeCode(String partakeCode) {
		this.partakeCode = partakeCode;
	}

	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
