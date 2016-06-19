package com.petsay.component.view.postcard;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.OrderProductDTO;
import com.petsay.vo.personalcustom.ProductDTO;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderItemView extends RelativeLayout {

	private Context mContext;
	
	private TextView mTvDesc,mTvOrderOnePrice,mTvOrderCount,mTvPostage;
	private ImageView mImgThumb;
	private OrderDTO mOrderDTO;
	public OrderItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mContext=context;
		initView();
	}
	
	private void initView(){
		inflate(mContext, R.layout.view_order_item, this);
		mTvDesc=(TextView) findViewById(R.id.tv_desc);
		mImgThumb=(ImageView) findViewById(R.id.img_thumb);
		mTvOrderOnePrice=(TextView) findViewById(R.id.tv_order_oneprice);
		mTvOrderCount=(TextView) findViewById(R.id.tv_order_count);
		mTvPostage=(TextView) findViewById(R.id.tv_order_postage);
	}
	
	public void setProduct(OrderDTO orderDTO){
		mOrderDTO=orderDTO;
		OrderProductDTO orderProductDTO=mOrderDTO.getOrderProducts().get(0);
		mTvDesc.setText(orderProductDTO.getName());
		ImageLoaderHelp.displayContentImage(orderProductDTO.getCover(), mImgThumb);
//		ImageLoaderHelp.displayContentImage(orderProductDTO.getCover(), mImgThumb);
		mTvOrderCount.setText("x"+mOrderDTO.getProductCount());
		mTvOrderOnePrice.setText("￥ "+orderProductDTO.getPrice());
		mTvPostage.setText("运费："+mOrderDTO.getShippingFee()+"元");
	}
	
	

}
