package com.petsay.activity.user.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.view.CircleImageView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetVo;


public class AttentionAdapter extends BaseAdapter{

	private Context mContext;
	private List<PetVo> mPetInfos;
	public AttentionAdapter(Context context){
		mContext=context;
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
			convertView=LayoutInflater.from(mContext).inflate(R.layout.attention_activity_list_item, null);
			holder.usericon=(CircleImageView) convertView.findViewById(R.id.img_header);
			holder.tvUsername=(TextView) convertView.findViewById(R.id.tv_username);
			holder.tvPetalk=(TextView) convertView.findViewById(R.id.tv_petalk_count);
			holder.tvFans=(TextView) convertView.findViewById(R.id.tv_fans_count);
			holder.imgStar=(ImageView) convertView.findViewById(R.id.img_star);
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		PetVo petInfo=mPetInfos.get(position);
//		ImageLoader.getInstance().displayImage(petInfo.getHeadPortrait(),holder.usericon,Constants.headerImgOptions);
//		PicassoUtile.loadHeadImg(mContext, petInfo.getHeadPortrait(), holder.usericon);
		ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(), holder.usericon);
//		ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(), holder.usericon);
		if (!TextUtils.isEmpty(petInfo.getStar())&&petInfo.getStar().equals("1")) {
			holder.imgStar.setVisibility(View.VISIBLE);
		} else {
			holder.imgStar.setVisibility(View.GONE);
		}
		holder.tvUsername.setText(petInfo.getNickName());
		holder.tvFans.setText("粉丝："+petInfo.getCounter().getFans());
		holder.tvPetalk.setText("宠物说:"+petInfo.getCounter().getIssue());
		return convertView;
	}
	
	private class Holder{
		private CircleImageView usericon;
		private TextView tvUsername,tvPetalk,tvFans;
		private ImageView imgStar;
//		private 
	}

}
