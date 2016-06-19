package com.petsay.vo.coupon;

import java.io.Serializable;

import com.petsay.vo.personalcustom.CategoryDTO;

public class CouponActivitySettingsDTO implements Serializable {

	private static final long serialVersionUID = 107394018104195073L;
	
	private String id;
	private String couponName;
	private String couponDesc;
	private int couponSendCount;
	private int couponLimitCount;
	private String couponFaceValue;
	private long couponBeginTime;
	private long couponEndTime;
	private boolean isTaken;
	
	private CategoryDTO category;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponDesc() {
		return couponDesc;
	}

	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}

	public int getCouponSendCount() {
		return couponSendCount;
	}

	public void setCouponSendCount(int couponSendCount) {
		this.couponSendCount = couponSendCount;
	}

	public int getCouponLimitCount() {
		return couponLimitCount;
	}

	public void setCouponLimitCount(int couponLimitCount) {
		this.couponLimitCount = couponLimitCount;
	}

	public String getCouponFaceValue() {
		return couponFaceValue;
	}

	public void setCouponFaceValue(String couponFaceValue) {
		this.couponFaceValue = couponFaceValue;
	}

	public long getCouponBeginTime() {
		return couponBeginTime;
	}

	public void setCouponBeginTime(long couponBeginTime) {
		this.couponBeginTime = couponBeginTime;
	}

	public long getCouponEndTime() {
		return couponEndTime;
	}

	public void setCouponEndTime(long couponEndTime) {
		this.couponEndTime = couponEndTime;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}
	
	public boolean getIsTaken() {
		return isTaken;
	}

	public void setIsTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}
}
