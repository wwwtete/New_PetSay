package com.petsay.activity.shop.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.shop.GoodsOrderVo;
import com.petsay.vo.shop.GoodsVo;

public class ExchangeHistoryAdapter extends BaseAdapter {

	private Context mContext;
    private List<GoodsOrderVo> goodsOrderVos;
    public int currentSelectIndex=0;
	public ExchangeHistoryAdapter(Context context) {
		mContext = context;
		this.goodsOrderVos=new ArrayList<GoodsOrderVo>();
	}
	
	public void refreshData(List<GoodsOrderVo> freeShopVos){
		if (freeShopVos != null && freeShopVos.size() > 0) {
			goodsOrderVos.clear();
			goodsOrderVos.addAll(freeShopVos);
		
			notifyDataSetChanged();
		}
		
	}
	
	public void addMore(List<GoodsOrderVo> sayVos){
		if(sayVos != null && sayVos.size() > 0){
			goodsOrderVos.addAll(sayVos);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void clear(){
		goodsOrderVos.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==goodsOrderVos) {
			return 0;
		}
		return  goodsOrderVos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==goodsOrderVos||goodsOrderVos.isEmpty()) {
			return null;
		}
		return  goodsOrderVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_exchange_list_item, null);
			holder = new Holder();
			holder.imgPet = (ImageView) convertView.findViewById(R.id.img_goods);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvPrice=(TextView) convertView.findViewById(R.id.tv_price);
			holder.layoutCommentRoot=(LinearLayout) convertView.findViewById(R.id.layout_comment_root);
			holder.tvExchangeDate=(TextView) convertView.findViewById(R.id.tv_exchange_date);
			holder.tvKey=(TextView) convertView.findViewById(R.id.tv_key);
			holder.tvSupplier=(TextView) convertView.findViewById(R.id.tv_supplier);
			holder.tvPostType=(TextView) convertView.findViewById(R.id.tv_posttype);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final GoodsOrderVo goodsOrderVo=goodsOrderVos.get(position);
		holder.layoutCommentRoot.setBackgroundColor(Color.WHITE);
		ImageLoaderHelp.displayImage(goodsOrderVo.getGoodsCoverUrl(), holder.imgPet);
		holder.tvPrice.setText(goodsOrderVo.getGoodsPrice()+"");
		holder.tvExchangeDate.setText("兑换时间:"+PublicMethod.formatTimeToString(goodsOrderVo.getCreateTime(), "yyyy-MM-dd kk:mm"));
		holder.tvContent.setText(goodsOrderVo.getGoodsDescription());
//		holder.tvKey.setText("领取码："+goodsOrderVo.getNo());
		holder.tvKey.setText(Html.fromHtml("领取码：<font color='#3d62ff'>"+goodsOrderVo.getReceiveKey()+"</font>"));
		holder.tvSupplier.setText(goodsOrderVo.getGoodsSupplier());
//		if (goodsOrderVo.getGoodsSalesMode() == GoodsVo.SalesMode_Exchange) {
			if (goodsOrderVo.getState() == 44||goodsOrderVo.getState() == 32) {
				holder.tvPostType.setVisibility(View.VISIBLE);
			} else {
				holder.tvPostType.setVisibility(View.GONE);
			}
//		} else {
//			if (goodsOrderVo.getState() == 44) {
//
//			} else {
//
//			}
//		}
		return convertView;
	}

	private class Holder {
		private ImageView imgPet;
		private TextView tvContent;
		private TextView tvPrice,tvExchangeDate,tvPostType;
		private LinearLayout layoutCommentRoot;
		private TextView tvKey,tvSupplier;
	}
}