package com.petsay.activity.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.global.guide.GuideActivity;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;

/**
 * 关于宠物说
 * @author G
 *
 */
public class SettingAboutPetsayActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout  mLayoutRate,mLayoutCheck, mLayoutWhat, mLayoutHelp;
	private TitleBar mTitleBar;

	private ProgressDialog mDialog;
	private TextView mTvUserProtocol;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_aboutpetsay);
		initView();
		setListener();
	}
	@Override
	protected void initView() {
		super.initView();
		initTitleBar();
		mLayoutRate=(RelativeLayout) findViewById(R.id.layout_rate);
		mLayoutCheck=(RelativeLayout) findViewById(R.id.layout_aboutus);
		mLayoutWhat=(RelativeLayout) findViewById(R.id.layout_whatpetsay);
		mLayoutHelp=(RelativeLayout)findViewById(R.id.layout_help);
		mTvUserProtocol = (TextView) findViewById(R.id.tv_userProtocol);
		mTvUserProtocol.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
		
		mLayoutRate.setBackgroundColor(Color.WHITE);
		mLayoutCheck.setBackgroundColor(Color.WHITE);
		mLayoutWhat.setBackgroundColor(Color.WHITE);
		mLayoutHelp.setBackgroundColor(Color.WHITE);
	}
	
	private void initTitleBar(){
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		mTitleBar.setTitleText("关于宠物说");
		mTitleBar.setFinishEnable(true);
	}
	
	private void setListener(){
		mTvUserProtocol.setOnClickListener(this);
		mLayoutRate.setOnClickListener(this);
		mLayoutCheck.setOnClickListener(this);
		mLayoutWhat.setOnClickListener(this);
		mLayoutHelp.setOnClickListener(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_userProtocol:
			jumpUserProtocolWebViewActivity();
			break;
		case R.id.layout_rate:
			onJumpMarketActivity();
			break;
		case R.id.layout_whatpetsay:
			onJumpWelcomeActivity();
			break;
		case R.id.layout_aboutus:
		    Intent intent=new Intent(SettingAboutPetsayActivity.this, CheckVersionActivity.class);
			startActivity(intent); 
			break;
		case R.id.layout_help:
			jumpWebViewActivity();
			break;
		}
	}


	private void onJumpMarketActivity() {
		try {
			Uri uri = Uri.parse("market://details?id="+Constants.PackageName); 
			Intent it = new Intent(Intent.ACTION_VIEW, uri); 
			startActivity(it); 			
		} catch (Exception e) {
			PublicMethod.showToast(SettingAboutPetsayActivity.this, "未检测到您手机安装任何应用市场");
		}
	}
	
	private void jumpUserProtocolWebViewActivity(){
		Intent intent = new Intent();
		intent.putExtra("folderPath", "宠物说用户协议");
		intent.putExtra("url", "file:///android_asset/agreement.html");
		intent.setClass(SettingAboutPetsayActivity.this, WebViewActivity.class);
		startActivity(intent);
	}

	private void jumpWebViewActivity(){
		Intent intent = new Intent();
		intent.putExtra("folderPath", "宠物说小助手");
		intent.putExtra("url", "http://mp.weixin.qq.com/s?__biz=MjM5MDM1ODExMQ==&mid=200867907&idx=1&sn=7119893f3ed7c8615b074347a56c9519#rd");
		intent.setClass(SettingAboutPetsayActivity.this, WebViewActivity.class);
		startActivity(intent);
	}
	

	private void onJumpWelcomeActivity() {
		Intent intent = new Intent();
		intent.setClass(this, GuideActivity.class);
		intent.putExtra("isStart", false);
		startActivity(intent);
	}
	
	@Override
	public void onPause() {
		PublicMethod.closeProgressDialog(mDialog, this);
		super.onPause();
	}

}
