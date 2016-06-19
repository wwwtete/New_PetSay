package com.petsay.vo.award;

import java.io.Serializable;

import com.petsay.R;

/**
 * 宠物参加的活动列表
 * 
 * @author xiaoyi
 *
 */
public class PetActivityDTO implements Serializable {

	private static final long serialVersionUID = 3451358509258364441L;

	/** 主键 */
	private String id;
	/** 1未进行2进行中3失败4成功 */
	private int state;
	/** 已经完成的步数 */
	private String currentSetp;
	/** 宠物的任务创建时间 */
	private long createTime;
	/** 服务器倒计时时间 */
	private long countdownTime;
	private ActivityDTO activityDTO;
	
	/* 客户端添加 */
    private int stateDrawableRes;
    private int listItemResByCatagory;
    private String taskProgres;//任务进度
	
	public String getTaskProgres() {
		return taskProgres;
	}

	public void setTaskProgres() {
		String result="";
		if (getActivityDTO().getCatagory() == 1) {
			
			switch (getState()) {
			case 1:
				result =getActivityDTO().getTitle()+"(您还未发布说说)";
				break;
			default:
				result = getActivityDTO().getTitle();
				break;
			}
			// holder.layoutAward.setBackgroundResource(R.color.award_lottery_item_bg);
		} else if (getActivityDTO().getCatagory() == 2) {
			result = getActivityDTO().getTitle();
		} else {
			switch (getState()) {
			case 1:
				result =getActivityDTO().getTitle()+"(您还未发布说说)";
				break;
			default:
				result = getActivityDTO().getTitle();
				break;
			}
		}
		this.taskProgres = result;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCurrentSetp() {
		return currentSetp;
	}

	public void setCurrentSetp(String currentSetp) {
		this.currentSetp = currentSetp;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getCountdownTime() {
		return countdownTime;
	}

	public void setCountdownTime(long countdownTime) {
		this.countdownTime = countdownTime;
	}

	public ActivityDTO getActivityDTO() {
		return activityDTO;
	}

	public void setActivityDTO(ActivityDTO activityDTO) {
		this.activityDTO = activityDTO;
	}

	
	public void setStateDrawableRes() {
		int result = 0;
		if (getActivityDTO().getCatagory() == 1) {
			switch (getState()) {
			case 1:
				result = R.drawable.award_lottery_started;
				break;
			case 2:
				result = R.drawable.award_lottery_alreadyjoin;
				break;
			case 3:
				result = R.drawable.award_lottery_unwinning;
				break;
			case 4:
				result = R.drawable.award_lottery_winning;
				break;
			default:
				result = R.drawable.award_lottery_unwinning;
				break;
			}
			// holder.layoutAward.setBackgroundResource(R.color.award_lottery_item_bg);
		} else if (getActivityDTO().getCatagory() == 2) {
			switch (getState()) {
			case 1:
				result = R.drawable.award_task_started;
				break;
			case 2:
				result = R.drawable.award_task_started;
				break;
			case 3:
				result = R.drawable.award_task_fail;
				break;
			case 4:
				result = R.drawable.award_task_complete;
				break;
			default:
				result = R.drawable.award_task_fail;
				break;
			}
		} else {
			switch (getState()) {
			case 1:
				result = R.drawable.award_rank_started;
				break;
			case 2:
				result = R.drawable.award_rank_alreadyjoin;
				break;
			case 3:
				result = R.drawable.award_rank_unwinning;
				break;
			case 4:
				result = R.drawable.award_rank_winning;
				break;
			default:
				result = R.drawable.award_rank_unwinning;
				break;
			}
		}
		this.stateDrawableRes = result;
	}
	
	public int getStateDrawableRes() {
		
		return stateDrawableRes;
	}

	public int getListItemResByCatagory() {
		

		return listItemResByCatagory;
	}

	public void setListItemResByCatagory() {
		int result = 0;
		if (getActivityDTO().getCatagory() == 1) {
			// holder.layoutAward.setBackgroundResource(R.color.award_lottery_item_bg);
			result = R.color.award_lottery_item_bg;
		} else if (getActivityDTO().getCatagory() == 2) {
			// holder.layoutAward.setBackgroundResource(R.drawable.award_task_item_bg);
			result = R.color.award_task_item_bg;
		} else {
			// holder.layoutAward.setBackgroundResource(R.drawable.award_ranking_item_bg);
			result = R.color.award_rank_item_bg;
		}
		this.listItemResByCatagory = result;
	}


}
