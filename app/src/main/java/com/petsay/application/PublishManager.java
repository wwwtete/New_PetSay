package com.petsay.application;

import android.database.Observable;
import android.text.TextUtils;

import com.petsay.component.view.UploadView;
import com.petsay.component.view.publishtalk.UploadPetalkView;
import com.petsay.constants.Constants;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UploadTokenNet;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PublishTalkParam;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/2
 * @Description
 */
public abstract class PublishManager extends Observable<PublishManager.PublishTalkManagerCallback> implements NetCallbackInterface {

    /**当前状态：-2：获取Token失败 -1：未初始化 0：获取Token中 1：获取Token成功 */
    protected int mState = -1;
    protected UploadView mUploadView;
    protected boolean mUploadViewInvaild;
    protected List<PublishTalkManagerCallback> mCallbackList;
    protected UploadTokenNet mTokenNet;
    protected LinkedList<UploadView> mWaitQueue;


    public interface PublishTalkManagerCallback{
        public void onDeleteLocalData(PublishTalkParam param);
    }

    public PublishManager() {
        mWaitQueue = new LinkedList<UploadView>();
    }

    public void startUpload(UploadView uploadView){
        mUploadViewInvaild = false;
        mUploadView = uploadView;
        if(TextUtils.isEmpty(Constants.UPLOAD_TOKEN)){
            mWaitQueue.addFirst(uploadView);
            if(mState < 0) {
                getUploadToken();
            }
        }else {
            uploadView.startUpload();
        }
    }

    /**
     * 因为Token无效，尝试重新上传
     * @param uploadView
     */
    public void retryUploadByTokenInvaild(UploadView uploadView){
        if(mState < 0){
            mWaitQueue.addFirst(uploadView);
            getUploadToken();
        }else {
            uploadView.startUpload();
        }
    }

    protected void getUploadToken(){
        mState = 0;
        if (mTokenNet == null) {
            mTokenNet = new UploadTokenNet();
            mTokenNet.setCallback(this);
        }
        mTokenNet.getUploadToken();
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        mState = 1;
        Constants.UPLOAD_TOKEN = bean.getValue();
        checkWaitQueue(true);
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        mState = -2;
        Constants.UPLOAD_TOKEN = "";
        checkWaitQueue(false);
    }

    private void checkWaitQueue(boolean isSccuess){
        synchronized (mWaitQueue) {
            while (!mWaitQueue.isEmpty()) {
                if (isSccuess)
                    mWaitQueue.pollFirst().startUpload();
                else
                    mWaitQueue.pollFirst().uploadFail();
            }
        }
    }

    /**
     * 弹出将要上传的UploadView，只能弹出一次
     * @return
     */
    public UploadView popupUploadView(){
        if(mUploadViewInvaild)
            return null;
        mUploadViewInvaild = true;
        return mUploadView;
    }


}
