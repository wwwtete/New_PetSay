package com.petsay.component.view.functionbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.petsay.R;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PetalkVo;


/**
 * @author wangw
 * 功能条的事件处理类
 */
public abstract class BaseFunctionBarEventHolder implements OnClickListener {

	protected FunctionBarView mFunctionBarView;
	protected PetalkVo mPetalkVo;
	protected Context mContext;
	protected ProgressDialog mDialog;
	
	public BaseFunctionBarEventHolder(Context context,FunctionBarView view){
		this.mContext = context;
		setFunctionBarView(view);
//		init();
	}
	
	public BaseFunctionBarEventHolder(Context context,FunctionBarView view,PetalkVo petalkVo){
		this.mContext = context;
		setFunctionBarView(view);	
		this.setSayVo(petalkVo);
//		init();
	}
	
//	protected void init() {
//	}
	
	public void setSayVo(PetalkVo petalkVo){
		this.mPetalkVo = petalkVo;
	}
	
	public void setFunctionBarView(FunctionBarView view){
		this.mFunctionBarView = view;
		this.mFunctionBarView.initListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_comment:	//评论
			onClickCommentView();
			break;
		case R.id.layout_step:		//踩
			onClickStepView();
			break;
		case R.id.layout_share:		//分享
			onClickShareView();
			break;
		}
	}

	protected abstract void onClickCommentView();

	protected abstract void onClickStepView();

	protected abstract void onClickShareView();
	
	
	protected void showLoading() {
		closeLoading();
		mDialog = PublicMethod.creageProgressDialog(mContext);
	}
	
	protected void closeLoading(){
		PublicMethod.closeProgressDialog(mDialog, mContext);
	}
	
	protected boolean checkLogin(){
		return UserManager.getSingleton().isLoginStatus();
	}
	
	protected void jumpLoginActivity(){
		Intent intent=new Intent(mContext, UserLogin_Activity.class);
		mContext.startActivity(intent);
	}
	
}
