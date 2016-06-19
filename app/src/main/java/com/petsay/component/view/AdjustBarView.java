package com.petsay.component.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.utile.PublicMethod;

/**
 * @author wangw
 * 饰品微调条
 */
public class AdjustBarView extends RelativeLayout implements OnTouchListener {


	private ImageView mEnlarged;
	private ImageView mReduced;
	private ImageView mRotateleft;
	private ImageView mRotateright;
	private TextView mTxtReset;

	private AdjustBarCallback mCallback;
	private Timer mTimer;

	public AdjustBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	private void initView() {
		inflate(getContext(), R.layout.adjust_layout, this);
		mEnlarged = (ImageView)findViewById( R.id.enlarged );
		mEnlarged.setOnTouchListener(this);
		mReduced = (ImageView)findViewById( R.id.reduced );
		mReduced.setOnTouchListener(this);
		mRotateleft = (ImageView)findViewById( R.id.rotateleft );
		mRotateleft.setOnTouchListener(this);
		mRotateright = (ImageView)findViewById( R.id.rotateright );
		mRotateright.setOnTouchListener(this);
		mTxtReset = (TextView)findViewById( R.id.txt_reset );
		mTxtReset.setOnTouchListener(this);
	}

	public void setOnCallback(AdjustBarCallback callback){
		this.mCallback = callback;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.enlarged:
			onEnlarged(event);
			break;
		case R.id.reduced:
			onReduced(event);
			break;
		case R.id.rotateleft:
			onRotateleft(event);
			break;
		case R.id.rotateright:
			onRotateright(event);
			break;
		case R.id.txt_reset:
			onReset(event);
			break;
		}
		
		if(MotionEvent.ACTION_DOWN == event.getAction()){
			onStartTimer(v.getId());
		}else if(MotionEvent.ACTION_UP == event.getAction() || MotionEvent.ACTION_CANCEL == event.getAction()){
			onStopTimer();
		}
		
		return true;
	}

	private void onReset(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTxtReset.setTextColor(Color.parseColor("#969696"));
			if(mCallback != null)
				mCallback.onReset();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mTxtReset.setTextColor(Color.WHITE);
			break;
		}
	}

	private void onEnlarged(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mEnlarged.setImageResource(R.drawable.enlarged_pull);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mEnlarged.setImageResource(R.drawable.enlarged);
			break;
		}
	}

	private void onReduced(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mReduced.setImageResource(R.drawable.reduced_pull);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mReduced.setImageResource(R.drawable.reduced);
			break;
		}
	}

	private void onRotateleft(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mRotateleft.setImageResource(R.drawable.rotateleft_pull);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mRotateleft.setImageResource(R.drawable.rotateleft);
			break;
		}
	}

	private void onRotateright(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mRotateright.setImageResource(R.drawable.rotateright_pull);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mRotateright.setImageResource(R.drawable.rotateright);
			break;
		}
	}

	private void onStartTimer(final int resId){
		onStopTimer();
		if(mCallback ==  null)
			return;
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				switch (resId) {
				case R.id.enlarged:
					mCallback.onEnlarged();
					break;
				case R.id.reduced:
					mCallback.onReduced();
					break;
				case R.id.rotateleft:
					mCallback.onRotateleft();
					break;
				case R.id.rotateright:
					mCallback.onRotateright();
					break;
				}	
			}
		}, 0,200);
	}

	private void onStopTimer(){
		if(mTimer != null ){
			mTimer.cancel();
			mTimer = null;
		}
	}


	public interface AdjustBarCallback {
		/**放大*/
		public void onEnlarged();
		/**缩小*/
		public void onReduced();
		/**左旋转*/
		public void onRotateleft();
		/**右旋转*/
		public void onRotateright();
		/**重置*/
		public void onReset();
	}


}
