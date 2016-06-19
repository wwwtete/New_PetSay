package com.petsay.activity.award.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.main.MainActivity;
import com.petsay.activity.petalk.TagSayListActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.component.text.Clickable;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.award.PetActivityDTO;

public class MyTaskListAdapter extends BaseAdapter {

	private Context mContext;
    private List<PetActivityDTO> petActivityDTOs;
    public int currentSelectIndex=0;
    private boolean isAll=false;
	public MyTaskListAdapter(Context context,boolean isAll) {
		mContext = context;
		this.isAll=isAll;
		this.petActivityDTOs=new ArrayList<PetActivityDTO>();
	}
	
	public void refreshData(List<PetActivityDTO> dtos){
		if (dtos != null && dtos.size() > 0) {
			petActivityDTOs.clear();
			petActivityDTOs.addAll(dtos);
			notifyDataSetChanged();
			handler.removeMessages(1);
			handler.sendEmptyMessage(1);
		}else if(!isAll){
			PublicMethod.showToast(mContext, "还没有正在进行的任务");
		}
	}
	
	public void addMore(List<PetActivityDTO> dtos){
		if(dtos != null && dtos.size() > 0){
			petActivityDTOs.addAll(dtos);
			notifyDataSetChanged();
			handler.removeMessages(1);
			handler.sendEmptyMessage(1);
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void clear(){
		petActivityDTOs.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==petActivityDTOs) {
			return 0;
		}
		return  petActivityDTOs.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==petActivityDTOs||petActivityDTOs.isEmpty()) {
			return null;
		}
		return  petActivityDTOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.award_task_list_item, null);
			holder = new Holder();
			holder.tvAwardName=(TextView) convertView.findViewById(R.id.tv_award_name);
			holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
			holder.tvTaskName=(TextView) convertView.findViewById(R.id.tv_task_name);
			holder.layoutItem=(RelativeLayout) convertView.findViewById(R.id.layout_item);
			holder.imgState=(ImageView) convertView.findViewById(R.id.img_state);
			holder.tvGo=(TextView) convertView.findViewById(R.id.tv_go);
			holder.tvTime=(TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final PetActivityDTO petActivityDTO=petActivityDTOs.get(position);
		
		holder.tvTitle.setText(petActivityDTO.getActivityDTO().getCatagoryString());
		
		holder.tvAwardName.setText(petActivityDTO.getActivityDTO().getAwardName());
		holder.imgState.setImageResource(petActivityDTO.getStateDrawableRes());
		holder.layoutItem.setBackgroundResource(petActivityDTO.getListItemResByCatagory());
		holder.tvGo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				switch (petActivityDTO.getActivityDTO().getCatagory()) {
				case 1:
					intent.setClass(mContext, TagSayListActivity.class);
					intent.putExtra("id", petActivityDTO.getActivityDTO().getTagId());
					mContext.startActivity(intent);
					break;
				case 3:
					intent.setClass(mContext, TagSayListActivity.class);
					intent.putExtra("id", petActivityDTO.getActivityDTO().getTagId());
					mContext.startActivity(intent);
					break;
				case 2:
					intent.setClass(mContext, MainActivity.class);
					mContext.startActivity(intent);
					break;
				default:
					intent.setClass(mContext, MainActivity.class);
					mContext.startActivity(intent);
					break;
				}
				
			}
		});
		if (petActivityDTO.getActivityDTO().getCatagory()==3) {
			String content=petActivityDTO.getTaskProgres();
			int start=content.indexOf("(");
			int end=content.indexOf(")");
			if (petActivityDTO.getState()==1||start==-1||end==-1) {
				holder.tvTaskName.setText(content);
			}else {
				SpannableString spannableString=new SpannableString(content);
				spannableString.setSpan(new Clickable(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent=new Intent(mContext,WebViewActivity.class);
						intent.putExtra("url", "http://mp.weixin.qq.com/s?__biz=MjM5MDM1ODExMQ==&mid=205284163&idx=1&sn=a938e6c433f7392f51652d56e378c68f#rd");
						mContext.startActivity(intent);
					}
				},false),start+1,end,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
				spannableString.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.score_color)),start+1,end,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 

				holder.tvTaskName.setMovementMethod(LinkMovementMethod.getInstance());
				holder.tvTaskName.setText(spannableString);
			}
			
		}else {
			holder.tvTaskName.setText(petActivityDTO.getTaskProgres());
		}
		if (isAll) {
			if (petActivityDTO.getCountdownTime()<=0&&(petActivityDTO.getState()!=3&&petActivityDTO.getState()!=4)) {
				holder.tvTime.setVisibility(View.VISIBLE);
				holder.tvTime.setText("结果统计中");
				holder.tvGo.setVisibility(View.INVISIBLE);
				if (petActivityDTO.getState()==1) {
					holder.imgState.setVisibility(View.INVISIBLE);
				}
			}else if (petActivityDTO.getCountdownTime()>0) {
				holder.tvTime.setVisibility(View.VISIBLE);
				holder.tvTime.setText("距离截止："+PublicMethod.formatCountDownTime(petActivityDTO.getCountdownTime()));
				holder.tvGo.setVisibility(View.VISIBLE);
				if (petActivityDTO.getActivityDTO().getCatagory() == 2) {

					if (petActivityDTO.getState() == 2) {
						holder.tvGo.setVisibility(View.VISIBLE);
						holder.tvGo.setText("完成任务>>");
					} else {
						holder.tvGo.setVisibility(View.INVISIBLE);
					}
				} else {
					if (petActivityDTO.getState() == 1) {
						holder.tvGo.setVisibility(View.VISIBLE);
						holder.tvGo.setText("去发布>>");
					} else {
						holder.tvGo.setVisibility(View.INVISIBLE);
					}
				}
			}else {
				holder.tvTime.setVisibility(View.INVISIBLE);
				holder.tvGo.setVisibility(View.INVISIBLE);
			}
		}else {
			holder.tvTime.setVisibility(View.VISIBLE);
			holder.tvTime.setText("距离截止："+PublicMethod.formatCountDownTime(petActivityDTO.getCountdownTime()));
			if (petActivityDTO.getActivityDTO().getCatagory()==2) {
				
				if (petActivityDTO.getState()==2) {
					holder.tvGo.setVisibility(View.VISIBLE);
					holder.tvGo.setText("完成任务>>");
				}else {
					holder.tvGo.setVisibility(View.INVISIBLE);
				}
			}else {
				if (petActivityDTO.getState()==1) {
					holder.tvGo.setVisibility(View.VISIBLE);
					holder.tvGo.setText("去发布>>");
				}else {
					holder.tvGo.setVisibility(View.INVISIBLE);
				}
			}
		}
		
		int stateImgRes=0;
		if (petActivityDTO.getCountdownTime()<=0) {
			holder.tvGo.setVisibility(View.INVISIBLE);
			if (petActivityDTO.getActivityDTO().getCatagory() == 2) {
				//TODO 任务
				if (petActivityDTO.getState()==1||petActivityDTO.getState()==2||petActivityDTO.getState()==3) {
					stateImgRes=R.drawable.award_task_fail;
				}else {
					stateImgRes=R.drawable.award_task_complete;
				}
//				holder.imgState.setImageResource(stateImgRes);
				holder.imgState.setVisibility(View.VISIBLE);
				holder.tvTime.setVisibility(View.INVISIBLE);
			}else if (petActivityDTO.getActivityDTO().getCatagory() == 1){
				//TODO 抽奖 排行 
				switch (petActivityDTO.getState()) {
				case 1:
					stateImgRes=R.drawable.award_lottery_started;
					holder.imgState.setVisibility(View.INVISIBLE);
					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				case 2:
					stateImgRes=R.drawable.award_lottery_alreadyjoin;
					holder.imgState.setVisibility(View.VISIBLE);
					holder.tvTime.setVisibility(View.VISIBLE);
					holder.tvTime.setText("结果统计中");
					break;
				case 3:
					stateImgRes=R.drawable.award_lottery_unwinning;
					holder.imgState.setVisibility(View.VISIBLE);
					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				case 4:
					stateImgRes=R.drawable.award_lottery_winning;
					holder.imgState.setVisibility(View.VISIBLE);
					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				default:
					stateImgRes=R.drawable.award_lottery_started;
					holder.imgState.setVisibility(View.INVISIBLE);
					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				}
//				holder.imgState.setImageResource(stateImgRes);
			}else {
				switch (petActivityDTO.getState()) {
				case 1:
					stateImgRes=R.drawable.award_rank_started;
					holder.imgState.setVisibility(View.INVISIBLE);
					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				case 2:
					stateImgRes=R.drawable.award_rank_alreadyjoin;
					holder.imgState.setVisibility(View.VISIBLE);
					holder.tvTime.setVisibility(View.VISIBLE);
					holder.tvTime.setText("结果统计中");
					break;
				case 3:
					stateImgRes=R.drawable.award_rank_unwinning;
					holder.imgState.setVisibility(View.VISIBLE);
					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				case 4:
					stateImgRes=R.drawable.award_rank_winning;
					holder.imgState.setVisibility(View.VISIBLE);
					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				default:
					stateImgRes=R.drawable.award_rank_started;
					holder.imgState.setVisibility(View.INVISIBLE);
					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				}
			}
			holder.imgState.setImageResource(stateImgRes);
		}else {//TODO  未结束
			holder.tvTime.setVisibility(View.VISIBLE);
			holder.tvGo.setVisibility(View.VISIBLE);
			holder.imgState.setVisibility(View.VISIBLE);
			holder.tvTime.setText("距离截止："+PublicMethod.formatCountDownTime(petActivityDTO.getCountdownTime()));
			if (petActivityDTO.getActivityDTO().getCatagory() == 2) {
				//TODO 任务
				switch (petActivityDTO.getState()) {
				case 3:
					stateImgRes=R.drawable.award_task_fail;
					holder.tvGo.setVisibility(View.INVISIBLE);
					
//					holder.tvGo.setText("完成任务>>");
					break;
				case 4:
					stateImgRes=R.drawable.award_task_complete;
					holder.tvGo.setVisibility(View.INVISIBLE);
					break;
				default:
					stateImgRes=R.drawable.award_task_started;
					holder.tvGo.setVisibility(View.VISIBLE);
					holder.tvGo.setText("完成任务>>");
					break;
				}
			}else if (petActivityDTO.getActivityDTO().getCatagory() == 1){
				//TODO 抽奖 排行 
				switch (petActivityDTO.getState()) {
				case 2:
					stateImgRes=R.drawable.award_lottery_alreadyjoin;
					holder.tvGo.setVisibility(View.INVISIBLE);
					break;
				case 3:
					stateImgRes=R.drawable.award_lottery_unwinning;
					holder.tvGo.setVisibility(View.INVISIBLE);
					break;
				case 4:
					stateImgRes=R.drawable.award_lottery_winning;
					holder.tvGo.setVisibility(View.INVISIBLE);
//					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				default:
					stateImgRes=R.drawable.award_lottery_started;
					holder.tvGo.setVisibility(View.VISIBLE);
					holder.tvGo.setText("去发布>>");
					break;
				}
//				holder.imgState.setImageResource(stateImgRes);
			}else {
				switch (petActivityDTO.getState()) {
				case 2:
					stateImgRes=R.drawable.award_rank_alreadyjoin;
					holder.tvGo.setVisibility(View.INVISIBLE);
					break;
				case 3:
					stateImgRes=R.drawable.award_rank_unwinning;
					holder.tvGo.setVisibility(View.INVISIBLE);
					break;
				case 4:
					stateImgRes=R.drawable.award_rank_winning;
					holder.tvGo.setVisibility(View.INVISIBLE);
//					holder.tvTime.setVisibility(View.INVISIBLE);
					break;
				default:
					stateImgRes=R.drawable.award_rank_started;
					holder.tvGo.setVisibility(View.VISIBLE);
					holder.tvGo.setText("去发布>>");
					break;
				}
			}
			holder.imgState.setImageResource(stateImgRes);
			
		}
			
		
		
		
		return convertView;
	}

	private class Holder {
		private TextView tvAwardName,tvTaskName,tvTitle,tvGo;
		private RelativeLayout layoutItem;
		private ImageView imgState;
		private TextView tvTime;
		
	}
	
	public Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				boolean isNeedCountTime = false;
				//①：其实在这块需要精确计算当前时间
				for(int index =0;index<petActivityDTOs.size();index++){
					PetActivityDTO petActivityDTO = petActivityDTOs.get(index);
					long time = petActivityDTO.getCountdownTime();
					if(time>1000){//判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
						isNeedCountTime = true;
						petActivityDTO.setCountdownTime(time-1000);
					}else{
						if (!isAll) {
							petActivityDTOs.remove(index);
							notifyDataSetChanged();
						}
//						petActivityDTO.setCountdownTime(0);
						
					}
				}
				//②：for循环执行的时间
				notifyDataSetChanged();
				if(isNeedCountTime){
					//TODO 然后用1000-（②-①），就赢延迟的时间
					handler.sendEmptyMessageDelayed(1, 1000);
				}
				break;
			}
		}
	};
}