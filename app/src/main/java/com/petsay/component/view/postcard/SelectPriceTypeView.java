package com.petsay.component.view.postcard;

import com.petsay.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 价格类型选择view
 * 
 * @author GJ
 *
 */
public class SelectPriceTypeView extends LinearLayout {

	private Context mContext;
	private LinearLayout mLayoutPrice, mLayoutMPrice;

	private boolean mEnableMCard = false;

	public SelectPriceTypeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		inflate(mContext, R.layout.view_select_price_type, this);
		// mLayoutMCard=(LinearLayout) findViewById(R.id.layout_mcard);
		// mLayoutNoMCard=(LinearLayout) findViewById(R.id.layout_no_mcard);
		mLayoutMPrice = (LinearLayout) findViewById(R.id.layout_mprice);
		mLayoutPrice = (LinearLayout) findViewById(R.id.layout_price);
		setEnableMCard(false);
	}

	private void setEnableMCard(boolean enableMCard) {
		mEnableMCard = enableMCard;
		if (mEnableMCard) {
			mLayoutMPrice.setVisibility(View.VISIBLE);
			mLayoutPrice.setBackgroundResource(R.drawable.price_default);
			mLayoutPrice.setOrientation(LinearLayout.VERTICAL);
			mLayoutPrice.setGravity(Gravity.CENTER);
		} else {
			mLayoutMPrice.setVisibility(View.GONE);
			mLayoutPrice.setBackgroundColor(Color.WHITE);
			mLayoutPrice.setPadding(0, 10, 0, 10);
			mLayoutPrice.setOrientation(LinearLayout.HORIZONTAL);
			mLayoutPrice.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		}
	}

	/**
	 * 设置商品价格
	 * 
	 * @param price
	 * @param mprice
	 *            没有此项则设置为0
	 */
	public void setPrice(float price, float mprice) {

	}

}
