package com.petsay.application;

import android.database.Observable;
import android.text.TextUtils;

import com.petsay.constants.Constants;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UploadTokenNet;
import com.petsay.vo.ResponseBean;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/7
 * @Description
 */
public class UploadTokenManager extends Observable<UploadTokenManager.UploadTokenManagerCallback> implements NetCallbackInterface {

    public static UploadTokenManager mInstance;
    public static UploadTokenManager getInstance(){
        if(mInstance == null)
            mInstance = new UploadTokenManager();
        return mInstance;
    }

    private UploadTokenNet mNet;
    private String mToken;

    private UploadTokenManager(){
        mNet = new UploadTokenNet();
        mNet.setCallback(this);
        mNet.setTag(this);
    }

    /**
     * 从服务器获取Token
     */
    public void loadToken(){
        mToken = "";
        mNet.getUploadToken();
    }

    public String getToken(){
        return mToken;
    }

    /**
     * 检查Token是否有效
     * @return
     */
    public boolean checkvalid(){
        return !TextUtils.isEmpty(mToken);
    }

    /**
     * 获取数据成功回调接口
     *
     * @param bean        服务器返回数据
     * @param requestCode 区分请求码
     */
    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        mToken = bean.getValue();
        Constants.UPLOAD_TOKEN = bean.getValue();
        notifyCallback(null);
    }

    /**
     * 获取数据失败回调接口(也包括服务器返回500的错误)
     *
     * @param error       错误信息类
     * @param requestCode 请求码
     */
    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        notifyCallback(error);
    }

    private void notifyCallback(PetSayError error){
        for (UploadTokenManagerCallback callback:mObservers){
            if(error == null){
                callback.onGetTokenSuccess(mToken);
            }else {
                callback.onGetTokenError(error);
            }
        }
    }

    public interface UploadTokenManagerCallback{
        public void onGetTokenSuccess(String token);
        public void onGetTokenError(PetSayError error);
    }

}
