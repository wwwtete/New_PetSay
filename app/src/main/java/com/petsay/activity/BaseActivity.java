package com.petsay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.petsay.R;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.application.UserManager;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.chat.ChatMsgManager;
import com.petsay.component.view.TitleBar;
import com.petsay.network.base.PetSayError;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.vo.petalk.PetVo;

import roboguice.activity.RoboFragmentActivity;

public class BaseActivity extends RoboFragmentActivity {

	protected TitleBar mTitleBar;
	protected ProgressDialog mDialog;
	protected ViewGroup mLayoutRoot;
	
//	public static final  String ACTION_AFTERLOGIN="AfterLogin";
//	public static final String ACTION_AFTERLOGOUT="AfterLogout";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		initData();
		SharePreferenceCache.getSingleton(getApplicationContext()).setIsRunningApp(true);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		 IntentFilter intentFilter = new IntentFilter();
//	        intentFilter.addAction("AfterLogin");
//	        intentFilter.addAction("AfterLogout");
//	        registerReceiver(receiver, intentFilter);
	}

	protected void initView(){
		mLayoutRoot = (ViewGroup) findViewById(R.id.layout_root);
	}

	@Override
	protected void onResume() {
		SharePreferenceCache.getSingleton(getApplicationContext()).setIsRunningApp(true);
        if(isLogin() && !ChatMsgManager.getInstance().isAuth())
            ChatMsgManager.getInstance().auth();
		super.onResume();
	}

	protected void initTitleBar(String title){
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		if(mTitleBar != null)
			mTitleBar.setTitleText(title);
	}

    protected void initTitleBar(String title,boolean finsihEnable){
        mTitleBar = (TitleBar) findViewById(R.id.titlebar);
        if(mTitleBar != null) {
            mTitleBar.setTitleText(title);
            mTitleBar.setFinishEnable(finsihEnable);
        }
    }

	protected void initTitleBar(int res){
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		if(mTitleBar != null)
			mTitleBar.setTitleText(res);
	}

	protected void showLoading(boolean canCancle){
		closeLoading();
		mDialog = PublicMethod.creageProgressDialog(BaseActivity.this, canCancle);
	}

	protected void showLoading(){
		closeLoading();
		mDialog = PublicMethod.creageProgressDialog(BaseActivity.this,true);
	}

	protected void closeLoading(){
		PublicMethod.closeProgressDialog(mDialog, getApplicationContext());
	}

	protected void showToast(int resId){
        ToastUtiles.showDefault(getApplicationContext(), resId);
	}


	protected void showToast(String msg){
        ToastUtiles.showDefault(getApplicationContext(), msg);
	}

	@Override
	protected void onPause() {
        //		closeLoading();
        SharePreferenceCache.getSingleton(getApplicationContext()).setIsRunningApp(false);
		super.onPause();
	}

    @Override
    protected void onStop() {
        super.onStop();
        if(!PublicMethod.isAppTopRuning(this))
            ChatMsgManager.getInstance().closeClient();
    }

    /**
	 * 根据PetSayError错误状态码提示Toast
	 * @param error
	 */
	protected void onErrorShowToast(PetSayError error){
        closeLoading();
		if(error == null){
			showToast(R.string.network_error);
		}else {
			switch (error.getCode()){
				case PetSayError.CODE_NETWORK_DISABLED:
					showToast(R.string.network_disabled);
					break;
				case PetSayError.CODE_SESSIONTOKEN_DISABLE:
                case PetSayError.CODE_PERMISSION_ERROR:
					onSessionTokenDisable(error);
					break;
				default:
					if(TextUtils.isEmpty(error.getMessage()))
						showToast(R.string.network_getdata_error);
					else
						showToast(error.getMessage());
					break;
			}
		}
	}

	/**
	 * Token失效错误异常处理
	 * @param error
	 */
	protected void onSessionTokenDisable(PetSayError error) {
		showToast(R.string.seesiontoken_error);
		jumpLoginActivity();
		finish();
	}
	protected void jumpLoginActivity(){
		startActivity(new Intent(this, UserLogin_Activity.class));
	}

	public UserManager getUserManager(){
        return UserManager.getSingleton();
    }

    public boolean isLogin(){
        return getUserManager().isLoginStatus();
    }

    protected PetVo getActivePetInfo(){
        return UserManager.getSingleton().getActivePetInfo();
    }

	@Override
	protected void onDestroy() {
		closeLoading();
		super.onDestroy();
	}

	

    public String getActivePetId(){
        return UserManager.getSingleton().getActivePetId();
    }
}
