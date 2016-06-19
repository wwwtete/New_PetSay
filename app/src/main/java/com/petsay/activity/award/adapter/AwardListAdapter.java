package com.petsay.activity.award.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.award.ActivityDTO;

public class AwardListAdapter extends BaseAdapter {

	private Context mContext;
    private List<ActivityDTO> activityDTOs;
    public int currentSelectIndex=0;
	public AwardListAdapter(Context context) {
		mContext = context;
		this.activityDTOs=new ArrayList<ActivityDTO>();
	}
	
	public void refreshData(List<ActivityDTO> dtos){
		if (dtos != null && dtos.size() > 0) {
			activityDTOs.clear();
			activityDTOs.addAll(dtos);
			notifyDataSetChanged();
		}
	}
	
	public void addMore(List<ActivityDTO> dtos){
		if(dtos != null && dtos.size() > 0){
			activityDTOs.addAll(dtos);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void clear(){
		activityDTOs.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==activityDTOs) {
			return 0;
		}
		return  activityDTOs.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==activityDTOs||activityDTOs.isEmpty()) {
			return null;
		}
		return  activityDTOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.award_list_item, null);
			holder = new Holder();
			holder.imgAward=(ImageView) convertView.findViewById(R.id.img_award);
			holder.tvAwardName=(TextView) convertView.findViewById(R.id.tv_award_name);
			holder.tvAwardBroseCount=(TextView) convertView.findViewById(R.id.tv_award_browse);
			holder.tvState=(TextView) convertView.findViewById(R.id.tv_award_state);
			holder.layoutItem=(LinearLayout) convertView.findViewById(R.id.layout_item);
			holder.layoutAward=(LinearLayout) convertView.findViewById(R.id.layout_award);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		ActivityDTO activityDTO=activityDTOs.get(position);
		holder.tvAwardName.setText(activityDTO.getAwardName());
		if (activityDTO.getCatagory()==1) {
			holder.layoutAward.setBackgroundResource(R.drawable.award_lottery_item_bg);
			holder.layoutItem.setBackgroundResource(R.drawable.award_lottery_item_layout_bg);
		}else if(activityDTO.getCatagory()==2) {
			holder.layoutAward.setBackgroundResource(R.drawable.award_task_item_bg);
			holder.layoutItem.setBackgroundResource(R.drawable.award_task_item_layout_bg);
		}else {
			holder.layoutAward.setBackgroundResource(R.drawable.award_ranking_item_bg);
			holder.layoutItem.setBackgroundResource(R.drawable.award_ranking_item_layout_bg);
		}
		ImageLoaderHelp.displayHeaderImage(activityDTO.getAwardCover(), holder.imgAward);
		holder.tvState.setText(activityDTO.getStateString());
//		/**  1未开始2可参与3已参与4进行中5已结束  */
//		switch (activityDTO.getState()) {
//		case 1:
//			holder.tvState.setText("未开始");
//			break;
//		case 2:
//			holder.tvState.setText("可参与");
//			break;
//		case 3:
//			holder.tvState.setText("已参与");
//			break;
//		case 4:
//			holder.tvState.setText("进行中");
//			break;
//		default:
//			holder.tvState.setText("已结束");
//			break;
//		}
		
		
		
		holder.tvAwardBroseCount.setText(PublicMethod.calPlayTimes(activityDTO.getViewCount())+"浏览");
		
		
		return convertView;
	}

	private class Holder {
		private ImageView imgAward;
		private TextView tvAwardName,tvAwardBroseCount,tvState;
		private LinearLayout layoutItem,layoutAward;
	}
}