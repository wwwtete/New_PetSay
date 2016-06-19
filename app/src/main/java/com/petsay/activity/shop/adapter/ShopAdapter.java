package com.petsay.activity.shop.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.shop.ShopAllFreeActivity;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.shop.GoodsVo;

public class ShopAdapter extends BaseAdapter {

	private Context mContext;
    private List<GoodsVo> goodsVos;
    public int currentSelectIndex=0;
    private int freeSize=0,chargeSize=0;
	public ShopAdapter(Context context) {
		mContext = context;
		this.goodsVos=new ArrayList<GoodsVo>();
	}
	
	public void refreshData(List<GoodsVo> freeShopVos,List<GoodsVo> chargeShopVos){
		if (freeShopVos != null && freeShopVos.size() > 0) {
			freeSize=freeShopVos.size();
			goodsVos.clear();
			goodsVos.addAll(freeShopVos);
			if(chargeShopVos != null && chargeShopVos.size() > 0){
				chargeSize=chargeShopVos.size();
				goodsVos.addAll(chargeShopVos);
			}
			notifyDataSetChanged();
		}else {
			freeSize=0;
			if(chargeShopVos != null && chargeShopVos.size() > 0){
				chargeSize=chargeShopVos.size();
				goodsVos.clear();
				goodsVos.addAll(chargeShopVos);
				notifyDataSetChanged();
			}
		}
		
	}
	
	public void addMore(List<GoodsVo> sayVos){
		if(sayVos != null && sayVos.size() > 0){
			goodsVos.addAll(sayVos);
			notifyDataSetChanged();
		}else {
			PublicMethod.showToast(mContext, R.string.no_more);
		}
	}
	
	public void clear(){
		goodsVos.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null==goodsVos) {
			return 0;
		}
		return  goodsVos.size();
	}

	@Override
	public Object getItem(int position) {
		if (null==goodsVos||goodsVos.isEmpty()) {
			return null;
		}
		return  goodsVos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_list_item, null);
			holder = new Holder();
			holder.imgPet = (ImageView) convertView.findViewById(R.id.img_pet);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.price = (TextView) convertView.findViewById(R.id.tv_price);
			holder.tvType=(TextView) convertView.findViewById(R.id.tv_type);
			holder.tvMore=(TextView) convertView.findViewById(R.id.tv_more);
			holder.viewSpace=convertView.findViewById(R.id.view_space);
			holder.tvPostType=(TextView) convertView.findViewById(R.id.tv_posttype);
			holder.tvBuy=(TextView) convertView.findViewById(R.id.tv_status);
//			holder.tvName=(TextView) convertView.findViewById(R.id.tv_name);
			holder.layoutCommentRoot=(LinearLayout) convertView.findViewById(R.id.layout_comment_root);
			holder.layoutStrike=(RelativeLayout) convertView.findViewById(R.id.layout_strike);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final GoodsVo goodsVo=goodsVos.get(position);
		holder.layoutCommentRoot.setBackgroundColor(Color.WHITE);
		ImageLoaderHelp.displayContentImage(goodsVo.getCoverUrl(), holder.imgPet);
		
		if (freeSize==0) {
			holder.viewSpace.setVisibility(View.GONE);
			holder.tvMore.setVisibility(View.GONE);
			if (position==0) {
				holder.tvType.setVisibility(View.VISIBLE);
				holder.tvType.setText("宠豆商城");
			}else {
				holder.tvType.setVisibility(View.GONE);
			}
		}else {
			if (position==0) {
				holder.viewSpace.setVisibility(View.GONE);
				holder.tvType.setVisibility(View.VISIBLE);
				holder.tvType.setText("免费试用");
			}else if(position==freeSize){	
				holder.viewSpace.setVisibility(View.VISIBLE);
				holder.tvType.setVisibility(View.VISIBLE);
				holder.tvType.setText("宠豆商城");
			}else {
				holder.viewSpace.setVisibility(View.GONE);
				holder.tvType.setVisibility(View.GONE);
			}
			
			if (position==freeSize-1) {
				holder.tvMore.setVisibility(View.VISIBLE);
				holder.tvMore.setText("查看全部试用");
			}else {
				holder.tvMore.setVisibility(View.GONE);
			}
		}
		
		if (position<freeSize) {
			holder.price.setTextColor(Color.GRAY);
			holder.layoutStrike.setVisibility(View.VISIBLE);
			Drawable drawable=mContext. getResources().getDrawable(R.drawable.coin_icon_gray);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			holder.price.setCompoundDrawables(drawable,null,null,null);
		}else {
			holder.price.setTextColor(mContext.getResources().getColor(R.color.shop_coin));
			holder.layoutStrike.setVisibility(View.GONE);
		}
		
		if (goodsVo.getPostageType()==0) {
			holder.tvPostType.setVisibility(View.VISIBLE);
		}else {
			holder.tvPostType.setVisibility(View.GONE);
		}
		
