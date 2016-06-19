package com.petsay.activity.settings;

import roboguice.inject.InjectView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.constants.Constants;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * @author wangw
 * 设置播放模式
 */
public class SetPlayMode_Activity extends BaseActivity implements OnClickListener {

	@InjectView(R.id.iv_wifiauto)
	private ImageView mIvWifi;
	@InjectView(R.id.iv_auto)
	private ImageView mIvAuto;
	@InjectView(R.id.iv_noauto)
	private ImageView mIvNo;
	private SharePreferenceCache mShare;
	private ImageView mCurrView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setplaymode_layout);
		initView();
	}

	@Override
	protected void initView() {
		super.initView();
		initTitleBar("播放设置");
		mTitleBar.setFinishEnable(true);
		findViewById(R.id.layout_wifiauto).setOnClickListener(this);
		findViewById(R.id.layout_auto).setOnClickListener(this);
		findViewById(R.id.layout_noauto).setOnClickListener(this);

		mShare = SharePreferenceCache.getSingleton(this);
		Constants.PLAY_GIF_MODE = mShare.getPlayMode();
		setPlayMode(Constants.PLAY_GIF_MODE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_wifiauto:
			setPlayMode(1);
			break;

		case R.id.layout_auto:
			setPlayMode(2);
			break;

		case R.id.layout_noauto:
			setPlayMode(0);
			break;
		}
	}

	private void setPlayMode(int mode){
		mShare.setPlaymode(mode);
		Constants.PLAY_GIF_MODE = mode;
		setSelectView(mode);
	}

	private void setSelectView(int mode){
		ImageView view = null;
		switch (mode) {
		case 0:
			view = mIvNo;
			break;
		case 1:
			view = mIvWifi;
			break;
		case 2:
			view = mIvAuto;
			break;
		}
		if(view != null){
			if(mCurrView != null)
				mCurrView.setImageBitmap(null);
			view.setImageResource(R.drawable.icon_playmode_ok);
			mCurrView = view;
		}
	}


}
