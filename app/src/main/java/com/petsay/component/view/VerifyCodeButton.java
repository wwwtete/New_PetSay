package com.petsay.component.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.petsay.R;

/**
 * @author wangw
 *
 */
public class VerifyCodeButton extends Button {

	private CountDownTimer mDownTimer; 
	private int mEnabledBg;
	
	public VerifyCodeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	
	private void initView() {
		mEnabledBg = R.drawable.fasongyanzhengmanormal;
	}
	
	/**
	 * 启动倒计时
	 * @param totalTime
	 * @param countDownInterval
	 */
	public void start(long totalTime, long countDownInterval){
		setEnabled(false);
		initCountTimer(totalTime,countDownInterval);
		mDownTimer.start();
	}
	
	private CountDownTimer initCountTimer(long time,final long countDownInterval){
		if(mDownTimer != null){
			mDownTimer.cancel();
			mDownTimer = null;
		}
		mDownTimer = new CountDownTimer(time,countDownInterval) {

				@Override
				public void onTick(long millisUntilFinished) {
						VerifyCodeButton.this.setText("重发验证码"+millisUntilFinished/countDownInterval+"s");
				}

				@Override
				public void onFinish() {
					reset();
				}
			};
			return mDownTimer;
	}
	
	public void release(){
		if(mDownTimer != null){
			mDownTimer.cancel();
			mDownTimer = null;
		}
	}
	
	
	/**
	 * 重置
	 */
	public void reset(){
		VerifyCodeButton.this.setEnabled(true);
		VerifyCodeButton.this.setText(R.string.reg_send);
		VerifyCodeButton.this.setBackgroundResource(mEnabledBg);
	}
	

}
