package com.petsay.activity.homeview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.homeview.MainActivity_Deprecated;
import com.petsay.component.view.NewFunctionStateView;

public class SlidingListAdapter extends BaseAdapter {
	private MainActivity_Deprecated mContext;
	private String[] _titles;
	private int[] mRightItemIconRess;
	public boolean hasUnreadMsg;
	
	public SlidingListAdapter(MainActivity_Deprecated context, String[] titles, int[] rightItemIconRess) {
		mContext = context;
		_titles = titles;
		mRightItemIconRess = rightItemIconRess;
	}

	@Override
	public int getCount() {
		return _titles.length;
	}

	@Override
	public Object getItem(int position) {
		return _titles[position];
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.sliding_list_item, null);
			holder.imgIcon=(ImageView) convertView.findViewById(R.id.img_icon);
			holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_content);
			holder.imgStateView = (NewFunctionStateView) convertView.findViewById(R.id.img_stateview);
			convertView.setTag(holder);
		} else
			holder = (Holder) convertView.getTag();
		// holder.imgIcon.setImageResource(mSkinNames[position]);
//		SkinHelp.setImageDrawable(holder.imgIcon, mContext, mContext.getString(mSkinNames[position]));
		holder.imgIcon.setImageResource(mRightItemIconRess[position]);
		holder.tvTitle.setText(_titles[position]);
		int curSelection=mContext.curPosition;
		
//		if (position==curSelection) {
//			holder.tvTitle.setTextColor(SkinManager.getInstance(mContext).getColor(mContext.getString(R.string.right_menu_text_color)));
//		}else {
			holder.tvTitle.setTextColor(mContext.getResources().getColor(R.color.list_content));
//		}
		
		checkNewFunctionState(_titles[position],holder.imgStateView);
		return convertView;
	}

	private void checkNewFunctionState(String title,NewFunctionStateView view){
		if(_titles[3].equals(title)){
			view.checkShow("topic_function");
		}else {
			view.setVisibility(View.GONE);
		}
	}
	
	private class Holder {
		private TextView tvTitle;
		private ImageView imgIcon;
		private NewFunctionStateView imgStateView;
	}

}
