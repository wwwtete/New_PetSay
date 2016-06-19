package com.petsay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;

import com.petsay.R;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.network.base.PetSayError;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;

import roboguice.fragment.RoboFragment;

/**
 * @author wangw
 *
 */
public class BaseFragment extends RoboFragment {

	
	protected View mView;
	protected View mLayoutRoot;
    protected ProgressDialog mDialog;
	
	protected void initView(View view){
		mView = view;
		if(mView != null){
			mLayoutRoot = mView.findViewById(R.id.layout_root);
		}
	}
	
	@Override
	public void onResume() {
		applySkin();
		super.onResume();
	}

	protected void applySkin() {
//		SkinHelp.setBackground(mLayoutRoot, getActivity(),getString(R.string.theme_bg));
	}

    protected void showLoading(boolean flag){
        closeLoading();
        mDialog = PublicMethod.creageProgressDialog(getActivity(), flag);
    }

    protected void showLoading(){
        closeLoading();
        mDialog = PublicMethod.creageProgressDialog(getActivity(),false);
    }

    protected void closeLoading(){
        PublicMethod.closeProgressDialog(mDialog, getActivity());
    }

    protected void showToast(int resId){
        PublicMethod.showToast(getActivity(), resId);
    }


    protected void showToast(String msg){
        PublicMethod.showToast(getActivity(), msg);
    }

    /**
     * 根据PetSayError错误状态码提示Toast
     * @param error
     */
    protected void onErrorShowToast(PetSayError error){
        closeLoading();
        if(error == null){
            showToast("未知错误");
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
                    showToast(R.string.network_getdata_error);
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
        startActivity(new Intent(getActivity(), UserLogin_Activity.class));
    }

    public String getActivePetId(){
        return UserManager.getSingleton().getActivePetId();
    }

}
