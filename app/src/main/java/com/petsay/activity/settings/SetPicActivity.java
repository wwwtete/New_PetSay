package com.petsay.activity.settings;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.view.switchButton.SwitchButton;
import com.petsay.utile.PublicMethod;

/**
 * @author wangw
 * 设置图片
 */
public class SetPicActivity extends BaseActivity {

//	@InjectView(R.id.iv_wifiauto)
//	private ImageView mIvWifi;
//	@InjectView(R.id.iv_auto)
//	private ImageView mIvAuto;
//	@InjectView(R.id.iv_noauto)
//	private ImageView mIvNo;
//	private SharePreferenceCache mShare;
//	private ImageView mCurrView;
	private SwitchButton mSwBtnPic,mSwBtnCamera;
    private SharePreferenceCache mSharePreferenceCache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setpic_layout);
		initView();
		mSharePreferenceCache=SharePreferenceCache.getSingleton(getApplicationContext());
		if (mSharePreferenceCache.getIsAutoSavePicMode()) {
			mSwBtnPic.setChecked(true);
		}
		if (mSharePreferenceCache.getIsAutoSaveCameraMode()) {
			mSwBtnCamera.setChecked(true);
		}
		
		
		
	}

	@Override
	protected void initView() {
		super.initView();
		initTitleBar("图片设置");
		mTitleBar.setFinishEnable(true);
		mSwBtnCamera=(SwitchButton) findViewById(R.id.swbtn_camera);
		mSwBtnPic=(SwitchButton) findViewById(R.id.swbtn_pic);
		
		
		mSwBtnCamera.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSharePreferenceCache.setIsAutoSaveCameraMode(isChecked);
			}
		});
		
		mSwBtnPic.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mSharePreferenceCache.setIsAutoSavePicMode(isChecked);
				
			}
		});
		
	}
}
