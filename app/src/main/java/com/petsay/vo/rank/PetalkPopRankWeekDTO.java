package com.petsay.vo.rank;

import com.petsay.vo.petalk.PetalkVo;

/**
 * @author wangw
 * 热门说说排行榜
 */
public class PetalkPopRankWeekDTO {

	/**
	 * 说说
	 */
	private PetalkVo petalkDTO;
	/**
	 * 热度
	 */
	private String popNum;
	/**
	 * 排名
	 */
	private int rankNum;
	/**
	 * 更新时间
	 */
	private String updateTime;
	
	public PetalkVo getPetalkDTO() {
		return petalkDTO;
	}
	public void setPetalkDTO(PetalkVo petalkDTO) {
		this.petalkDTO = petalkDTO;
	}
	public String getPopNum() {
		return popNum;
	}
	public void setPopNum(String popNum) {
		this.popNum = popNum;
	}
	public int getRankNum() {
		return rankNum;
	}
	public void setRankNum(int rankNum) {
		this.rankNum = rankNum;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
