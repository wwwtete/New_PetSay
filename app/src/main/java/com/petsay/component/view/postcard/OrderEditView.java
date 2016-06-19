package com.petsay.component.view.postcard;

import com.petsay.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 订单列表中每个订单的数目编辑view
 * 
 * @author GJ
 *
 */
public class OrderEditView extends LinearLayout implements android.view.View.OnClickListener {

	private Context mContext;
	private ImageView mImgAdd, mImgMinus;
	private TextView mTvCount;
	private OnOrderButtonClickListener mOnOrderButtonClickListener;

	public OrderEditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		inflate(mContext, R.layout.view_order_edit, this);
		mImgAdd = (ImageView) findViewById(R.id.img_add);
		mImgMinus = (ImageView) findViewById(R.id.img_minus);
		mTvCount = (TextView) findViewById(R.id.tv_count);
		mImgAdd.setOnClickListener(this);
		mImgMinus.setOnClickListener(this);
		setOrderCount(1);
	}

	public void setOnOrderButtonClickListener(OnOrderButtonClickListener onOrderButtonClickListener) {
		mOnOrderButtonClickListener = onOrderButtonClickListener;
	}

	public int getOrderCount() {
		return Integer.parseInt(mTvCount.getText().toString());
	}
	
	public void setOrderCount(int count){
		mTvCount.setText(count+"");
		if (count>1) {
			mImgMinus.setEnabled(true);
		}else {
			mImgMinus.setEnabled(false);
		}
	}

	public void addOrderCount() {
		int count=Integer.parseInt(mTvCount.getText().toString())+1;
         mTvCount.setText(count+"");
	}

	public void minusOrderCount() {
		int count=Integer.parseInt(mTvCount.getText().toString())-1;
        mTvCount.setText(count+"");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_add:
			addOrderCount();
			mImgMinus.setEnabled(true);
            if (null!=mOnOrderButtonClickListener) {
                mOnOrderButtonClickListener.onAddClick(getOrderCount());
            }
			break;
		case R.id.img_minus:
			minusOrderCount();
			if (getOrderCount()<=1) {
				mImgMinus.setEnabled(false);
			}
            if (null!=mOnOrderButtonClickListener) {
				mOnOrderButtonClickListener.onMinusClick(getOrderCount());
			}

		default:
            break;
        }

    }

    public void setTextSize(float size){
        mTvCount.setTextSize(TypedValue.COMPLEX_UNIT_SP,size);
    }

    public void setTextColor(int color){
        mTvCount.setTextColor(color);
    }

    public void setTextBold(boolean bold){
        mTvCount.getPaint().setFakeBoldText(bold);
    }

    public interface OnOrderButtonClickListener {
        void onAddClick(int count);
        void onMinusClick(int count);
    }

	}


