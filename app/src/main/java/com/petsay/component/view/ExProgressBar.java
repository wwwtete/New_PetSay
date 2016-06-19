package com.petsay.component.view;

import com.petsay.R;
import com.petsay.utile.PublicMethod;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author wangw
 * 带加载进度的ProgressBar
 */
public class ExProgressBar extends RelativeLayout implements AnimationListener {

	private View mView;
	private TextView mTvLbl;
	private ImageView mIvIcon;
	private Animation mCircleAnim;
	private Animation mHidenAnim;

	public ExProgressBar(Context context){
		super(context);
		onCreateView();
	}

	public ExProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		onCreateView();
	}

	private void onCreateView(){
		mView = inflate(getContext(), R.layout.progress_dialog_view, this);
		mIvIcon= (ImageView) mView.findViewById(R.id.imageView);
		mTvLbl = (TextView) mView.findViewById(R.id.tv_lbl);
		mTvLbl.setVisibility(View.VISIBLE);
		mCircleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.progress_dialog_window_anim);
		LinearInterpolator lin = new LinearInterpolator();
		mCircleAnim.setInterpolator(lin);
		mHidenAnim = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_out);
		mHidenAnim.setAnimationListener(this);
	}

	public void show(){
		if(this.getVisibility() != View.VISIBLE){
			mIvIcon.startAnimation(mCircleAnim);
			setVisibility(View.VISIBLE);
			this.clearAnimation();
		}
	}

	public void hiden(){
		mIvIcon.clearAnimation();
		setVisibility(View.GONE);
		//		setVisibility(View.VISIBLE);
		//		this.startAnimation(mHidenAnim);
	}

	public void updateProgress(float progress){
		//		PublicMethod.log_d("下载进度："+progress);
		mTvLbl.setText( String.format("%.1f", (progress*100))+"%");
	}

	@Override
	public void onAnimationStart(Animation animation) {
		mIvIcon.clearAnimation();
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		mTvLbl.setText("");
		this.setVisibility(View.GONE);
		this.clearAnimation();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

}
