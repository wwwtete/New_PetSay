package com.petsay.component.view;

import com.petsay.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author wangw
 * 动态显示声音大小控件
 */
public class VolumeView extends FrameLayout {

	private ImageView mImg;
	private TextView mTvTime;
	
	public VolumeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}
	
	private void initViews() {
		inflate(getContext(), R.layout.volume_view, this);
		mImg = (ImageView) findViewById(R.id.img_icon);
		mTvTime = (TextView) findViewById(R.id.tv_time);
	}
	
	
	public void setRecordTime(String time){
		mTvTime.setText(time);
	}
	
	public String getRecordTime(){
		return mTvTime.getText().toString();
	}

	public void volumeChange(int change){
		if(change <= 10){
			setImageResource(R.drawable.micro001);
		}else if(change >10 && change <= 20){
			setImageResource(R.drawable.micro002);
		}else if(change >20 && change <= 30){
			setImageResource(R.drawable.micro003);
		}else if(change >30 && change <= 40){
			setImageResource(R.drawable.micro004);
		}else if(change >40 && change <= 50){
			setImageResource(R.drawable.micro005);
		}else if(change >50 && change <= 60){
			setImageResource(R.drawable.micro006);
		}else if(change >60 && change <= 70){
			setImageResource(R.drawable.micro007);
		}else if(change >70 && change <= 80){
			setImageResource(R.drawable.micro008);
		}else if(change >80 && change <= 90){
			setImageResource(R.drawable.micro009);
		}else {
			setImageResource(R.drawable.micro010);
		}
	}

	private void setImageResource(int resId) {
		mImg.setImageResource(resId);
	}
	
	

	
}
