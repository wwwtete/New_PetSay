package com.petsay.activity.global.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.component.view.TagView;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.SearchTagVo;

import java.util.ArrayList;
import java.util.List;

public class SearchTagAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<SearchTagVo> mSearchTagVos;
	
	public SearchTagAdapter(Context context){
		mContext=context;
		mSearchTagVos=new ArrayList<SearchTagVo>();
	}
	
	public void refreshData(List<SearchTagVo> data){
		if(data != null ){
			mSearchTagVos.clear();
			mSearchTagVos.addAll(data);
			notifyDataSetChanged();
		}
	}
	
	public void addMore(List<SearchTagVo> data){
		if(data != null && data.size() > 0){
			mSearchTagVos.addAll(data);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void clear(){
		mSearchTagVos.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==mSearchTagVos) {
			return 0;
		}
		return mSearchTagVos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==mSearchTagVos||mSearchTagVos.isEmpty()) {
			return null;
		}
		return mSearchTagVos.get(position);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.search_tag_list_item, null);
			holder.tvTag=(TextView) convertView.findViewById(R.id.tv_tag);
			holder.layoutContent=(LinearLayout) convertView.findViewById(R.id.layout_content);
			holder.tagView1=(ImageView) convertView.findViewById(R.id.tagView1);
			holder.tagView2=(ImageView) convertView.findViewById(R.id.tagView2);
			holder.tagView3=(ImageView) convertView.findViewById(R.id.tagView3);
			holder.tagView4=(ImageView) convertView.findViewById(R.id.tagView4);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		SearchTagVo searchTagVo=  mSearchTagVos.get(position);
		holder.tvTag.setBackgroundResource(TagView.getTagBgResId(mContext));
		holder.tvTag.setText(searchTagVo.getName());
		holder.tagView1.setVisibility(View.GONE);
		holder.tagView2.setVisibility(View.GONE);
		holder.tagView3.setVisibility(View.GONE);
		holder.tagView4.setVisibility(View.GONE);
//		s
		for (int i = 0; i < searchTagVo.getPetalks().size(); i++) {
			ImageView tagView = null;
			switch (i) {
			case 0:
				holder.tagView1.setVisibility(View.VISIBLE);
//				ImageLoader.getInstance().displayImage(searchTagVo.getSayVos().get(i).getPhotoUrl(), holder.tagView1, Constants.contentPetImgOptions);
				tagView = holder.tagView1;
				break;
			case 1:
				holder.tagView2.setVisibility(View.VISIBLE);
//				ImageLoader.getInstance().displayImage(searchTagVo.getSayVos().get(i).getPhotoUrl(), holder.tagView2, Constants.contentPetImgOptions);
				tagView = holder.tagView2;
				break;
			case 2:
				holder.tagView3.setVisibility(View.VISIBLE);
//				ImageLoader.getInstance().displayImage(searchTagVo.getSayVos().get(i).getPhotoUrl(), holder.tagView3, Constants.contentPetImgOptions);
				tagView = holder.tagView3;
				break;
			case 3:
				holder.tagView4.setVisibility(View.VISIBLE);
//				ImageLoader.getInstance().displayImage(searchTagVo.getSayVos().get(i).getPhotoUrl(), holder.tagView4, Constants.contentPetImgOptions);
				tagView = holder.tagView4;
				break;
			}
//			PicassoUtile.loadImg(mContext, searchTagVo.getSayVos().get(i).getPhotoUrl(), tagView);
            if(tagView != null)
			    ImageLoaderHelp.displayContentImage(searchTagVo.getPetalks().get(i).getPhotoUrl(), tagView);
		}
		return convertView;
	}
	
	private class Holder{
		private TextView tvTag;
		private ImageView tagView1,tagView2,tagView3,tagView4;
		private LinearLayout layoutContent;
		
	}

}
