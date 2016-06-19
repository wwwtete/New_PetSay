package com.petsay.component.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;

public class HorizontalListViewAdapter extends BaseAdapter{
	private int[] mIconIDs;
	private String[] mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = 0;

	public HorizontalListViewAdapter(Context context, String[] titles, int[] ids){
		this.mContext = context;
		this.mIconIDs = ids;
		this.mTitles = titles;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return mTitles.length;
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
//			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.mTitle=(TextView)convertView.findViewById(R.id.text_list_item);
			holder.mouthTab=(LinearLayout) convertView.findViewById(R.id.layout_mouth_tab);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			convertView.setSelected(true);
			holder.mTitle.setTextColor(Color.parseColor("#898989"));
		}else{
			holder.mTitle.setTextColor(R.color.white);
			convertView.setSelected(false);
		}
		
		holder.mTitle.setText(mTitles[position]);

		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle ;
//		private ImageView mImage;
		private LinearLayout mouthTab;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}
}