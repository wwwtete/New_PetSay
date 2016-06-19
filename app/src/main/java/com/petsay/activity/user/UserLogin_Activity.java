package com.petsay.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.application.PetSayApplication;
import com.petsay.chat.ChatMsgManager;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.utile.VerifyUserUtile;
import com.petsay.utile.json.JsonParse;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.decoration.DecorationDataManager;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import roboguice.inject.InjectView;

/**
 * @author wangw
 *	用户登陆Activity
 */
public class UserLogin_Activity extends BaseActivity implements NetCallbackInterface, OnClickListener,PlatformActionListener{

	@InjectView(R.id.titlebar)
	private TitleBar mTitleBar;
	@InjectView(R.id.edtxt_username)
	private EditText mEd_Name;
	@InjectView(R.id.edtxt_pwd)
	private EditText mEd_Pwd;
	@InjectView(R.id.iv_login)
	private Button mBtn_Login;
	@InjectView(R.id.txt_goreg)
	private TextView mTxt_GoReg;
	@InjectView(R.id.txt_forgetpwd)
	private TextView mTxt_ForgetPwd;
	//第三方登陆
	@InjectView(R.id.layout_login_sina)
	private LinearLayout mLayoutSINA;
	@InjectView(R.id.layout_login_qq)
	private LinearLayout mLayoutQQ;
	@InjectView(R.id.layout_login_wx)
	private LinearLayout mLayoutWX;
	
	private String platformName=""; 
	private String platformUsername=""; 
	private String hintName="";
	private final int CANCLE_OTHER_AUTH=99;
	private final int ERROR_OTHER_AUTH=98;
	private final int SUCCESS_OTHER_AUTH=100;
	private UserNet mUserNet;
	
	private PetSayApplication mApplication;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CANCLE_OTHER_AUTH:
				closeLoading();
				setBtnClickable(true);
				PublicMethod.showToast(getApplicationContext(), "已取消授权");
				break;
			case ERROR_OTHER_AUTH:
				closeLoading();
				setBtnClickable(true);
				if (msg.obj instanceof WechatClientNotExistException) {
					PublicMethod.showToast(getApplicationContext(), R.string.wechat_client_inavailable);
				}else {
					PublicMethod.showToast(getApplicationContext(), "授权失败");
				}
				break;
			case SUCCESS_OTHER_AUTH:
				setBtnClickable(true);
				PublicMethod.showToast(getApplicationContext(), "授权成功，登录中...");
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlogin_layout);
		mUserNet=new UserNet();
		mUserNet.setTag(UserLogin_Activity.this);
		mUserNet.setCallback(this);
		mApplication=(PetSayApplication) getApplication();
		initView();
	}
	
	protected void initView() {
		super.initView();
		mTitleBar.setTitleText("登陆宠物说");
		mTitleBar.setFinishEnable(true);
		mTxt_ForgetPwd.setOnClickListener(this);
		mTxt_GoReg.setOnClickListener(this);
		mBtn_Login.setOnClickListener(this);
		mLayoutQQ.setOnClickListener(this);
		mLayoutSINA.setOnClickListener(this);
		mLayoutWX.setOnClickListener(this);
//		UserInfo info=Db4oUtil.get(UserManager.getSingleton())
	}

	
