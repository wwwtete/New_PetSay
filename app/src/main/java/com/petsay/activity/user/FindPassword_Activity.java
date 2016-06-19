package com.petsay.activity.user;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.view.VerifyCodeButton;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.VerifyUserUtile;
import com.petsay.vo.ResponseBean;

/**
 * @author wangw
 * 找回密码
 */
public class FindPassword_Activity extends BaseActivity implements OnClickListener,NetCallbackInterface {

	private long TOTALTIME = 60 * 1000;
	@InjectView(R.id.ev_phonenum) 
	private EditText mPhonenum;
	@InjectView(R.id.btn_sendcode) 
	private VerifyCodeButton mBtnSendcode;
	@InjectView(R.id.edtxt_code) 
	private EditText mEvCode;
	@InjectView(R.id.btn_next) 
	private Button mBtnNext;
	private UserNet mUserNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.findpwd_layout);
		initView();
	}

	protected void initView() {
		super.initView();
		mUserNet = new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
		mBtnNext.setOnClickListener(this);
		mBtnSendcode.setOnClickListener(this);
		initTitleBar(R.string.findpwd_title);
		mTitleBar.setFinishEnable(true);
	}

//	@Override
//	protected void applySkin() {
//		super.applySkin();
//		SkinHelp.setRectangleBtnSelector(this, mBtnNext);
//	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_sendcode:
			onSendCode();
			break;
		case R.id.btn_next:
			onNext();
			break;
		}
	}

	/**
	 * 找回密码
	 */
	private void onNext() {
		String phoneNum = mPhonenum.getText().toString();
		String captcha = mEvCode.getText().toString();
		if(VerifyUserUtile.verifyMobileNumber(this, mPhonenum) && 
				VerifyUserUtile.verifyCaptcha(this, mEvCode)){
			mUserNet.verifyCaptcha(phoneNum, "002", captcha);
		}
	}

	/**
	 * 发送验证码
	 */
	private void onSendCode() {
		String phoneNum = mPhonenum.getText().toString();
		
		if(VerifyUserUtile.verifyMobileNumber(this, mPhonenum)){
			mUserNet.getCaptcha(phoneNum, "002");
			mBtnSendcode.setEnabled(false);
			mBtnSendcode.setBackgroundResource(R.drawable.fasongyanzhengma);
			mBtnSendcode.start(TOTALTIME, 1000);
		}
	}

	public void onGetCaptcha(ResponseBean bean) {
		if(bean == null || (bean != null && bean.getError() != ProtocolManager.SUCCESS_CODE))
			mBtnSendcode.reset();
	}

	public void onVerifyCaptcha(ResponseBean bean) {
		if(bean != null && "true".equals(bean.getValue())){
			jumpModifyPwd();
			return;
		}
		PublicMethod.showToast(this, R.string.verifycode_error);
	}

	@Override
	protected void onDestroy() {
		if(mUserNet != null){
			mUserNet.cancelAll(this);
			mUserNet = null;
		}
		super.onDestroy();
	}

	public void jumpModifyPwd(){
		setResult(RESULT_OK);
		Intent intent = new Intent();
		intent.setClass(this, ModifyPassword_Activity.class);
		intent.putExtra("phonenum", mPhonenum.getText().toString());
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_VERIFYCAPTCHA:
			onVerifyCaptcha(bean);
			break;

		case RequestCode.REQUEST_GETCAPTCHA:
			onGetCaptcha(bean);
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		onErrorShowToast(error);
	}

}
