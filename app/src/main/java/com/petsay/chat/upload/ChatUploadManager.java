package com.petsay.chat.upload;

import android.text.TextUtils;

import com.emsg.sdk.client.android.asynctask.TaskCallBack;
import com.google.inject.Singleton;
import com.petsay.constants.Constants;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UploadTokenNet;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.qiniu_petsay.auth.JSONObjectRet;
import com.qiniu_petsay.io.IO;
import com.qiniu_petsay.io.PutExtra;
import com.qiniu_petsay.utils.QiniuException;

import org.json.JSONObject;

import java.io.File;
import java.util.LinkedList;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/22
 * @Description 聊天七牛上传
 */
@Singleton
public class ChatUploadManager implements NetCallbackInterface {

    private static ChatUploadManager instance;
    public static ChatUploadManager getInstance(){
        if(instance == null)
            instance = new ChatUploadManager();
        return instance;
    }

    private UploadTokenNet mTokenNet;
    private LinkedList<UploadModel> mWaitQueue;
    private ChatUploadManager(){
        init();
    }

    private void init() {
        mTokenNet = new UploadTokenNet();
        mTokenNet.setCallback(this);
        mWaitQueue = new LinkedList<UploadModel>();
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
       Constants.UPLOAD_TOKEN = bean.getValue();
       onGetTokenSuccess(bean);
    }

    private void onGetTokenSuccess(ResponseBean bean) {
        synchronized (mWaitQueue){
            while (!mWaitQueue.isEmpty()){
                UploadModel model = mWaitQueue.poll();
                upload(model.file,model.callBack);
            }
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @param callback
     */
    public void upload(File file, final TaskCallBack callback) {
        if(checkTokenEnable()){
            onUpload(file,callback);
        }else {
            addQueue(file,callback);
            mTokenNet.getUploadToken();
        }
    }

    private void onUpload(final File file, final TaskCallBack callback) {
        final String mKey = Constants.CHAT_SERVER_AUDIO +  file.getName();
        IO.putFile(Constants.UPLOAD_TOKEN, mKey, file, new PutExtra(), new JSONObjectRet() {

            @Override
            public void onProcess(long current, long total) {
            }

            @Override
            public void onFailure(QiniuException ex) {
                PublicMethod.log_e( mKey + ": 上传失败：失败原因：", ex.getMessage());
                //code = 401 代表Token失效
                if(ex.code == 401){
                    Constants.UPLOAD_TOKEN = "";
                    addQueue(file,callback);
                }else {
                    if (callback != null)
                        callback.onFailure();
                }
            }

            @Override
            public void onSuccess(JSONObject obj) {
                if (callback != null) {
                    callback.onSuccess(Constants.DOWNLOAD_SERVER +mKey);
                }
            }
        });
    }

    private void addQueue(File file,TaskCallBack callBack){
        UploadModel model = new UploadModel();
        model.file = file;
        model.callBack = callBack;
        mWaitQueue.addLast(model);
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        Constants.UPLOAD_TOKEN = "";
    }

    public boolean checkTokenEnable(){
        return !TextUtils.isEmpty(Constants.UPLOAD_TOKEN);
    }

    class UploadModel{
        public File file;
        public TaskCallBack callBack;
    }

}
