package com.petsay.component.view.functionbar;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.onekeyshare.Share.ShareCallback;
import cn.sharesdk.onekeyshare.SharePopupWindow;

import com.petsay.activity.petalk.DetailActivity;
import com.petsay.activity.user.UserLogin_Activity;
import com.petsay.component.view.BasePopupWindow.IAddShowLocationViewService;
import com.petsay.component.view.petalklistitem.ListItemStepLayout;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.CommentVo;
import com.petsay.vo.petalk.PetVo;

/**
 * @author wangw
 * 列表功能条事件处理类
 */
public class ListItemFunctionBarEventHolder extends BaseFunctionBarEventHolder implements ShareCallback, NetCallbackInterface{

//	protected SayData mSayData;
//	private SayModule mSayModule;
	private SayDataNet mSayDataNet;
	private IAddShowLocationViewService mViewService;
    private ListItemStepLayout mItemStepLayout;

	public ListItemFunctionBarEventHolder(Context context, FunctionBarView view,IAddShowLocationViewService viewService) {
		super(context, view);
		mViewService = viewService;
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(context);
	}

    public void setItemStepLayout(ListItemStepLayout layout){
        this.mItemStepLayout = layout;
    }
	
	@Override
	protected void onClickCommentView() {
		if (checkLogin()) {
			Intent intent = new Intent();
			intent.setClass(mContext, DetailActivity.class);
			Constants.Detail_Sayvo=mPetalkVo;
			intent.putExtra("operationType", 2);
			mContext.startActivity(intent);
		} else {
			jumpLoginActivity();
		}
	}

	@Override
	protected void onClickStepView() {
		if (UserManager.getSingleton().isLoginStatus()) {
			if (mPetalkVo.getZ()==0) {
				mFunctionBarView.startStepAnimation();
				mPetalkVo.setZ(1);
				int count=mPetalkVo.getCounter().getFavour();
				mPetalkVo.getCounter().setFavour(count+1);
				if (UserManager.getSingleton().isLoginStatus()) {
					mSayDataNet.interactionCreate(mPetalkVo.getPetalkId(),
							Constants.FAVOUR,UserManager.getSingleton().getActivePetInfo().getId(), mPetalkVo.getPetId());
				}else
					mSayDataNet.interactionCreate( mPetalkVo.getPetalkId(),Constants.FAVOUR,"", mPetalkVo.getPetId());
				mFunctionBarView.addStepCount(mPetalkVo.getPetalkId());
                updateStepLayout();
			}else{
                PublicMethod.showToast(mContext, "已赞过");
			}
		}else {
			Intent intent=new Intent(mContext, UserLogin_Activity.class);
			mContext.startActivity(intent);
		}
		
	}

    private void updateStepLayout() {
        if(mItemStepLayout != null){
            List<CommentVo> vos = mPetalkVo.getF();
            CommentVo vo= new CommentVo();
            PetVo petInfo = UserManager.getSingleton().getActivePetInfo();
            vo.setPetId(UserManager.getSingleton().getActivePetId());
            vo.setPetHeadPortrait(petInfo.getHeadPortrait());
            vos.add(0,vo);
            mItemStepLayout.addFirstView(vo,vos.size());
        }
    }

    @Override
	protected void onClickShareView() {
		SharePopupWindow popupWindow=new SharePopupWindow(mContext, mPetalkVo,mViewService,this,false);
		popupWindow.show();
		
	}

	@Override
	public void shareError(Platform platform, int arg1, Throwable arg2) {
		
	}

	@Override
	public void shareComplete(Platform platform, int arg1,HashMap<String, Object> arg2) {
		if (checkLogin()) {
			mSayDataNet.interactionCreate( mPetalkVo.getPetalkId(), Constants.SHARE, UserManager.getSingleton().getActivePetInfo().getId(), mPetalkVo.getPetId());
		}else {
			mSayDataNet.interactionCreate( mPetalkVo.getPetalkId(), Constants.SHARE, "0", mPetalkVo.getPetId());
		}
	}

	@Override
	public void shareCancel(Platform platform, int arg1) {}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_INTERACTIONCREATE:
			String type=(String) bean.getTag();
			if (type.equals(Constants.SHARE)) {
				ToastUtiles.showCenter(mContext, bean.getMessage());
				mFunctionBarView.addShareCount();
				mPetalkVo.getCounter().setShare(mFunctionBarView.getShareCount());
			} else {
				ToastUtiles.showCenter(mContext, bean.getMessage());
				mPetalkVo.setZ(1);
			}
			break;
		}
	}

	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		switch (requestCode) {
		case RequestCode.REQUEST_INTERACTIONCREATE:
			break;
		default:
			break;
		}
	}
}
