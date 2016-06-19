package com.petsay.vo.award;

import java.io.Serializable;

/**
 * 我的任务的一些基本信息
 * 
 * @author xiaoyi
 *
 */
public class PetActivityInfoDTO implements Serializable {

	
	private static final long serialVersionUID = -4208183951633860761L;
	
	/**  未完成的任务数  */
	private String unfinishedActCount;
	/**  已经完成的任务数  */
	private String finishedActCount;

	public String getUnfinishedActCount() {
		return unfinishedActCount;
	}

	public void setUnfinishedActCount(String unfinishedActCount) {
		this.unfinishedActCount = unfinishedActCount;
	}

	public String getFinishedActCount() {
		return finishedActCount;
	}

	public void setFinishedActCount(String finishedActCount) {
		this.finishedActCount = finishedActCount;
	}

}
