package com.petsay.vo.sign;

import java.io.Serializable;
import java.util.List;

public class SignInCalendarVo implements Serializable {

	private static final long serialVersionUID = 1852273016206429668L;

	private String sign;// 是否已签到
	private String award;// 签到奖励
	private String count;// 连续签到天数
	private String memo;// 备注
	private String partakeTime; // 签到时间

	private List<ActivityPartakeDetailVo> details;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAward() {
		return award;
	}

	public void setAward(String award) {
		this.award = award;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPartakeTime() {
		return partakeTime;
	}

	public void setPartakeTime(String partakeTime) {
		this.partakeTime = partakeTime;
	}

	public List<ActivityPartakeDetailVo> getDetails() {
		return details;
	}

	public void setDetails(List<ActivityPartakeDetailVo> details) {
		this.details = details;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
