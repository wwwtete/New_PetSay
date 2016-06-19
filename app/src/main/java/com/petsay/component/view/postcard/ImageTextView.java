package com.petsay.component.view.postcard;

import com.petsay.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageTextView extends RelativeLayout{

	private Context mContext;
	private ImageView mImg;
	private TextView mTv;
	private AttributeSet mAttributeSet;
	private boolean mIsSelected=false;
	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		mAttributeSet=attrs;
		initView();
		
	}
	
	private void initView(){
		inflate(mContext, R.layout.view_imagetext, this);
		mImg=(ImageView) findViewById(R.id.imgtv_img);
		mTv=(TextView) findViewById(R.id.imgtv_text);
		
		TypedArray a = mContext.obtainStyledAttributes(mAttributeSet, R.styleable.imagetext);//TypedArray是一个数组容器  
		String infoType=a.getString(R.styleable.imagetext_itv_text);
		mTv.setText(infoType);
		mImg.setImageResource(a.getResourceId(R.styleable.imagetext_itv_background, R.drawable.clothing_type_default));
		a.recycle();
	}
	
	public  void setBackgroundResource(int resId){
		mImg.setImageResource(resId);
		mIsSelected=false;
	}
	public void setSelectBackgroundResource(int resId){
		mImg.setImageResource(resId);
		mIsSelected=true;
	}
	
	public boolean isSelected(){
		return mIsSelected;
	}
	
	public String getText(){
		return mTv.getText().toString();
	}

}
