package com.petsay.activity.global.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.view.CircleImageView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetVo;

public class SearchUserAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<PetVo> mPetInfos;
	public SearchUserAdapter(Context context){
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
	
	public void clear(){
		mPetInfos.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==mPetInfos) {
			return 0;
		}
		return mPetInfos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==mPetInfos||mPetInfos.isEmpty()) {
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
		if (null == convertView) {
			holder = new Holder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.search_user_list_item, null);
			holder.imgHeader=(CircleImageView) convertView.findViewById(R.id.img_user_header);
			holder.tvAttention=(TextView) convertView.findViewById(R.id.tv_user_attention);
			holder.tvFansCount=(TextView) convertView.findViewById(R.id.tv_fans_count);
			holder.tvSayCount=(TextView) convertView.findViewById(R.id.tv_say_count);
			holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		PetVo petInfo=mPetInfos.get(position);
		holder.tvName.setText(petInfo.getNickName());
		
		holder.tvFansCount.setText(petInfo.getCounter().getFans()+"");
		holder.tvSayCount.setText(petInfo.getCounter().getIssue()+"");
		
//		holder.tvSayCount
		
//		ImageLoader.getInstance().displayImage(petInfo.getHeadPortrait(), holder.imgHeader, Constants.headerImgOptions);
//		PicassoUtile.loadHeadImg(mContext,petInfo.getHeadPortrait(), holder.imgHeader);
		ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(), holder.imgHeader);
		return convertView;
	}
	
	private class Holder{
		private TextView tvName,tvSayCount,tvFansCount,tvAttention;
		private CircleImageView imgHeader;
//		private
	}

}