//		if (goodsVo.getSalesMode()==GoodsVo.SalesMode_Free) {
			switch (goodsVo.getState()) {
			case 20:
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				holder.tvBuy.setText(mContext.getString(R.string.shop_start_trial));
				break;
			case 21:
				
				if (goodsVo.getOrderState().equals("41")) {
					holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
					holder.tvBuy.setText(mContext.getString(R.string.shop_applying));
				}else if (goodsVo.getOrderState().equals("42")||goodsVo.getOrderState().equals("44")) {
//					holder.tvBuy.setText("申请成功");
					holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
					holder.tvBuy.setText(mContext.getString(R.string.shop_apply_success));
				}else if (goodsVo.getOrderState().equals("43")) {
//					holder.tvBuy.setText("申请失败");
					holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
					holder.tvBuy.setText(mContext.getString(R.string.shop_apply_failed));
				}else {
//					holder.tvBuy.setText("申请试用");
					holder.tvBuy.setBackgroundResource(R.drawable.shop_buy);
					holder.tvBuy.setText(mContext.getString(R.string.shop_applytrial));
				}
				break;
			case 22:
				if (goodsVo.getOrderState().equals("41")) {
					holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
					holder.tvBuy.setText(mContext.getString(R.string.shop_applying));
				}else if (goodsVo.getOrderState().equals("42")||goodsVo.getOrderState().equals("44")) {
					holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
					holder.tvBuy.setText(mContext.getString(R.string.shop_apply_success));
				}else if (goodsVo.getOrderState().equals("43")) {
					holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
					holder.tvBuy.setText(mContext.getString(R.string.shop_apply_failed));
				}else {
					holder.tvBuy.setBackgroundResource(R.drawable.shop_buy);
					holder.tvBuy.setText(mContext.getString(R.string.shop_applytrial));
				}
				break;
			case 10:
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				holder.tvBuy.setText(mContext.getString(R.string.shop_start_exchange));
				break;
			case 11:
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy);
				holder.tvBuy.setText(mContext.getString(R.string.shop_exchange));
				break;
			case 12:
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				holder.tvBuy.setText(mContext.getString(R.string.shop_no_goods));
				break;
			default:
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				holder.tvBuy.setText(mContext.getString(R.string.shop_no_goods));
				break;
			}
//		}
//	else {
//			
//		}
		holder.tvMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext,ShopAllFreeActivity.class);
				mContext.startActivity(intent);
			}
		});
		holder.tvContent.setText(goodsVo.getDescription());
		holder.price.setText(goodsVo.getPrice()+"");

		
		return convertView;
	}

	private class Holder {
		private ImageView imgPet;
		private TextView tvContent,tvType,tvMore,tvBuy,tvPostType;
		private TextView price;
		private View viewSpace;
		private LinearLayout layoutCommentRoot;
		private RelativeLayout layoutStrike;
	}
}