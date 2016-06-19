package com.petsay.activity.settings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.activity.chat.ChatSettingActivity;
import com.petsay.activity.petalk.publishtalk.DraftboxActivity;
import com.petsay.activity.user.ModifyPassword_Activity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.chat.ChatMsgManager;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.database.DBManager;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.task.ClearCacheTask;
import com.petsay.utile.task.ClearCacheTask.ClearCacheTaskListener;
import com.petsay.vo.ResponseBean;

import java.io.File;

public class SettingActivity extends BaseActivity implements OnClickListener, NetCallbackInterface {

	private RelativeLayout mLayoutModifyPwd, mLayoutFeed,
			 mLayoutClearCache, mLayoutAboutPetsay, mLayoutSkin,
			mLayoutAutoPlay, mLayoutPic,mLayoutDraft;
	private TitleBar mTitleBar;
	private Button mBtn_Login;
	private UserManager mUserManager;

	private ProgressDialog mDialog;
	private TextView mTvSize;
	private File mAudioCache;
	private File mImageCache;

//	private UserModule mUserModule;
//	private UserData mUserData;
	private UserNet mUserNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		mUserModule = UserModule.getSingleton();
//		mUserData = new UserData(mHandler);
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
		setContentView(R.layout.setting);

		mUserManager = UserManager.getSingleton();
		initView();
		setListener();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		super.initView();
		initTitleBar();
		mBtn_Login = (Button) findViewById(R.id.iv_login);
		mLayoutModifyPwd = (RelativeLayout) findViewById(R.id.layout_modify_pwd);
		mLayoutFeed = (RelativeLayout) findViewById(R.id.layout_feed);
		mLayoutClearCache = (RelativeLayout) findViewById(R.id.layout_clearcache);
		mLayoutAboutPetsay = (RelativeLayout) findViewById(R.id.layout_aboutpetsay);
		// mLayoutHelp=(RelativeLayout)findViewById(R.id.layout_help);
		mLayoutSkin = (RelativeLayout) findViewById(R.id.layout_skin);
		mLayoutAutoPlay = (RelativeLayout) findViewById(R.id.layout_setplay);
		mLayoutPic = (RelativeLayout) findViewById(R.id.layout_picsetting);
        mLayoutDraft=(RelativeLayout) findViewById(R.id.layout_draft);
		mTvSize = (TextView) findViewById(R.id.tv_size);
		mAudioCache = new File(FileUtile.getPath(this,
				Constants.AUDIO_DOWNLOAD_PATHE));
		mImageCache = new File(FileUtile.getPath(this,
				Constants.IMAGE_CACHE_PATH));
		refreshCacheSize();

