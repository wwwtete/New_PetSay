package com.petsay.activity.user.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.user.FansActivity;
import com.petsay.component.view.CircleImageView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetVo;


public class FansAdapter extends BaseAdapter{

	private Context mContext;
	private List<PetVo> mPetInfos;
	/**跳转类型：0：代表别人的粉丝列表，1：代表自己的粉丝列表*/
	private int mJumpType;
	public FansAdapter(Context context,int jumpType){
		mContext=context;
		mJumpType = jumpType;
		mPetInfos=new ArrayList<PetVo>();
	}

	public void refreshData(List<PetVo> data){
		if(data != null ){
			mPetInfos.clear();
			mPetInfos.addAll(data);
			notifyDataSetChanged();
		}
	}

	public void addMore(List<PetVo> data){
		if(data != null && data.size() > 0){
			mPetInfos.addAll(data);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}

	@Override
	public int getCount() {
		if (mPetInfos==null) {
			return 0;
		}
		return mPetInfos.size();
	}

	@Override
	public Object getItem(int position) {
		if (mPetInfos==null||mPetInfos.isEmpty()) {
			return null;
		}
		return mPetInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (null==convertView) {
			holder=new Holder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.fans_activity_list_item, null);
			holder.tvAttention=(TextView) convertView.findViewById(R.id.btn_attention);
			holder.img = (ImageView) convertView.findViewById(R.id.iv_img);
			holder.rLayoutFans=(RelativeLayout) convertView.findViewById(R.id.rlayout_fans);
			holder.imgStar=(ImageView) convertView.findViewById(R.id.img_star);
			//mJumpType等于1代表是自己的粉丝列表，可以显示
//			if(mJumpType == 0){
				
//			}else {
//				holder.img.setVisibility(View.GONE);
//				holder.tvAttention.setVisibility(View.VISIBLE);
//			}
			holder.usericon=(CircleImageView) convertView.findViewById(R.id.img_header);
			holder.tvUsername=(TextView) convertView.findViewById(R.id.tv_username);
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		holder.rLayoutFans.setBackgroundColor(Color.WHITE);
		holder.tvAttention.setVisibility(View.GONE);
		holder.img.setVisibility(View.VISIBLE);
		PetVo petInfo=mPetInfos.get(position);
		ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(), holder.usericon);
		if (!TextUtils.isEmpty(petInfo.getStar())&&petInfo.getStar().equals("1")) {
			holder.imgStar.setVisibility(View.VISIBLE);
		} else {
			holder.imgStar.setVisibility(View.GONE);
		}
		//mJumpType等于1代表是自己的粉丝列表，可以显示
		if(mJumpType == 1){
			initAttention(holder.tvAttention, petInfo);
		}
		
		holder.tvUsername.setText(petInfo.getNickName());
		holder.tvAttention.setTag(petInfo);
		return convertView;
	}
	
	private void initAttention(TextView tvAttention,PetVo petInfo){
		if (petInfo.getRs()==0 || petInfo.getRs()==1) {
			tvAttention.setBackgroundResource(R.drawable.attention);
			tvAttention.setTextColor(Color.parseColor("#3DC5FF"));
			tvAttention.setText(R.string.add_attention);
		}else if (petInfo.getRs()==2) {
			tvAttention.setBackgroundResource(R.drawable.attentioned);
			tvAttention.setTextColor(Color.parseColor("#2359B8"));
			tvAttention.setText(R.string.already_add_attention);
		}
		tvAttention.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mContext instanceof FansActivity){
					if(v.getTag() != null){
						((FansActivity)mContext).onAttention((PetVo) v.getTag());
					}
				}
			}
		});
	}

	private class Holder{
		private CircleImageView usericon;
		private TextView tvUsername;
		private TextView tvAttention; 
		private ImageView img,imgStar;
		private RelativeLayout rLayoutFans;
		//		private 
	}

}
