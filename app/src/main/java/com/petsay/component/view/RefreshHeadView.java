package com.petsay.component.view;

import com.petsay.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @author wangw
 * 下拉刷新头部View
 */
public class RefreshHeadView extends LinearLayout {

	private int[] mInitFrames={R.drawable.ref_head_01,R.drawable.ref_head_02
			,R.drawable.ref_head_03,R.drawable.ref_head_04
			,R.drawable.ref_head_05,R.drawable.ref_head_06
			,R.drawable.ref_head_07,R.drawable.ref_head_08};
	
	private ImageView mImg;
	private int mCurrentFrame;
	private AnimationDrawable mDrawable;
	
	public RefreshHeadView(Context context){
		super(context);
		initView();
	}
	
	public RefreshHeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private void initView(){
		View view = inflate(getContext(), R.layout.refresh_header_view, this);
		mImg = (ImageView) view.findViewById(R.id.img_anim);
		mCurrentFrame = 0;
	}
	
	public void nextFrame(){
		mCurrentFrame++;
		if(mCurrentFrame < mInitFrames.length){
			setFrame(mInitFrames[mCurrentFrame]);
		}else {
			mCurrentFrame = mInitFrames.length;
		}
	}
	
	public void preFrame(){
		mCurrentFrame--;
		if(mCurrentFrame > -1){
			setFrame(mInitFrames[mCurrentFrame]);
		}else {
			mCurrentFrame = 0;
		}
	}
	
	public void reset(){
		mCurrentFrame = 0;
		setFrame(mInitFrames[mCurrentFrame]);
	}
	
	protected void setFrame(int resId){
		if(mImg != null){
			mImg.setImageResource(resId);
		}
	}
	
	public void playGif(){
		mImg.setImageResource(R.anim.refresh_head_animation);
		mDrawable = (AnimationDrawable) mImg.getDrawable();
		mDrawable.start();
	}
	
	public void stopGif(){
		if(mDrawable != null){
			mDrawable.stop();
		}
	}
	

}
