package com.petsay.activity.shop.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.shop.GoodsVo;

public class ShopFreeAllAdapter extends BaseAdapter {

	private Context mContext;
    private List<GoodsVo> goodsVos;
    public int currentSelectIndex=0;
	public ShopFreeAllAdapter(Context context) {
		mContext = context;
		this.goodsVos=new ArrayList<GoodsVo>();
	}
	
	public void refreshData(List<GoodsVo> freeShopVos){
		if (freeShopVos != null && freeShopVos.size() > 0) {
			goodsVos.clear();
			goodsVos.addAll(freeShopVos);
		
			notifyDataSetChanged();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.shop_allfree_list_item, null);
			holder = new Holder();
			holder.imgPet = (ImageView) convertView.findViewById(R.id.img_pet);
			holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tvPrice=(TextView) convertView.findViewById(R.id.tv_price);
			holder.tvPostType=(TextView) convertView.findViewById(R.id.tv_posttype);
			holder.tvBuy=(TextView) convertView.findViewById(R.id.tv_status);
			holder.tvAllCount = (TextView) convertView.findViewById(R.id.tv_allcount);
			holder.tvJoinCount=(TextView) convertView.findViewById(R.id.tv_joincount);
			holder.tvRemaindays=(TextView) convertView.findViewById(R.id.tv_remaindays);
			holder.layoutCommentRoot=(LinearLayout) convertView.findViewById(R.id.layout_comment_root);
			holder.layoutStrike=(RelativeLayout) convertView.findViewById(R.id.layout_strike);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final GoodsVo goodsVo=goodsVos.get(position);
		holder.layoutCommentRoot.setBackgroundColor(Color.WHITE);
		ImageLoaderHelp.displayImage(goodsVo.getCoverUrl(), holder.imgPet);
		holder.tvPrice.setText(goodsVo.getPrice()+"");
		holder.tvContent.setText(goodsVo.getDescription());
		holder.tvAllCount.setText("试用数："+goodsVo.getInventory());
		holder.tvJoinCount.setText("已参与："+goodsVo.getApply());
		

			holder.tvPrice.setTextColor(Color.GRAY);
			holder.layoutStrike.setVisibility(View.VISIBLE);
			Drawable drawable=mContext. getResources().getDrawable(R.drawable.coin_icon_gray);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			holder.tvPrice.setCompoundDrawables(drawable,null,null,null);
		
		if (goodsVo.getPostageType()==0) {
			holder.tvPostType.setVisibility(View.VISIBLE);
		}else {
			holder.tvPostType.setVisibility(View.GONE);
		}
		
		switch (goodsVo.getState()) {
		case 20:
			holder.tvRemaindays.setText(mContext.getString(R.string.shop_start_trial));
			holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			holder.tvBuy.setText(mContext.getString(R.string.shop_start_trial));
//			holder.tvRemaindays.setVisibility(View.GONE);
			break;
		case 21:
			holder.tvRemaindays.setText("距离结束："+PublicMethod.calcDaysFromToday(goodsVo.getEndTime()));
			if (goodsVo.getOrderState().equals("41")) {
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				holder.tvBuy.setText(mContext.getString(R.string.shop_applying));
			}else if (goodsVo.getOrderState().equals("42")||goodsVo.getOrderState().equals("44")) {
//				holder.tvBuy.setText("申请成功");
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				holder.tvBuy.setText(mContext.getString(R.string.shop_apply_success));
			}else if (goodsVo.getOrderState().equals("43")) {
//				holder.tvBuy.setText("申请失败");
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
				holder.tvBuy.setText(mContext.getString(R.string.shop_apply_failed));
			}else {
//				holder.tvBuy.setText("申请试用");
				holder.tvBuy.setBackgroundResource(R.drawable.shop_buy);
				holder.tvBuy.setText(mContext.getString(R.string.shop_applytrial));
			}
			break;
		case 22:
			holder.tvRemaindays.setText(mContext.getString(R.string.shop_end_trial));
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
		case 23:
			holder.tvRemaindays.setText(mContext.getString(R.string.shop_end_trial));
			holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			holder.tvBuy.setText(mContext.getString(R.string.shop_goods_already_over));
		default:
			holder.tvBuy.setBackgroundResource(R.drawable.shop_buy_unclick);
			holder.tvBuy.setText(mContext.getString(R.string.shop_no_goods));
			break;
		}
//	}
		
		return convertView;
	}

	private class Holder {
		private ImageView imgPet;
		private TextView tvContent,tvAllCount,tvJoinCount,tvRemaindays;
		private TextView tvPrice,tvBuy,tvPostType;
		private LinearLayout layoutCommentRoot;
		private RelativeLayout layoutStrike;
	}
}