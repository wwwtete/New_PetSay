package com.petsay.vo.award;

import java.io.Serializable;
import java.util.List;

/**
 * 有奖活动
 * 
 * @author xiaoyi
 *
 */
public class ActivityDTO implements Serializable {

	private static final long serialVersionUID = 5503857358282396590L;
	
	private String id;
	/**  1 抽奖2任务3排行  */
	private int catagory;
	/**  奖品名称  */
	private String awardName;
	/**  奖品封面  */
	private String awardCover;
	/**  浏览数  */
	private long viewCount;
	/**  参与人数  */
	private long joinerCount;
	/**  1未开始2可参与3已参与4进行中5已结束  */
	private int state;
	/**  活动介绍  */
	private String description;
	/**  活动简介-标题  */
	private String title;
	/**  活动开始时间  */
	private long beginTime;
	/**  活动结束时间  */
	private long endTime;
	/**  如果catagory是1抽奖或者3排行，会指定标签发布说说，这个就是标签id。 可能用户点击参与跳转到这个标签说说下  */
	private String tagId;
	/**  标签名称  */
	private String tagName;
	/**  如果catagory是2任务，完成任务所需要的步数(例如目标需要7个赞)。 其他抽奖或者排行完成步数都是1(无意义)   */
	private String targetStep;
	
	/**  轮播图片  */
	private List<AwardPicDTO> awardPics;
	/**  最后几个参与人的信息  */
	private List<JoinerDTO> joiners;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public String getAwardCover() {
		return awardCover;
	}

	public void setAwardCover(String awardCover) {
		this.awardCover = awardCover;
	}

	public long getViewCount() {
		return viewCount;
	}

	public void setViewCount(long viewCount) {
		this.viewCount = viewCount;
	}

	public long getJoinerCount() {
		return joinerCount;
	}

	public void setJoinerCount(long joinerCount) {
		this.joinerCount = joinerCount;
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
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getCatagory() {
		return catagory;
	}

	public void setCatagory(int catagory) {
		this.catagory = catagory;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	
	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTargetStep() {
		return targetStep;
	}

	public void setTargetStep(String targetStep) {
		this.targetStep = targetStep;
	}

	public List<AwardPicDTO> getAwardPics() {
		return awardPics;
	}

	public void setAwardPics(List<AwardPicDTO> awardPics) {
		this.awardPics = awardPics;
	}

	public List<JoinerDTO> getJoiners() {
		return joiners;
	}

	public void setJoiners(List<JoinerDTO> joiners) {
		this.joiners = joiners;
	}
	
	
	/**
	 * 获取状态的文本显示内容
	 * @return
	 */
	public String getStateString(){
		String result="";
		/**  1未开始2可参与3已参与4进行中5已结束  */
		switch (getState()) {
		case 1:
			result="未开始";
			break;
		case 2:
			result="可参与";
			break;
		case 3:
			result="已参与";
			break;
		case 4:
			result="进行中";
			break;
		default:
			result="已结束";
			break;
		}
		return result;
		
	}
	/**
	 * 获取活动类型文本
	 * @return
	 */
	public String getCatagoryString(){
		String result="";
		/**  1未开始2可参与3已参与4进行中5已结束  */
		switch (getCatagory()) {
		case 1:
			result="抽奖";
			break;
		case 2:
			result="任务";
			break;
		case 3:
			result="排行";
			break;
		default:
			result="排行";
			break;
		}
		return result;
	}

}
