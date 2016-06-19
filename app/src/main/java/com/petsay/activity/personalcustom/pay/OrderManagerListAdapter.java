package com.petsay.activity.personalcustom.pay;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.activity.chat.ChatActivity;
import com.petsay.activity.personalcustom.pay.fragment.OrderListFragment;
import com.petsay.constants.Constants;
import com.petsay.network.net.OrderNet;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderProductDTO;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderManagerListAdapter extends ExBaseAdapter<OrderDTO>{
	private int mType;
//    private OrderNet mNet;
    private OrderListFragment mFragment;

	public OrderManagerListAdapter(OrderListFragment fragment,int type) {
        super(fragment.getActivity());
        this.mFragment = fragment;
		mType=type;
//        this.mNet = net;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (null==convertView) {
			convertView=LayoutInflater.from(mContext).inflate(R.layout.order_manager_list_item, null);
			holder=new Holder();
			holder.imgContactCustomer=(ImageView) convertView.findViewById(R.id.img_contact_customer);
			holder.imgThumb=(ImageView) convertView.findViewById(R.id.img_thumb);
			holder.imgToPay=(ImageView) convertView.findViewById(R.id.img_topay);
			holder.tvCount=(TextView) convertView.findViewById(R.id.tv_order_count);
			holder.tvInfo=(TextView) convertView.findViewById(R.id.tv_orderinfo);
			holder.tvOnePrice=(TextView) convertView.findViewById(R.id.tv_order_oneprice);
			holder.tvOrderStatus=(TextView) convertView.findViewById(R.id.tv_orderstatus);
			holder.tvPostPrice=(TextView) convertView.findViewById(R.id.tv_post_price);
			holder.tvTotalPrice=(TextView) convertView.findViewById(R.id.tv_total_price);
			holder.tvCouponPrice=(TextView) convertView.findViewById(R.id.tv_coupon_price);
            holder.imgToPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object obj = v.getTag();
                    if(obj != null && obj instanceof OrderDTO){
                        OrderDTO dto = (OrderDTO) obj;
                        if("2".equals(dto.getState())){
//                            Intent intent = new Intent(mContext,OrderDetailsActivity.class);
//                            intent.putExtra("orderdto",dto);
//                            intent.putExtra("orderid", dto.getId());
//                            mContext.startActivity(intent);
                            mFragment.orderConfirm(v, dto);
                        }else if("5".equals(dto.getState())){
                            mFragment.receiveProduct(v,dto);
                        }
                    }
                }
            });
            holder.imgContactCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,ChatActivity.class);
                    intent.putExtra("petid", Constants.OFFICIAL_ID);
                    mContext.startActivity(intent);
                }
            });
			convertView.setTag(holder);
		}else {
			holder=(Holder) convertView.getTag();
		}
        OrderDTO dto = getItem(position);
        holder.imgToPay.setTag(dto);
        if (dto.getUseCoupon()) {
        	holder.tvCouponPrice.setVisibility(View .VISIBLE);
        	 holder.tvCouponPrice.setText("已优惠：￥"+dto.getDiscountAmount());
		}else {
			holder.tvCouponPrice.setVisibility(View .GONE);
		}
       
        if("2".equals(dto.getState())){
            holder.imgToPay.setImageResource(R.drawable.order_topay);
            holder.imgToPay.setVisibility(View.VISIBLE);
        }else if("5".equals(dto.getState())) {
            holder.imgToPay.setImageResource(R.drawable.querenshouhuoda);
            holder.imgToPay.setVisibility(View.VISIBLE);
        }else {
            holder.imgToPay.setVisibility(View.GONE);
        }
        holder.tvOrderStatus.setText(dto.getStateDesc());
        if(!dto.getOrderProducts().isEmpty()) {
            OrderProductDTO productDTO = dto.getOrderProducts().get(0);
            ImageLoaderHelp.displayContentImage(productDTO.getCover(), holder.imgThumb);
            holder.tvInfo.setText(productDTO.getName());
            holder.tvOnePrice.setText("￥ "+productDTO.getPrice());
        }else {
            holder.imgThumb.setImageBitmap(null);
            holder.tvInfo.setText("");
            holder.tvOnePrice.setText("");
        }
        holder.tvCount.setText("x" + dto.getProductCount());
        holder.tvPostPrice.setText("运费: ￥"+dto.getShippingFee());
        holder.tvTotalPrice.setText("￥"+dto.getAmount());
		return convertView;
	}

    private class Holder {
		private TextView tvOrderStatus, tvInfo, tvOnePrice, tvCount,
				tvTotalPrice, tvPostPrice,tvCouponPrice;
		private ImageView imgThumb, imgToPay, imgContactCustomer;

	}

}
