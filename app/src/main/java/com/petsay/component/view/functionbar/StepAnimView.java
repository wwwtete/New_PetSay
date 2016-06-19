package com.petsay.component.view.functionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.petsay.R;

/**
 * @author wangw
 * 踩的动画
 */
public class StepAnimView extends ImageView implements AnimationListener {

	private AnimationSet mAnimationSet;
	
	public StepAnimView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private void initView() {
		mAnimationSet = (AnimationSet) AnimationUtils.loadAnimation(getContext(), R.anim.step_anim);
		mAnimationSet.setAnimationListener(this);
	}
	
	public void startAnimation(){
//		this.setImageResource(mResId);
		this.setVisibility(View.VISIBLE);
		mAnimationSet.cancel();
		this.startAnimation(mAnimationSet);
	}
	
	public void refreshSkin(){
//		Drawable stepDrawable = SkinManager.getInstance(getContext()).getDrawable(getContext().getString(R.string.step_anim));
//		if(stepDrawable == null){
//			setImageResource(R.drawable.step_anim);
//		}else {
//			setImageDrawable(stepDrawable);
//		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		this.clearAnimation();
//		this.setImageDrawable(null);
		this.setVisibility(View.GONE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	

}
