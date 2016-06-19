package com.petsay.activity.homeview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.petsay.R;
import com.petsay.component.view.CircleImageView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.petalk.PetVo;

import java.util.List;

@Deprecated
public class RecommendHListViewAdapter extends BaseAdapter{
//	private int[] mIconIDs;
//	private String[] mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	private Bitmap iconBitmap;
	private int selectIndex = 0;
    private List<PetVo> mPetInfos;
	public RecommendHListViewAdapter(Context context, List<PetVo> petVos){
		this.mContext = context;
		mPetInfos=petVos;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
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
		if (null==mPetInfos||mPetInfos.isEmpty()) {
			return 0;
		}
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.recommend_hlist_item, null);
			holder.mImage=(CircleImageView)convertView.findViewById(R.id.img_header);
			holder.imgSex=(ImageView) convertView.findViewById(R.id.img_sex);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		PetVo petInfo=mPetInfos.get(position);
//		if(position == selectIndex){
//			convertView.setSelected(true);
//			holder.mTitle.setTextColor(Color.parseColor("#898989"));
//		}else{
//			holder.mImage.setTextColor(R.color.white);
//			convertView.setSelected(false);
//		}
		ImageLoaderHelp.displayHeaderImage(petInfo.getHeadPortrait(), holder.mImage);
		if (petInfo.getGender()==0) {
			holder.imgSex.setVisibility(View.VISIBLE);
			holder.imgSex.setImageResource(R.drawable.square_female);
		}else if (petInfo.getGender()==1) {
			holder.imgSex.setVisibility(View.VISIBLE);
			holder.imgSex.setImageResource(R.drawable.square_male);
		}else {
			holder.imgSex.setVisibility(View.GONE);
		}
		return convertView;
	}

	private static class ViewHolder {
		private CircleImageView mImage;
		private ImageView imgSex;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}
}