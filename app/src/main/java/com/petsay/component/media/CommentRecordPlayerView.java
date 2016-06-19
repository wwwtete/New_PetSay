package com.petsay.component.media;

import com.petsay.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 回复语音View
 * @author wangw
 *
 */
public class CommentRecordPlayerView extends LinearLayout {

	private Context mContext;
	//声音效果
	private ImageView imgVoice;
	//声音时长
	private TextView tvSecond;
	private AnimationDrawable mDrawable;
	private ProgressBar mBar;
	private View mLayoutView;
	private String mAudioPath;
	private String mAudioUrl;

	public CommentRecordPlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.comment_record_player, this);
		mContext = context;
		initView();
	}

	public CommentRecordPlayerView(Context context) {
		super(context);
		inflate(context, R.layout.comment_record_player, this);
		mContext = context;
		initView();
	}

	private void initView() {
		imgVoice=(ImageView) findViewById(R.id.img_voice);
		tvSecond=(TextView) findViewById(R.id.tv_second);
		mBar = (ProgressBar) findViewById(R.id.pro_loading);
		mLayoutView = findViewById(R.id.layout_tip);
		mDrawable = (AnimationDrawable) imgVoice.getDrawable();
		stopAnimation();
	}

	public void reset(){
		mBar.setVisibility(View.GONE);
		mLayoutView.setVisibility(View.VISIBLE);
	}

	public void setDownloadIng(boolean downloading){
		if(downloading){
			mBar.setVisibility(View.VISIBLE);
			mLayoutView.setVisibility(View.INVISIBLE);
		}else {
			reset();
		}
	}
	
	public void playAnimation(){
		if(mDrawable != null && !mDrawable.isRunning()){
			mDrawable.start();
		}
	}
	
	public void stopAnimation(){
		if(mDrawable != null){
			mDrawable.stop();
		}
	}

	public void  setAudioSecond(String time){
		tvSecond.setText(time);
	}

	public void setAudioPath(String path){
		mAudioPath = path;
	}

	public void setAudioUrl(String url){
		mAudioUrl = url;
	}

	public String getAudioUrl(){
		return mAudioUrl;
	}

	public String getAudioPath(){
		return mAudioPath;
	}
}