//	@Override
//	protected void applySkin() {
//		super.applySkin();
////		SkinHelp.setRectangleBtnSelector(this, mBtn_Login);
//	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.iv_login:
			login();
			break;
		case R.id.txt_goreg:
			onGoReg();
			break;
		case R.id.txt_forgetpwd:
			onForgetPwd();
			break;
		case R.id.layout_login_sina:
			setBtnClickable(false);
			showLoading(false);
			platformName="sina";
			authorize(new SinaWeibo(this));
			break;
		case R.id.layout_login_qq:
			setBtnClickable(false);
			showLoading(false);
			platformName="qq";
			authorize(new  QQ(this));
			break;
		case R.id.layout_login_wx:
			setBtnClickable(false);
			showLoading(false);
			platformName="wechat";
			authorize(new  Wechat(this));
			break;
		}
	}
	
	private void authorize(Platform plat) {
		if (plat == null) {
			return;
		}
		String userId=null;
		if(plat.isValid()) {
			userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				checkUserName(plat);
				return;
			}
		}
		plat.setPlatformActionListener(this);
		plat.SSOSetting(false);
		plat.showUser(null);
		
	}

	private void onForgetPwd() {
		Intent intent = new Intent();
		intent.setClass(this, FindPassword_Activity.class);
		startActivityForResult(intent, 800);
	}




	private void onGoReg() {
		Intent intent = new Intent();
		intent.setClass(this, RegUser_Acivity.class);
		startActivity(intent);
		finish();
	}

	private void TurnToMain() {
		PushManager pushManager=PushManager.getInstance();
		String cid=pushManager.getClientid(UserLogin_Activity.this);
		if (null!=cid&&!cid.trim().equals("")) {
			UserManager userManager=UserManager.getSingleton();
			mUserNet.messageMPTS(userManager.getUserInfo().getId(), userManager.getActivePetId(), pushManager.getClientid(UserLogin_Activity.this));
		}
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 800:
			if(resultCode == RESULT_OK)
				this.finish();
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onDestroy() {
		mUserNet.cancelAll(UserLogin_Activity.this);
		super.onDestroy();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		handler.sendEmptyMessage(CANCLE_OTHER_AUTH);
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(SUCCESS_OTHER_AUTH);
			//授权成功
			if (platform.getName().equals(Wechat.NAME)) {
				platform.getDb().putUserId(res.get("openid").toString());
			}
			checkUserName(platform);
		}
		
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		arg2.printStackTrace();
		Message message=new Message();
		message.what=ERROR_OTHER_AUTH;
		message.obj=arg2;
		handler.sendMessage(message);
	}
	
    private void checkUserName(Platform plt){
    	
		if (plt.getName().equals(SinaWeibo.NAME)) {
			platformName="sina";
			platformUsername="sina"+plt.getDb().getUserId();
		}else if(plt.getName().equals(QQ.NAME)){
			platformName="qq";
			platformUsername="qq"+plt.getDb().getUserId();
		}else {
			platformName="wechat";
			PlatformDb db=plt.getDb();
			platformUsername="wx"+db.getUserId();
		}
		hintName=plt.getDb().getUserName();
		mUserNet.checkUsername(platformUsername);
    }
    
    /**
	 * 跳转到注册宠物信息
	 */
	private void turnPetInfoActivity(String platformName,String name,String pwd){
		Intent intent = new Intent();
		intent.putExtra(PetInfo_Acitivity.TURN_TYPE, PetInfo_Acitivity.TYPE_REG);
		intent.putExtra("loginName", name);
		intent.putExtra("password", pwd);
		intent.putExtra("platform", platformName);
		intent.putExtra("hintName", hintName);
		intent.setClass(this, PetInfo_Acitivity.class);
		startActivity(intent);
		finish();
	}
	
	private void setBtnClickable(boolean clickable){
		mLayoutQQ.setClickable(clickable);
		mLayoutSINA.setClickable(clickable);
		mLayoutWX.setClickable(clickable);
	}
	
	private void login(){
//		if (name.trim().equals("")) {
//			PublicMethod.showToast(UserLogin_Activity.this, R.string.login_name);
//		}else if (pwd.equals("")) {
//			PublicMethod.showToast(UserLogin_Activity.this, R.string.login_pwd);
//		}else {
//			
//			mUserNet.login(name, pwd);
//		}
		
		if(VerifyUserUtile.verifyMobileNumber(this, mEd_Name) &&
				VerifyUserUtile.verifyPwd(this, mEd_Pwd)){
			String name=mEd_Name.getText().toString();
			String pwd=mEd_Pwd.getText().toString();
			mUserNet.login(name, pwd);
		}
		
		
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		int errorCode=bean.getError();
		if (requestCode==RequestCode.REQUEST_LOGIN) {
			switch (errorCode) {
			case 200:
				setBtnClickable(true);
				closeLoading();
				JsonParse.getSingleton().parseLogin(UserLogin_Activity.this,bean.getValue());
                ToastUtiles.showDefault(getApplicationContext(), "登陆成功");				
                DecorationDataManager.getInstance(this).getServerDecorationData();
                ChatMsgManager.getInstance().auth();
//                Intent intent = new Intent();  
//                intent.setAction(ACTION_AFTERLOGIN);  
//                sendOrderedBroadcast(intent,null); 
                TurnToMain();
				break;
			case 500:
				closeLoading();
				 ToastUtiles.showDefault(UserLogin_Activity.this, bean.getMessage());
				break;
			case -1:
				loginFailed();
				break;
			}
		}else if (requestCode==RequestCode.REQUEST_CHECKUSERNAME) {
			boolean value = Boolean.parseBoolean(bean.getValue());
			switch (errorCode) {
			case 200:
				if (value)
					turnPetInfoActivity(platformName, platformUsername,platformUsername);
				else {
					closeLoading();
					setBtnClickable(true);
					mUserNet.login(platformUsername, platformUsername);
				}
				break;
			case -1:
				loginFailed();
				break;
			}
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		if(error == null){
			showToast("未知错误");
		} else if (error.getCode() == PetSayError.CODE_NETWORK_DISABLED) {
			showToast(R.string.network_disabled);
		} else {
			if (TextUtils.isEmpty(error.getMessage())) {
				onErrorShowToast(error);
			}else {
				int errorCode=error.getCode();
				if (requestCode==RequestCode.REQUEST_LOGIN) {
					switch (errorCode) {
					case 500:
						closeLoading();
						PublicMethod.showToast(UserLogin_Activity.this, error.getMessage());
						break;
					case -1:
						loginFailed();
						break;
					}
				}else if (requestCode==RequestCode.REQUEST_CHECKUSERNAME) {
						loginFailed();
				}
			}
			
		}
	}
	
	private void loginFailed(){
		closeLoading();
		PublicMethod.showToast(getApplicationContext(), "登录失败，请重试");
		setBtnClickable(true);
	}
	
}
