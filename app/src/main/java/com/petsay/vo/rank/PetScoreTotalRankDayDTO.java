package com.petsay.vo.rank;

import java.util.List;


/**
 * @author wangw
 * 用户积分排行
 */
public class PetScoreTotalRankDayDTO {
	
	/** 宠物ID */
	private String petId;
	/** 宠物头像 */
	private String petHeadPortrait;
	/** 宠物昵称 */
	private String petNickName;// 
	/** 宠物性别 */
	private int petGender;//

    /** 宠物达宠 */
	private String petStar;// 
	/** 积分 */
	private String score;// 
	/** 等级 */
	private String level;// 
	/** 最后说说 */
	private List<SimplePetalkDTO> simplePetalkDTOs;

	/**
	 * 排名
	 */
	private int rankNum;
	/**
	 * 更新时间
	 */
	private String updateTime;


    public String getPetStar() {
        return petStar;
    }

    public void setPetStar(String petStar) {
        this.petStar = petStar;
    }
	
	public String getPetId() {
		return petId;
	}
	public void setPetId(String petId) {
		this.petId = petId;
	}
	public String getPetHeadPortrait() {
		return petHeadPortrait;
	}
	public void setPetHeadPortrait(String petHeadPortrait) {
		this.petHeadPortrait = petHeadPortrait;
	}
	public String getPetNickName() {
		return petNickName;
	}
	public void setPetNickName(String petNickName) {
		this.petNickName = petNickName;
	}
	public int getPetGender() {
		return petGender;
	}
	public void setPetGender(int petGender) {
		this.petGender = petGender;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public List<SimplePetalkDTO> getSimplePetalkDTOs() {
		return simplePetalkDTOs;
	}
	public void setSimplePetalkDTOs(List<SimplePetalkDTO> simplePetalkDTOs) {
		this.simplePetalkDTOs = simplePetalkDTOs;
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
