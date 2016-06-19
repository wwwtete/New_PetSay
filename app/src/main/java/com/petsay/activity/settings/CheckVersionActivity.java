package com.petsay.activity.settings;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.component.view.TitleBar;
import com.petsay.application.CheckVersionManager;
import com.petsay.application.CheckVersionManager.OnCheckVersionListtener;
import com.petsay.utile.PublicMethod;

public class CheckVersionActivity extends BaseActivity implements OnClickListener,OnCheckVersionListtener {
	private TextView mTvUserProtocol, mTvCheckVersion;
	private TitleBar mTitleBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_version);
		initView();
		initTitleBar();
		setListener();
//		mUserModule=UserModule.getSingleton();
//		mUserData=new UserData(handler);
	}

	protected void initView() {
		super.initView();
		mTvUserProtocol = (TextView) findViewById(R.id.tv_userProtocol);
		mTvUserProtocol.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		mTvCheckVersion = (TextView) findViewById(R.id.tv_checkversion);
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
	}
	
	

	private void setListener() {
		mTvUserProtocol.setOnClickListener(this);
		mTvCheckVersion.setOnClickListener(this);
	}

	private void initTitleBar() {
		mTitleBar.setTitleText(R.string.checkversion_title);
		mTitleBar.setFinishEnable(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_userProtocol:
			jumpWebViewActivity();
			break;
		case R.id.tv_checkversion:
//			mUserModule.addListener(mUserData);
//			mUserModule.systemVersion(mUserData);
			CheckVersionManager.getSingleton().checkVersion(CheckVersionActivity.this,this);
			break;
		}

	}

	@Override
	public void onFinish(boolean hasNew) {
		if (!hasNew) {
			mTvCheckVersion.setText("当前已是最新版本");
			mTvCheckVersion.setBackgroundResource(R.drawable.oldversion);
			PublicMethod.showToast(this, "当前版本已为最新版本");
			mTvCheckVersion.setClickable(false);
		}else {
			
		}
	}
	
	private void jumpWebViewActivity(){
		Intent intent = new Intent();
		intent.putExtra("folderPath", "宠物说用户协议");
		intent.putExtra("url", "file:///android_asset/agreement.html");
		intent.setClass(CheckVersionActivity.this, WebViewActivity.class);
		startActivity(intent);
	}
	
}
