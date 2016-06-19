package com.petsay.component.view;

import com.petsay.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author wangw
 * TabTitleView
 */
public class TabTitleLayoutView extends LinearLayout {

	
	private TextView mTvTip;
	private TextView mTvCount;
	
	public TabTitleLayoutView(Context context){
		super(context);
		initView();
	}
	
	public TabTitleLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		inflate(getContext(), R.layout.tabtitle_view, this);
		mTvTip = (TextView) findViewById(R.id.tv_tip);
		mTvCount = (TextView) findViewById(R.id.tv_count);
	}
	
	public void setTitle(String title){
		mTvTip.setText(title);
	}
	
	public void setValue(String value){
		mTvCount.setText(value);
		
	}
	
	public void setCountVisibility(int flag){
		mTvCount.setVisibility(flag);
	}
	
	public void setTextColor(int color) {
		mTvCount.setTextColor(color);
		mTvTip.setTextColor(color);
	}
	
	
	
	
	

}