		// mLayoutModifyPwd.setBackgroundColor(Color.WHITE);
		mLayoutFeed.setBackgroundColor(Color.WHITE);
		mLayoutModifyPwd.setBackgroundColor(Color.WHITE);
		mLayoutClearCache.setBackgroundColor(Color.WHITE);
		mLayoutAboutPetsay.setBackgroundColor(Color.WHITE);
		mLayoutSkin.setBackgroundColor(Color.WHITE);
		mLayoutAutoPlay.setBackgroundColor(Color.WHITE);
		mLayoutPic.setBackgroundColor(Color.WHITE);
	}

	private void initTitleBar() {
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		mTitleBar.setTitleText("设置");
		mTitleBar.setFinishEnable(true);
	}

	private void setListener() {
		mBtn_Login.setOnClickListener(this);
		mLayoutModifyPwd.setOnClickListener(this);
		mLayoutFeed.setOnClickListener(this);
		mLayoutClearCache.setOnClickListener(this);
		mLayoutAboutPetsay.setOnClickListener(this);
		mLayoutSkin.setOnClickListener(this);
		mLayoutAutoPlay.setOnClickListener(this);
		mLayoutPic.setOnClickListener(this);
		mLayoutDraft.setOnClickListener(this);
		findViewById(R.id.layout_chatsetting).setOnClickListener(this);
	}		

	@Override
	public void onResume() {
		super.onResume();
		if (mUserManager.isLoginStatus()) {
			String username = UserManager.getSingleton().getUserInfo()
					.getLoginName();

			if (username.indexOf("sina") != -1 || username.indexOf("qq") != -1
					|| username.indexOf("wx") != -1) {
				// 第三方登陆
				mLayoutModifyPwd.setVisibility(View.GONE);
			} else {
				mLayoutModifyPwd.setVisibility(View.VISIBLE);
			}
			mLayoutDraft.setVisibility(View.VISIBLE);
			mBtn_Login.setText(getString(R.string.logout));
		} else {
			mLayoutDraft.setVisibility(View.GONE);
			mLayoutModifyPwd.setVisibility(View.GONE);
			mBtn_Login.setText(getString(R.string.login));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_login:
			if (mUserManager.isLoginStatus()) {
				mUserNet.logout( UserManager.getSingleton().getUserInfo().getId());
				// MainActivity activity=(MainActivity) getActivity();
//				mUserManager.setUserInfo(null);
				getUserManager().logout();
				// activity.setUnreadMsgCount(0);

				mBtn_Login.setText(getString(R.string.login));
				PublicMethod.showToast(SettingActivity.this, "已成功注销");
				mLayoutModifyPwd.setVisibility(View.GONE);
				ChatMsgManager.getInstance().closeClient();
				DBManager.getInstance().close();
			} else
				onTurnLogin();
			break;
		case R.id.layout_modify_pwd:
			if (mUserManager.isLoginStatus()) {
				jumpModifyPwd();
			} else {
				onTurnLogin();
			}
			break;
		case R.id.layout_aboutpetsay:
			onJumpAboutPetsayActivity();

			break;
		case R.id.layout_clearcache:
			onClearCache();
			break;
		case R.id.layout_feed:
			Intent intent = new Intent(SettingActivity.this,
					FeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_aboutus:
			intent = new Intent(SettingActivity.this,CheckVersionActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_setplay:
			onSetPlayMode();
			break;
		case R.id.layout_picsetting:
			intent = new Intent(SettingActivity.this, SetPicActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_chatsetting:
			onChatSetting();
			break;
		case R.id.layout_draft:
			onDraftBox();
			break;
		}
	}

	private void onDraftBox() {
		Intent intent = new Intent(SettingActivity.this, DraftboxActivity.class);
		startActivity(intent);

	}

	private void onChatSetting() {
		Intent intent = new Intent(SettingActivity.this,
				ChatSettingActivity.class);
		startActivity(intent);
	}

	private void onSetPlayMode() {
		Intent intent = new Intent();
		intent.setClass(SettingActivity.this, SetPlayMode_Activity.class);
		startActivity(intent);
	}

	private void onJumpMarketActivity() {
		try {
			Uri uri = Uri.parse("market://details?id=" + Constants.PackageName);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);
		} catch (Exception e) {
			PublicMethod.showToast(SettingActivity.this, "未检测到您手机安装任何应用市场");
		}
	}

	private void jumpWebViewActivity() {
		Intent intent = new Intent();
		intent.putExtra("folderPath", "宠物说小助手");
		intent.putExtra(
				"url",
				"http://mp.weixin.qq.com/s?__biz=MjM5MDM1ODExMQ==&mid=200867907&idx=1&sn=7119893f3ed7c8615b074347a56c9519#rd");
		intent.setClass(SettingActivity.this, WebViewActivity.class);
		startActivity(intent);
	}

	private void onClearCache() {
		mDialog = PublicMethod.creageProgressDialog(SettingActivity.this);
		String download_audio = FileUtile.getPath(SettingActivity.this,Constants.AUDIO_DOWNLOAD_PATHE);
		String imgCache = FileUtile.getPath(SettingActivity.this,Constants.IMAGE_CACHE_PATH);
		ClearCacheTask task = new ClearCacheTask();
		task.setOnListener(new ClearCacheTaskListener() {

			@Override
			public void onClearCacheCallback(boolean flag) {
				PublicMethod.closeProgressDialog(mDialog, SettingActivity.this);
				PublicMethod.showToast(SettingActivity.this, "缓存清理完毕");
				mTvSize.setText("0.0M");
			}
		});
		task.execute(download_audio, imgCache);
	}

	private void onJumpAboutPetsayActivity() {
		Intent intent = new Intent();
		intent.setClass(this, SettingAboutPetsayActivity.class);
		startActivity(intent);
	}

	/**
	 * 跳转到登陆页面
	 */
	private void onTurnLogin() {
		Intent intent = new Intent();
		intent.setClass(this, UserLogin_Activity.class);
		startActivity(intent);
	}

	private void jumpModifyPwd() {
		Intent intent = new Intent();
		intent.setClass(this, ModifyPassword_Activity.class);
		startActivity(intent);
	}

	private void refreshCacheSize() {
		float audioSize = FileUtile.getFolderSize(mAudioCache);
		float imgSize = FileUtile.getFolderSize(mImageCache);
		mTvSize.setText(String.format("%.2f", (audioSize + imgSize)) + "M");
	}

	@Override
	public void onPause() {
		PublicMethod.closeProgressDialog(mDialog, this);
		super.onPause();
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_LOGOUT:
			break;
		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
	}

}
