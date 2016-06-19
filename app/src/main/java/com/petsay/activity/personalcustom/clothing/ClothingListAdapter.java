package com.petsay.activity.personalcustom.clothing;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.StringUtiles;
import com.petsay.vo.personalcustom.ProductDTO;

public class ClothingListAdapter extends BaseAdapter {

	private Context mContext;
    private List<ProductDTO> mProductDTOs;
    public int currentSelectIndex=0;
	public ClothingListAdapter(Context context) {
		mContext = context;
		this.mProductDTOs=new ArrayList<ProductDTO>();
	}
	
	public void refreshData(List<ProductDTO> dtos){
		if (dtos != null && dtos.size() > 0) {
			mProductDTOs.clear();
			mProductDTOs.addAll(dtos);
			notifyDataSetChanged();
		}
	}
	
	public void addMore(List<ProductDTO> dtos){
		if(dtos != null && dtos.size() > 0){
			mProductDTOs.addAll(dtos);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void clear(){
		mProductDTOs.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==mProductDTOs) {
			return 0;
		}
		return  mProductDTOs.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==mProductDTOs||mProductDTOs.isEmpty()) {
			return null;
		}
		return  mProductDTOs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null==convertView) {
			holder=new Holder();
			convertView=LayoutInflater.from(mContext).inflate(R.layout.clothing_list_item, null);
		    holder.img=(ImageView) convertView.findViewById(R.id.img_thumb);
		    holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
		    holder.tvPrice=(TextView) convertView.findViewById(R.id.tv_price);
		    holder.tvPostage=(TextView) convertView.findViewById(R.id.tv_postage);
//		    holder.tvMPrice=(TextView) convertView.findViewById(R.id.tv_mprice);
		    convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
		ProductDTO dto=mProductDTOs.get(position);
		holder.tvName.setText(dto.getName());
		ImageLoaderHelp.displayImage(dto.getCover(), holder.img, ImageLoaderHelp.GetOptions(R.drawable.pet1,ImageScaleType.EXACTLY_STRETCHED));
//		holder.tvPrice.setText("专属价格："+mProductDTOs.get(position).getPrice());
		String content="优惠价："+dto.getPrice()+"元/月";
		holder.tvPrice.setText(	StringUtiles.formatSpannableString(content, 4, content.length()-3, Color.rgb(0xff, 0x74, 0x71), 18));
		holder.tvPostage.setText("运费："+dto.getShippingFee()+"元");
		return convertView;
	}

	private class Holder{
		private TextView tvName;
		private ImageView img;
		private TextView tvPrice,tvPostage;
		
	}
}