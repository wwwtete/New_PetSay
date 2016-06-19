package com.petsay.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.global.WebViewActivity;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.VerifyCodeButton;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.VerifyUserUtile;
import com.petsay.vo.ResponseBean;

import roboguice.inject.InjectView;

/**
 * @author wangw
 *	用户注册界面
 */
public class RegUser_Acivity extends BaseActivity implements OnClickListener,NetCallbackInterface {

	public static RegUser_Acivity Instance;
	/**总的倒计时时间*/
	private long TOTALTIME = 60 * 1000;
//	public static final String TYPE = "reg";

	@InjectView(R.id.titlebar)
	private TitleBar mTitleBar;
	@InjectView(R.id.edtxt_reg_username)
	private EditText mEdtxt_Name;
	@InjectView(R.id.edtxt_reg_pwd)
	private EditText mEdtxt_Pwd;
	@InjectView(R.id.edtxt_code)
	private EditText mEdtxt_Code;
	@InjectView(R.id.txt_provision)
	private TextView mTxt_Provison;
	@InjectView(R.id.btn_sendcode)
	private VerifyCodeButton mBtn_Send;
	@InjectView(R.id.btn_reg)
	private Button mBtn_reg;
//	@Inject
//	private UserModule mUserModule;
//	private UserData mUserData;
//	@Inject
//	private UserService mService;
	private UserNet mUserNet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Instance = this;
		setContentView(R.layout.reguser_layout);
//		mUserData=new UserData(handler);
//		mService.addListener(this);
		mUserNet = new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
		initView();
	}
	
		
	protected void initView() {
		super.initView();
		mTitleBar.setTitleText("新用户注册");
		mTitleBar.setFinishEnable(true);
		mTxt_Provison.setText(Html.fromHtml(getString(R.string.reg_item)));
		mTxt_Provison.setOnClickListener(this);
		mBtn_reg.setOnClickListener(this);
		mBtn_Send.setOnClickListener(this);
		mBtn_Send.setEnabled(false);
		mBtn_Send.setBackgroundResource(R.drawable.fasongyanzhengma);
		mEdtxt_Name.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if(!PublicMethod.verifyMobileNumber(mEdtxt_Name.getText().toString())){
						PublicMethod.showToast(RegUser_Acivity.this, "请检查手机号码是否正确！");
					}else {
//						mUserModule.addListener(mUserData);
						mUserNet.checkUsername(mEdtxt_Name.getText().toString());
					}
				}
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_reg:
			if(verifyMobileNumber() && verifyPwd() && verifyCaptcha()){
				String phoneNum = mEdtxt_Name.getText().toString();
				String code = mEdtxt_Code.getText().toString();
				mUserNet.verifyCaptcha(phoneNum, "001", code);
			}
			break;
		case R.id.btn_sendcode:
			onSendCode();
			break;
		case R.id.txt_provision:
			jumpWebViewActivity();
			break;
		}
	}
	
	private boolean verifyMobileNumber(){
		return VerifyUserUtile.verifyMobileNumber(this, mEdtxt_Name);
	}
	
	private boolean verifyCaptcha(){
		return VerifyUserUtile.verifyCaptcha(this, mEdtxt_Code);
	}
	
	private boolean verifyPwd(){
		return VerifyUserUtile.verifyPwd(this, mEdtxt_Pwd);
	}
	
//	@Override
//	protected void applySkin() {
//		super.applySkin();
//		SkinHelp.setRectangleBtnSelector(this, mBtn_reg);
//	}
	
	private void jumpWebViewActivity(){
		Intent intent = new Intent();
		intent.putExtra("folderPath", "宠物说用户协议");
		intent.putExtra("url", "file:///android_asset/agreement.html");
		intent.setClass(RegUser_Acivity.this, WebViewActivity.class);
		startActivity(intent);
	}

//	/**
//	 * 注册
//	 */
//	private void onReg() {
//		if (TextUtils.isEmpty(mEdtxt_Name.getText().toString())) {
//			PublicMethod.showToast(this, "密码不能为空！");
//		}else if(TextUtils.isEmpty(mEdtxt_Pwd.getText().toString())) {
//			PublicMethod.showToast(this, "密码不能为空！");
//		}else if(mEdtxt_Pwd.getText().toString().length()<5){
//			PublicMethod.showToast(this, "密码不能小于六位");
//		}else {
//			
//		}
//	}

	/**
	 * 跳转到注册宠物信息
	 */
	private void turnPetInfoActivity(){
		Intent intent = new Intent();
		intent.putExtra(PetInfo_Acitivity.TURN_TYPE, PetInfo_Acitivity.TYPE_REG);
		intent.putExtra("loginName", mEdtxt_Name.getText().toString());
		intent.putExtra("password", mEdtxt_Pwd.getText().toString());
		intent.setClass(this, PetInfo_Acitivity.class);
		startActivity(intent);
	}

	/**
	 * 发送验证码
	 */
	private void onSendCode() {
		String phoneNum =mEdtxt_Name.getText().toString();
//		if (TextUtils.isEmpty(mEdtxt_Name.getText().toString())) {
//			PublicMethod.showToast(this, "手机号不能为空！");
//		}else if(TextUtils.isEmpty(mEdtxt_Pwd.getText().toString())) {
//			PublicMethod.showToast(this, "密码不能为空！");
//		}else if(mEdtxt_Pwd.getText().toString().length()<5){
//			PublicMethod.showToast(this, "密码不能小于六位");
//		}else {
//			mUserNet.getCaptcha(phoneNum, "001");
//			mBtn_Send.setBackgroundResource(R.drawable.fasongyanzhengma);
//			mBtn_Send.start(TOTALTIME, 1000);
//		}
		
		if(verifyMobileNumber() && verifyPwd()){
			mUserNet.getCaptcha(phoneNum, "001");
			mBtn_Send.setBackgroundResource(R.drawable.fasongyanzhengma);
			mBtn_Send.start(TOTALTIME, 1000);
		}
		
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		if(mBtn_Send != null)
			mBtn_Send.release();
		Instance = null;
		if(mUserNet != null){
			mUserNet.cancelAll(this);
			mUserNet = null;
		}
		super.onDestroy();
	}

	public void onGetCaptcha(ResponseBean bean) {
		if(bean == null || (bean != null && bean.getError() != ProtocolManager.SUCCESS_CODE))
			mBtn_Send.reset();
		
	}

	public void onVerifyCaptcha(ResponseBean bean) {
		if (bean != null && "true".equals(bean.getValue())) {
			turnPetInfoActivity();
			return;
		}
		PublicMethod.showToast(this, R.string.verifycode_error);
		
	}


	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_GETCAPTCHA:
			onGetCaptcha(bean);
			break;

		case RequestCode.REQUEST_VERIFYCAPTCHA:
			onVerifyCaptcha(bean);
			break;
		case RequestCode.REQUEST_CHECKUSERNAME:
			boolean value = Boolean.parseBoolean(bean.getValue());
			if (value){
				mBtn_Send.setEnabled(true);
				mBtn_Send.setBackgroundResource(R.drawable.fasongyanzhengmanormal);
			}else{
				PublicMethod.showToast(getApplicationContext(), "此手机号已注册！");
			}
			break;
		}
	}


	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		if(TextUtils.isEmpty(error.getMessage())){
			onErrorShowToast(error);
		}else {
			showToast(error.getMessage());
		}
	}
}
