package com.petsay.component.animationview;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.view.ViewHelper;
import com.petsay.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AttentionButtonView extends RelativeLayout{
    private Context mContext;
    
    private TextView imgAdd,btnAlreadyAttention,tvAddAtt;
	private RelativeLayout rLayoutAttention;
	private LinearLayout layoutAttention;
	
	public AttentionButtonView(Context context) {
		super(context);
		mContext=context;
		initView();
	}
	
	public AttentionButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}
	
	private void initView(){
		inflate(mContext, R.layout.attention_anim_view, this);
		imgAdd=(TextView) findViewById(R.id.img_add);
		btnAlreadyAttention=(TextView) findViewById(R.id.btn_already_attention);
		tvAddAtt=(TextView) findViewById(R.id.tv_add_att);
		rLayoutAttention=(RelativeLayout) findViewById(R.id.rlayout_attention);
		layoutAttention=(LinearLayout) findViewById(R.id.layout_attention);
	}
	
	public  void startAnim(){
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.playTogether(ObjectAnimator.ofFloat(imgAdd,
				"rotation", 0, 360), ObjectAnimator.ofFloat(
				imgAdd, "scaleX", 1, 1.5f, 1), ObjectAnimator
				.ofFloat(imgAdd, "scaleY", 1, 1.5f, 1));
		animatorSet.setDuration(500);
		animatorSet.start();
		animatorSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				AnimatorSet set = new AnimatorSet();
				set.playSequentially(
						ObjectAnimator.ofFloat(imgAdd, "scale",1f, 0.5f).setDuration(100),
						ObjectAnimator.ofFloat(layoutAttention,"alpha", 1, 0).setDuration(300),
						ObjectAnimator.ofFloat(rLayoutAttention,"alpha", 1, 0).setDuration(300),
						ObjectAnimator.ofFloat(btnAlreadyAttention, "alpha", 0,1).setDuration(300),
						ObjectAnimator.ofFloat(btnAlreadyAttention, "alpha", 1,0).setDuration(300));
				set.start();
				set.addListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animator arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animator arg0) {
                        AttentionButtonView.this.setVisibility(View.GONE);
                        ViewHelper.setAlpha(imgAdd, 1f);
                		ViewHelper.setAlpha(rLayoutAttention, 1f);
                		ViewHelper.setAlpha(layoutAttention, 1f);
                        AttentionButtonView.this.setClickable(true);
					}

					@Override
					public void onAnimationCancel(Animator arg0) {
						// TODO Auto-generated method stub

					}
				});
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	public void showAttentionBtn(){
		ViewHelper.setAlpha(imgAdd, 1f);
		ViewHelper.setAlpha(rLayoutAttention, 1f);
		ViewHelper.setAlpha(layoutAttention, 1f);
//		ViewHelper.setAlpha(imgAdd, 1f);
//		ViewHelper.setAlpha(imgAdd, 1f);
	}
	
	
	public void setBackGroundRes(Drawable d){
		rLayoutAttention.setBackgroundDrawable(d);
	}
	public void setTextColorRes(int colorId){
		imgAdd.setTextColor(colorId);
		tvAddAtt.setTextColor(colorId);
		btnAlreadyAttention.setTextColor(colorId);
	}

}
