package com.petsay.component.view.postcard;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import com.petsay.R;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.StringUtiles;

public class AddOrderView  extends LinearLayout {

	private TextView mTvOrderCount,mTvOrderPrice;
	private Button mBtnCommitOrder;
	private LinearLayout mLayoutCount;
	private LinearLayout mLayoutOrderCount;
	private Context mContext;
	private int mOrderCount=0;
	private float mOrderTotalPrice=0;
	private SpannableString spanText ;
	private String mStr_TvOrderPriceStr="共0元";
	
	public AddOrderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}
	
	private void initView(){
		inflate(mContext, R.layout.add_order, this);
		mTvOrderCount=(TextView) findViewById(R.id.tv_order_totalcount);
		mTvOrderPrice=(TextView) findViewById(R.id.tv_order_price);
		mLayoutOrderCount=(LinearLayout) findViewById(R.id.layout_order_count);
		mLayoutCount=(LinearLayout) findViewById(R.id.layout_count);
		mBtnCommitOrder=(Button) findViewById(R.id.btn_commit_order);
		
		
		spanText=new SpannableString(mStr_TvOrderPriceStr);
		spanText.setSpan(new ForegroundColorSpan(Color.rgb(0xff, 0x74, 0x71)), 1,
                mStr_TvOrderPriceStr.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		spanText.setSpan(new AbsoluteSizeSpan(20, true), 1, spanText.length()-1,
			    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		mTvOrderPrice.setText(spanText);
		
	}
	
	
	public void  setClickListener(OnClickListener clickListener){
		//R.id.layout_count
		mLayoutCount.setOnClickListener(clickListener);
		mBtnCommitOrder.setOnClickListener(clickListener);
	}
	
	public void addOrder(float price){
		mOrderCount++;
		mTvOrderCount.setText(" X "+mOrderCount);
//		mOrderTotalPrice+=price;
		mOrderTotalPrice=PublicMethod.AddFloat(mOrderTotalPrice, price);
		mStr_TvOrderPriceStr="共"+mOrderTotalPrice+"元";
		spanText=new SpannableString(mStr_TvOrderPriceStr);
		spanText=StringUtiles.formatSpannableString(mStr_TvOrderPriceStr, 1, spanText.length()-1, Color.rgb(0xff, 0x74, 0x71), 18);
		mTvOrderPrice.setText(spanText);
	}
	
	public void minusOrder(float price){
		mOrderCount--;
		mTvOrderCount.setText(" X "+mOrderCount);
//		mOrderTotalPrice-=price;
		mOrderTotalPrice=PublicMethod.MinusFloat(mOrderTotalPrice, price);
		mStr_TvOrderPriceStr="共"+mOrderTotalPrice+"元";
		spanText=new SpannableString(mStr_TvOrderPriceStr);
		spanText=StringUtiles.formatSpannableString(mStr_TvOrderPriceStr, 1, spanText.length()-1, Color.rgb(0xff, 0x74, 0x71), 18);
		mTvOrderPrice.setText(spanText);
	}
	
	public void setOrderCount(int orderCount,float onePrice){
		mOrderCount=orderCount;
		mTvOrderCount.setText(" X "+mOrderCount);
		mOrderTotalPrice=onePrice*orderCount;
		mStr_TvOrderPriceStr="共"+PublicMethod.formatFloat(2, mOrderTotalPrice)+"元";
		spanText=new SpannableString(mStr_TvOrderPriceStr);
		spanText=StringUtiles.formatSpannableString(mStr_TvOrderPriceStr, 1, spanText.length()-1, Color.rgb(0xff, 0x74, 0x71), 18);
		mTvOrderPrice.setText(spanText);
	}
	
	public void setTotalPrice(float totalPrice){
		mLayoutOrderCount.setVisibility(View.GONE);
		mOrderTotalPrice=totalPrice;
		mStr_TvOrderPriceStr="共"+PublicMethod.formatFloat(2, mOrderTotalPrice)+"元";
		spanText=new SpannableString(mStr_TvOrderPriceStr);
		spanText=StringUtiles.formatSpannableString(mStr_TvOrderPriceStr, 1, spanText.length()-1, Color.rgb(0xff, 0x74, 0x71), 18);
		mTvOrderPrice.setText(spanText);
		
	}

    public void setOrderIcon(int resId){
        mTvOrderCount.setCompoundDrawablesWithIntrinsicBounds(resId,0,0,0);
    }

}
