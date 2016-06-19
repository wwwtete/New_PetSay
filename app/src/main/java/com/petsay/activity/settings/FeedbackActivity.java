package com.petsay.activity.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UserNet;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;

public class FeedbackActivity extends BaseActivity implements NetCallbackInterface {
	private EditText mEd_content,mEd_info;
	private TitleBar mTitleBar;
	private TextView mTvCommit;
//	private UserModule mUserModule;
//	private UserData mUserData;
	private UserNet mUserNet;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		initView();
		initTitleBar();
//		mUserData=new UserData(handler);
//		mUserModule=UserModule.getSingleton();
		mUserNet=new UserNet();
		mUserNet.setCallback(this);
		mUserNet.setTag(this);
	}
	
	private void initTitleBar(){
//		titleBar.setTitleText(R.string.reg_pet_title);
		mTitleBar.setTitleText(R.string.feedback_title);
		mTitleBar.setFinishEnable(true);
		mTvCommit = PublicMethod.addTitleRightText(mTitleBar, "发送");
//		mTvCommit.setText(R.string.feedback_commit);
		mTvCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String content=mEd_content.getText().toString();
				String info=mEd_info.getText().toString();
				if (!TextUtils.isEmpty(content)&&!TextUtils.isEmpty(info)) {
					v.setEnabled(false);
//					mUserModule.addListener(mUserData);
					mUserNet.messageCULM(info, content);
				}else {
					PublicMethod.showToast(getApplicationContext(), R.string.feedback_error);
				}
			}
		});
	}
	
	protected void initView(){
		super.initView();
		mEd_content=(EditText) findViewById(R.id.ed_content);
		mEd_info=(EditText) findViewById(R.id.ed_info);
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_MESSAGECULM:
			PublicMethod.showToast(getApplicationContext(), "已提交反馈");
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_MESSAGECULM:
			mTvCommit.setEnabled(true);
			PublicMethod.showToast(getApplicationContext(), "提交失败");
			break;

		default:
			break;
		}
		
	}
}
