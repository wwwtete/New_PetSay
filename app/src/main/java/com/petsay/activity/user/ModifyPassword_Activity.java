package com.petsay.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.VerifyUserUtile;
import com.petsay.vo.ResponseBean;

import roboguice.inject.InjectView;

/**
 * @author wangw
 * 修改验证码
 */
public class ModifyPassword_Activity extends BaseActivity implements OnClickListener,NetCallbackInterface {


	@InjectView(R.id.ev_old_pwd) 
	private EditText mEvOldPwd;
	@InjectView(R.id.ev_new_pwd) 
	private EditText mEvNewPwd;
	@InjectView(R.id.ev_verify_pwd) 
	private EditText mEvVerifyPwd;
	@InjectView(R.id.btn_ok) 
	private Button mBtnOk;
	private UserNet mUserNet;
	private String mPhoneNum;

	/**是否从忘记密码跳转过来的*/
	private boolean mIsForgetPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifypwd_layout);
		mPhoneNum = getIntent().getStringExtra("phonenum");
		mIsForgetPwd = !TextUtils.isEmpty(mPhoneNum);
		mBtnOk.setOnClickListener(this);
//		mService.addListener(this);
		mUserNet = new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
		initView();
		if(mIsForgetPwd)
			initForgetView();
		else
			initModifyViews();
	}
	
//	@Override
//	protected void applySkin() {
//		super.applySkin();
//		SkinHelp.setRectangleBtnSelector(this, mBtnOk);
//	}

	/**
	 * 从忘记密码跳转过来
	 */
	private void initForgetView() {
		initTitleBar(R.string.modifypwd_forget_title);
		mTitleBar.setFinishEnable(true);
		mEvOldPwd.setVisibility(View.GONE);
	}

	/**
	 * 从修改密码跳转过来
	 */
	private void initModifyViews() {
		initTitleBar(R.string.modifypwd_modify_title);
		mTitleBar.setFinishEnable(true);
		mEvOldPwd.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onDestroy() {
		if(mUserNet != null){
			mUserNet.cancelAll(this);
			mUserNet = null;
			}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if(verifyPwd()){
				if(mIsForgetPwd)
					onForgetClick();
				else
					onModifyClick();
			}
			break;
		}
	}

	/**
	 * 重置密码
	 */
	private void onForgetClick(){
		mUserNet.restPassword(mPhoneNum, mEvNewPwd.getText().toString());
	}

	/**
	 * 修改密码
	 */
	private void onModifyClick(){
		String oldPwd = mEvOldPwd.getText().toString();
		String newPwd = mEvNewPwd.getText().toString();
		String id = UserManager.getSingleton().getUserInfo().getId();
		mUserNet.modifyPassword(oldPwd, newPwd, id);
	}

	private boolean verifyPwd(){
//		if(TextUtils.isEmpty(verify) || TextUtils.isEmpty(newPwd)){
//			showToast("密码不能为空");
//			return false;
//		}
//
//		if(newPwd.length() < 5){
//			showToast("密码不能少于5位");
//			return false;
//		}
//		if(!verify.equals(newPwd)){
//			showToast("密码不一致");
//			return false;
//		}
		boolean flag = true;
		if(!mIsForgetPwd){
			flag = VerifyUserUtile.verifyPwd(this, mEvOldPwd);
		}
		
		if(flag && VerifyUserUtile.verifyPwd(this, mEvNewPwd) && 
				VerifyUserUtile.verifyPwd(this, mEvVerifyPwd)){
			String verify = mEvVerifyPwd.getText().toString();
			String newPwd = mEvNewPwd.getText().toString();
			if(verify.equals(newPwd)){
				return true;
			}else {
				showToast("密码不一致");
				PublicMethod.startShakeAnimation(this, mEvVerifyPwd);
				return false;
			}
		}
		return false;
	}

	public void onRestPassword(ResponseBean bean) {
		if(bean != null && bean.getError() == ProtocolManager.SUCCESS_CODE){
			showToast(bean.getMessage());
			jumpLogin();
			return;
		}
		if(bean != null)
			showToast(bean.getMessage());
		else
			showToast("设定密码失败！");
	}
	
	private void jumpLogin(){
		Intent intent = new Intent();
		intent.setClass(this, UserLogin_Activity.class);
		startActivity(intent);
		this.finish();
	}

	public void onModifyPassword(ResponseBean bean) {
		if(bean != null && bean.getError() == ProtocolManager.SUCCESS_CODE){
			showToast(bean.getMessage());
			this.finish();
			return;
		}
		if(bean != null)
			showToast(bean.getMessage());
		else
			showToast("修改密码失败！");
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_RESTPASSWORD:
			onRestPassword(bean);
			break;

		case RequestCode.REQUEST_MODIFYPASSWORD:
			onModifyPassword(bean);
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		if (TextUtils.isEmpty(error.getMessage())) {
			onErrorShowToast(error);
		}else{
			showToast(error.getMessage());
		}
	}



}
